package fr.fresnel.fourPolar.ui.algorithms.preprocess;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

import fr.fresnel.fourPolar.algorithm.preprocess.darkBackground.IChannelDarkBackgroundRemover;
import fr.fresnel.fourPolar.algorithm.preprocess.realignment.IChannelRealigner;
import fr.fresnel.fourPolar.algorithm.preprocess.segmentation.ICapturedImageSetSegmenter;
import fr.fresnel.fourPolar.algorithm.preprocess.soi.ISoICalculator;
import fr.fresnel.fourPolar.core.image.captured.ICapturedImageSet;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.image.soi.ISoIImage;
import fr.fresnel.fourPolar.core.image.soi.SoIImage;
import fr.fresnel.fourPolar.core.imageSet.acquisition.sample.SampleImageSet;
import fr.fresnel.fourPolar.io.image.captured.ICapturedImageSetReader;
import fr.fresnel.fourPolar.io.image.polarization.IPolarizationImageSetWriter;
import fr.fresnel.fourPolar.io.image.soi.ISoIImageWriter;

/**
 * Using this class, we can preprocess the given {@link SampleImageSet} to
 * generate a {@link IPolarizationImageSet} for each
 * {@link ICapturedImageFileSet} inside this sample set, using an instance of
 * {@link PreprocessResult}. For this end, this processor does the following:
 * <ol>
 * <li>Loads each captured image set using the user supported
 * {@link ICapturedImageSetReader}</li>
 * <li>Segments each captured set using a user provided
 * {@link ICapturedImageSetSegmenter} to its contained channels (one or
 * several), and creates a {@link IPolarizationImageSet} for each channel.</li>
 * <li>Realigns each channel using a user provided
 * {@link IChannelRealigner}</li>
 * <li>Removes the dark background for each channel using a user provided
 * {@link IChannelDarkBackgroundRemover}</li>
 * <li>Creates the {@link ISoIImage} for each polarization image set</li>
 * <li>Writes each polarization image set to the disk, using the write interface
 * provided by the user</li>
 * <li>Writes each SoI image to the disk</li>
 * <ol/>
 */
public class SampleImageSetPreprocessor {
    private final SampleImageSet _sampleImageSet;

    private ICapturedImageSetSegmenter _segmenter;
    private ICapturedImageSetReader _capturedImageSetReader;

    private final IChannelDarkBackgroundRemover[] _backgroundRemovers;
    private final IChannelRealigner[] _realigners;
    private final IPolarizationImageSetWriter[] _polarizationWriters;
    private final ISoIImageWriter[] _soiImageWriters;
    private final ISoICalculator[] _soiCalculators;

    private final int _numChannels;

    public SampleImageSetPreprocessor(SampleImageSet sampleImageSet, int numChannels) {
        // TODO directly pass the result of this class to the fourPolar processor, we
        // need to Implement an observer between the two probably.
        this._sampleImageSet = sampleImageSet;

        this._realigners = new IChannelRealigner[numChannels];
        this._backgroundRemovers = new IChannelDarkBackgroundRemover[numChannels];

        this._polarizationWriters = new IPolarizationImageSetWriter[numChannels];
        this._soiImageWriters = new ISoIImageWriter[numChannels];
        this._soiCalculators = new ISoICalculator[numChannels];

        this._numChannels = numChannels;
    }

    /**
     * Set the reader of captured image.
     */
    public void setCapturedImageSetLoader(ICapturedImageSetReader reader) {
        Objects.requireNonNull(reader);

        this._capturedImageSetReader = reader;
    }

    /**
     * Set the polarization image set writer.
     */
    public void setPolarizationImageSetWriter(IPolarizationImageSetWriter writer) {
        Objects.requireNonNull(writer);

        // TODO Create a copy, so as to be used for multi-thread if needed.
        for (int channel = 0; channel < this._numChannels; channel++) {
            this._polarizationWriters[channel] = writer;
        }

    }

    /**
     * Set the channel realigner
     */
    public void setChannelRealigner(int channel, IChannelRealigner realigner) {
        Objects.requireNonNull(realigner);
        this._realigners[channel] = realigner;
    }

    /**
     * Set channel dark back ground remover.
     */
    public void setChannelDarkBackgroundRemover(int channel, IChannelDarkBackgroundRemover darkBackgroundRemover) {
        Objects.requireNonNull(darkBackgroundRemover);
        this._backgroundRemovers[channel] = darkBackgroundRemover;

    }

    /**
     * Set writer for soi Image.
     */
    public void setSoIImageWriter(ISoIImageWriter soiImageWriter) {
        Objects.requireNonNull(soiImageWriter);

        // TODO Create a copy, so as to be used for multi-thread if needed.
        for (int channel = 0; channel < this._numChannels; channel++) {
            this._soiImageWriters[channel] = soiImageWriter;
        }

    }

