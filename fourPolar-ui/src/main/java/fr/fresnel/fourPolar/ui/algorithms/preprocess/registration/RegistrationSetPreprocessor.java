package fr.fresnel.fourPolar.ui.algorithms.preprocess.registration;

import java.io.IOException;

import fr.fresnel.fourPolar.algorithm.preprocess.darkBackground.IChannelDarkBackgroundRemover;
import fr.fresnel.fourPolar.algorithm.preprocess.darkBackground.estimator.IChannelDarkBackgroundEstimator;
import fr.fresnel.fourPolar.algorithm.preprocess.realignment.ChannelRealigner;
import fr.fresnel.fourPolar.algorithm.preprocess.realignment.IChannelRealigner;
import fr.fresnel.fourPolar.algorithm.preprocess.registration.IChannelRegistrator;
import fr.fresnel.fourPolar.algorithm.preprocess.segmentation.ICapturedImageSetSegmenter;
import fr.fresnel.fourPolar.algorithm.util.image.color.GrayScaleToColorConverter.Color;
import fr.fresnel.fourPolar.algorithm.visualization.figures.polarization.IPolarizationImageSetCompositesCreater;
import fr.fresnel.fourPolar.algorithm.visualization.figures.polarization.RegistrationCompositeFigureCreator;
import fr.fresnel.fourPolar.core.image.captured.ICapturedImageSet;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.image.soi.ISoIImage;
import fr.fresnel.fourPolar.core.imageSet.acquisition.registration.RegistrationImageSet;
import fr.fresnel.fourPolar.core.imageSet.acquisition.sample.SampleImageSet;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.preprocess.RegistrationSetProcessResult;
import fr.fresnel.fourPolar.core.preprocess.darkBackground.IChannelDarkBackground;
import fr.fresnel.fourPolar.core.preprocess.registration.IChannelRegistrationResult;
import fr.fresnel.fourPolar.core.preprocess.registration.RegistrationRule;
import fr.fresnel.fourPolar.core.visualization.figures.polarization.IPolarizationImageSetComposites;
import fr.fresnel.fourPolar.core.visualization.figures.polarization.PolarizationImageSetComposites;
import fr.fresnel.fourPolar.io.image.captured.ICapturedImageSetReader;
import fr.fresnel.fourPolar.io.visualization.figures.polarization.IPolarizationImageSetCompositesWriter;


class RegistrationSetProcessor implements IRegistrationSetProcessor {
    private final ICapturedImageSetReader _registrationImageReader;
    private final IPolarizationImageSetCompositesWriter _compositeWriter;

    private final IChannelRegistrator _registrator;
    private final ICapturedImageSetSegmenter _segmenter;
    private final IChannelDarkBackgroundEstimator _darkBackgroundEstimator;
    private final IPolarizationImageSetCompositesCreater _compositeImageCreator;

    public RegistrationSetProcessor(IRegistrationSetProcessorBuilder builder) {
        this._registrationImageReader = builder.getRegistrationImageReader();

        this._segmenter = builder.getSegmenter();
        this._registrator = builder.getRegistrator();
        this._darkBackgroundEstimator = builder.getDarkBackgroundEstimator();
        this._compositeImageCreator = builder.getCompositeImageCreator();

        this._compositeWriter = builder.getCompositeWriter();
    }

    /**
     * Process each captured image set.
     */
    @Override
    public RegistrationSetProcessResult process(RegistrationImageSet registrationImageSet) throws IOException {
        // TODO check every parameter is set before process
        // TODO use multi-threading here.
        RegistrationSetProcessResult preprocessResult = new RegistrationSetProcessResult(this._numChannels);

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

    private void _realignChannel(IPolarizationImageSet polImageSet, IChannelRegistrationResult registrationResult) {
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
        IPolarizationImageSetCompositesWriter writer = this._compositeWriters[compositeFigures.channel() - 1];

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
        for (IPolarizationImageSetCompositesWriter writer : this._compositeWriters) {
            writer.close();
        }

    }

}