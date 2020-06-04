package fr.fresnel.fourPolar.ui.algorithms.preprocess;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.IntStream;

import fr.fresnel.fourPolar.algorithm.preprocess.darkBackground.estimator.IChannelDarkBackgroundEstimator;
import fr.fresnel.fourPolar.algorithm.preprocess.realignment.ChannelRealigner;
import fr.fresnel.fourPolar.algorithm.preprocess.realignment.IChannelRealigner;
import fr.fresnel.fourPolar.algorithm.preprocess.registration.IChannelRegistrator;
import fr.fresnel.fourPolar.algorithm.preprocess.segmentation.BeadCapturedImageSetSegmenter;
import fr.fresnel.fourPolar.algorithm.preprocess.segmentation.ICapturedImageSetSegmenter;
import fr.fresnel.fourPolar.algorithm.util.image.color.GrayScaleToColorConverter.Color;
import fr.fresnel.fourPolar.algorithm.visualization.figures.registration.RegistrationCompositeFigureCreator;
import fr.fresnel.fourPolar.core.image.captured.ICapturedImageSet;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.imageSet.acquisition.AcquisitionSet;
import fr.fresnel.fourPolar.core.imageSet.acquisition.bead.BeadImageSet;
import fr.fresnel.fourPolar.core.imageSet.acquisition.sample.SampleImageSet;
import fr.fresnel.fourPolar.core.physics.channel.ChannelUtils;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.preprocess.PreprocessResult;
import fr.fresnel.fourPolar.core.preprocess.darkBackground.IChannelDarkBackground;
import fr.fresnel.fourPolar.core.preprocess.registration.IChannelRegistrationResult;
import fr.fresnel.fourPolar.core.preprocess.registration.RegistrationRule;
import fr.fresnel.fourPolar.core.visualization.figures.polarization.IPolarizationImageSetComposites;
import fr.fresnel.fourPolar.core.visualization.figures.polarization.PolarizationImageSetComposites;
import fr.fresnel.fourPolar.io.image.captured.ICapturedImageSetReader;
import fr.fresnel.fourPolar.io.visualization.figures.polarization.IRegistrationCompositeFiguresWriter;
import javassist.tools.reflect.CannotCreateException;

/**
 * Processes a given {@link AcquisitionSet} (mostly intended for
 * {@link BeadImageSet}, but if no such set exists, a replacement an alias for
 * bead set in the form of a {@link SampleImageSet}) and returns the
 * {@link PreprocessResult}, which in turn can be used to process the sample
 * set. By preprocess we imply:
 * <ol>
 * <li>Loading each {@link ICapturedImageSet}</li>
 * <li>Segmenting each captured set using {@link ICapturedImageSetSegmenter} to
 * its corresponding channels (one or several), and creating a
 * {@link IPolarizationImageSet} for each channel.</li>
 * <li>Registering each channel using an {@link IChannelRegistrator}</li>
 * <li>Calculates the dark background for each channel</li>
 * <li>Create overlayed realigned images (for each channel), which can be used
 * to investigate the subjective quality of registration.</li>
 * <ol/>
 */
public class Preprocessor {
    private final ArrayList<ICapturedImageFileSet> _capturedFileSets;
    private final ArrayList<ICapturedImageSetSegmenter> _segmenters;
    private final ArrayList<ICapturedImageSetReader> _readers;
    private final IChannelRegistrator[] _registrators;
    private final IRegistrationCompositeFiguresWriter[] _compositeWriters;
    private final IChannelDarkBackgroundEstimator[] _darkBackgroundEstimator;
    private final int _numChannels;

    private final File _root4PProject;
    private Color _compositeBaseImageColor = null;
    private Color _compositeImageToRegisterColor = null;

    /**
     * Create a processor for processing the following set. It's checked that the
     * given acquisition set does not have multiple images for a given channel.
     * Otherwise a CannotCreateException exception is thrown.
     * 
     * @param acquisitionSet
     * @throws CannotCreateException in case the acquisition set has multiple images
     *                               for a given channel.
     */
    public Preprocessor(AcquisitionSet acquisitionSet, int numChannels) throws CannotCreateException {
        Objects.requireNonNull(acquisitionSet, "acquisitionSet can't be null");
        ChannelUtils.checkNumChannelsNonZero(numChannels);

        this._capturedFileSets = this._setCapturedImages(acquisitionSet);
        this._segmenters = new ArrayList<>();
        this._readers = new ArrayList<>();
        this._numChannels = numChannels;
        this._registrators = new IChannelRegistrator[numChannels];
        this._darkBackgroundEstimator = new IChannelDarkBackgroundEstimator[numChannels];
        this._compositeWriters = new IRegistrationCompositeFiguresWriter[numChannels];
        this._root4PProject = acquisitionSet.rootFolder();
    }

    private ArrayList<ICapturedImageFileSet> _setCapturedImages(AcquisitionSet acquisitionSet)
            throws CannotCreateException {
        ArrayList<ICapturedImageFileSet> capturedFileSets = new ArrayList<>();
        for (Iterator<ICapturedImageFileSet> fileSetIterator = acquisitionSet.getIterator(); fileSetIterator
                .hasNext();) {
            ICapturedImageFileSet fileSet = fileSetIterator.next();
            // TODO how to write this check properly.
            // this._checkSingleImageForEachChannel(fileSet);

            capturedFileSets.add(fileSet);
        }
        return capturedFileSets;
    }

    private void _checkSingleImageForEachChannel(ICapturedImageFileSet fileSet) throws CannotCreateException {
        for (ICapturedImageFileSet capturedImageFileSet : _capturedFileSets) {
            if (Arrays.equals(capturedImageFileSet.getChannels(), fileSet.getChannels())) {
                throw new CannotCreateException(
                        "Can't create preprocessor because duplicate image is given for channel "
                                + fileSet.getChannels()[0]);
            }
        }
    }