    /**
     * Set soi calculator.
     */
    public void setSoICalculator(ISoICalculator calculator) {
        Objects.requireNonNull(calculator);

        // TODO Create a copy, so as to be used for multi-thread if needed.
        for (int channel = 0; channel < this._numChannels; channel++) {
            this._soiCalculators[channel] = calculator;
        }

    }

    /**
     * Process each captured image set.
     * 
     * @throws IOException is thrown in case there's a problem reading a captured
     *                     image set, or writing a polarization image set. This
     *                     exception should not happen, basically because we check
     *                     existence of each captured image using
     *                     {@ICapturedImageChecker} when creating sample set.
     */
    public void process(File projectRootFolder) throws IOException {
        for (Iterator<ICapturedImageFileSet> fileSetItr = this._sampleImageSet.getIterator(); fileSetItr.hasNext();) {
            ICapturedImageFileSet fileSet = fileSetItr.next();

            ICapturedImageSet capturedImageSet = this._capturedImageSetReader.read(fileSet);
            IPolarizationImageSet[] channelsPolImageSet = this._createChannelPolarizationImages(capturedImageSet);

            this._realignChannelPolarizationImages(channelsPolImageSet);
            this._removeChannelDarkBackgrounds(channelsPolImageSet);

            ISoIImage[] channelsSoIImage = this._createChannelSoIImages(channelsPolImageSet);
            this._calculateChannelSoI(channelsPolImageSet, channelsSoIImage);

            this._writeChannelPolarizationImages(channelsPolImageSet);
            this._writeChannelSoIImages(channelsSoIImage);
        }

        this._closeReaderWriterResources();
    }

    /**
     * Note that multi-threading here is unclear. This is because all captured
     * images are processed together here.
     */
    private IPolarizationImageSet[] _createChannelPolarizationImages(ICapturedImageSet capturedImageSet) {
        this._segmenter.setCapturedImage(capturedImageSet);
        IPolarizationImageSet[] channelsPolImageSet = new IPolarizationImageSet[this._numChannels];
        for (int channel = 1; channel <= this._numChannels; channel++) {
            channelsPolImageSet[channel - 1] = this._segmenter.segment(channel);
        }

        return channelsPolImageSet;
    }

    private void _realignChannelPolarizationImages(IPolarizationImageSet[] channelsPolImageSet) {
        for (int channel = 1; channel <= this._numChannels; channel++) {
            this._realigners[channel - 1].realign(channelsPolImageSet[channel - 1]);
        }
    }

    private void _removeChannelDarkBackgrounds(IPolarizationImageSet[] channelsPolImageSet) {
        for (int channel = 1; channel <= this._numChannels; channel++) {
            this._backgroundRemovers[channel - 1].remove(channelsPolImageSet[channel - 1]);
        }
    }

    private ISoIImage[] _createChannelSoIImages(IPolarizationImageSet[] channelsPolImageSet) {
        ISoIImage[] channelsSoIImage = new ISoIImage[this._numChannels];
        for (int channel = 1; channel <= this._numChannels; channel++) {
            SoIImage.create(channelsPolImageSet[channel - 1]);
        }

        return channelsSoIImage;
    }

    private void _calculateChannelSoI(IPolarizationImageSet[] channelsPolImageSet, ISoIImage[] channelsSoIImage) {
        for (int channel = 1; channel <= this._numChannels; channel++) {
            this._soiCalculators[channel - 1].calculateUINT16Sum(channelsPolImageSet[channel - 1].getIterator(),
                    channelsSoIImage[channel - 1].getImage().getCursor());
        }
    }

    private void _writeChannelPolarizationImages(IPolarizationImageSet[] channelsPolImageSet) throws IOException {
        for (int channel = 1; channel <= this._numChannels; channel++) {
            this._polarizationWriters[channel - 1].write(this._sampleImageSet.rootFolder(),
                    channelsPolImageSet[channel]);
        }
    }

    private void _writeChannelSoIImages(ISoIImage[] channelsSoIImage) throws IOException {
        for (int channel = 1; channel <= this._numChannels; channel++) {
            this._soiImageWriters[channel - 1].write(this._sampleImageSet.rootFolder(), channelsSoIImage[channel]);
        }
    }

    private void _closeReaderWriterResources() throws IOException {
        this._closePolarizationWriterResources();
        this._closeCapturedImageReaderResources();
        this._closeSoIWriterResources();
    }

    private void _closeCapturedImageReaderResources() throws IOException {
        this._capturedImageSetReader.close();
    }

    private void _closePolarizationWriterResources() throws IOException {
        for (IPolarizationImageSetWriter writer : this._polarizationWriters) {
            writer.close();
        }
    }

    private void _closeSoIWriterResources() throws IOException {
        for (ISoIImageWriter writer : this._soiImageWriters) {
            writer.close();
        }
    }

}