    /**
     * Set the channel registrator used for registering the images of each channel.
     */
    public void setRegistrator(IChannelRegistrator registrator) {
        Objects.requireNonNull(registrator);

        // TODO Create a copy, so as to be used for multi-thread if needed.
        for (int i = 0; i < _registrators.length; i++) {
            this._registrators[i] = registrator;
        }
    }

    /**
     * Set the segmenter used for segmentation of each captured image set.
     */
    public void setSegmenter(ICapturedImageSetSegmenter segmenter) {
        Objects.requireNonNull(segmenter);

        // TODO Create a copy, so as to be used for multi-thread if needed.
        for (int i = 0; i < this._capturedFileSets.size(); i++) {
            this._segmenters.add(segmenter);
        }
    }

    /**
     * Set the reader for each captured image set.
     */
    public void setCapturedImageSetReader(ICapturedImageSetReader reader) {
        Objects.requireNonNull(reader);

        // TODO Create a copy, so as to be used for multi-thread if needed.
        for (int i = 0; i < this._capturedFileSets.size(); i++) {
            this._readers.add(reader);
        }

    }

    public void setDarkBackgroundCalculator(IChannelDarkBackgroundEstimator backgroundEstimator) {
        Objects.requireNonNull(backgroundEstimator);

        // TODO Create a copy, so as to be used for multi-thread if needed.
        for (int i = 0; i < this._capturedFileSets.size(); i++) {
            this._darkBackgroundEstimator[i] = backgroundEstimator;
        }
    }

    public void setRegistratonCompositeWriter(IRegistrationCompositeFiguresWriter compositeWriter) {
        Objects.requireNonNull(compositeWriter);

        // TODO Create a copy, so as to be used for multi-thread if needed.
        for (int i = 0; i < this._capturedFileSets.size(); i++) {
            this._compositeWriters[i] = compositeWriter;
        }
    }

    public void setRegistrationCompositeColor(Color baseImage, Color imageToRegister) {
        this._compositeBaseImageColor = baseImage;
        this._compositeImageToRegisterColor = imageToRegister;
    }

    public PreprocessResult process() throws IOException {
        // TODO check every parameter is set before process
        // TODO use multi-threading here.
        PreprocessResult preprocessResult = new PreprocessResult(this._numChannels);

        for (int cSet_ctr = 0; cSet_ctr < this._capturedFileSets.size(); cSet_ctr++) {
            ICapturedImageFileSet fileSet = this._capturedFileSets.get(0);

            ICapturedImageSet capturedImageSet = this._readers.get(0).read(fileSet);

            ICapturedImageSetSegmenter cSetSegmenter = this._segmenters.get(0);
            cSetSegmenter.setCapturedImage(capturedImageSet);
            for (int channel : fileSet.getChannels()) {
                IPolarizationImageSet polarizationImageSet = cSetSegmenter.segment(channel);
                IChannelRegistrationResult registrationResult = this._registrators[channel - 1]
                        .register(polarizationImageSet);
                preprocessResult.setRegistrationResult(channel, registrationResult);

                IChannelDarkBackground darkBackground = this._darkBackgroundEstimator[channel - 1]
                        .estimate(polarizationImageSet);
                preprocessResult.setDarkBackground(channel, darkBackground);

                this._realignChannel(polarizationImageSet, registrationResult);
                IPolarizationImageSetComposites compositeFigures = this
                        ._createChannelCompositeImages(polarizationImageSet);
                this._writeChannelCompositeImages(compositeFigures);
            }
        }

        this._closeIOResources();
        return preprocessResult;
    }

    private void _realignChannel(IPolarizationImageSet polImageSet, IChannelRegistrationResult registrationResult){
        IChannelRealigner channelRealigner = ChannelRealigner.create(registrationResult);
        channelRealigner.realign(polImageSet);
    }

    private IPolarizationImageSetComposites _createChannelCompositeImages(IPolarizationImageSet polImageSet) {
        PolarizationImageSetComposites compositeFigures = new PolarizationImageSetComposites(polImageSet.channel());

        RegistrationCompositeFigureCreator creator = new RegistrationCompositeFigureCreator(compositeFigures,
                this._compositeBaseImageColor, this._compositeImageToRegisterColor);

        for (RegistrationRule rule : RegistrationRule.values()) {
            Polarization polBaseImage = rule.getBaseImagePolarization();
            Polarization polToRegisterImage = rule.getToRegisterImagePolarization();
            creator.createComposite(polImageSet.getPolarizationImage(polBaseImage).getImage(),
                    polImageSet.getPolarizationImage(polToRegisterImage).getImage(), rule);
        }

        return compositeFigures;

    }

    private void _writeChannelCompositeImages(IPolarizationImageSetComposites compositeFigures) throws IOException {
        IRegistrationCompositeFiguresWriter writer = this._compositeWriters[compositeFigures.channel() - 1];

        writer.write(this._root4PProject, compositeFigures);
    }

    private void _closeIOResources() throws IOException {
        this._closeCapturedImageReaderResources();
        this._closeCompositeWriterResources();
    }

    /**
     * Close any resources associated with the readers.
     * 
     * @throws IOException
     */
    private void _closeCapturedImageReaderResources() throws IOException {
        for (ICapturedImageSetReader reader : this._readers) {
            reader.close();
        }

    }

    private void _closeCompositeWriterResources() throws IOException {
        for (IRegistrationCompositeFiguresWriter writer : this._compositeWriters) {
            writer.close();
        }

    }
}