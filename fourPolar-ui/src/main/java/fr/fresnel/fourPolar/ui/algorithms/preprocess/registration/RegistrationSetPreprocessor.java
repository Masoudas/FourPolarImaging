package fr.fresnel.fourPolar.ui.algorithms.preprocess.registration;

import java.io.IOException;

import fr.fresnel.fourPolar.algorithm.preprocess.darkBackground.estimator.IChannelDarkBackgroundEstimator;
import fr.fresnel.fourPolar.algorithm.preprocess.realignment.ChannelRealigner;
import fr.fresnel.fourPolar.algorithm.preprocess.realignment.IChannelRealigner;
import fr.fresnel.fourPolar.algorithm.preprocess.registration.IChannelRegistrator;
import fr.fresnel.fourPolar.algorithm.preprocess.segmentation.ICapturedImageSetSegmenter;
import fr.fresnel.fourPolar.algorithm.visualization.figures.polarization.IPolarizationImageSetCompositesCreater;
import fr.fresnel.fourPolar.core.image.captured.ICapturedImageSet;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.imageSet.acquisition.registration.RegistrationImageSet;
import fr.fresnel.fourPolar.core.physics.channel.ChannelUtils;
import fr.fresnel.fourPolar.core.preprocess.RegistrationSetProcessResult;
import fr.fresnel.fourPolar.core.preprocess.darkBackground.IChannelDarkBackground;
import fr.fresnel.fourPolar.core.preprocess.registration.IChannelRegistrationResult;
import fr.fresnel.fourPolar.core.visualization.figures.polarization.IPolarizationImageSetComposites;
import fr.fresnel.fourPolar.io.image.captured.ICapturedImageSetReader;

class RegistrationSetProcessor implements IRegistrationSetProcessor {
    private final int _numChannels;

    private final ICapturedImageSetReader _registrationImageReader;

    private final IChannelRegistrator _registrator;
    private final ICapturedImageSetSegmenter _segmenter;
    private final IChannelDarkBackgroundEstimator _darkBackgroundEstimator;
    private final IPolarizationImageSetCompositesCreater _compositeImageCreator;

    private IPolarizationImageSetComposites[] _channelComposites;

    public RegistrationSetProcessor(IRegistrationSetProcessorBuilder builder) {
        this._numChannels = builder.getNumChannels();

        this._registrationImageReader = builder.getRegistrationImageReader();

        this._segmenter = builder.getSegmenter();
        this._registrator = builder.getRegistrator();
        this._darkBackgroundEstimator = builder.getDarkBackgroundEstimator();
        this._compositeImageCreator = builder.getCompositeImageCreator();

        this._channelComposites = null;
    }

    /**
     * Process the given registration captured image set.
     */
    @Override
    public RegistrationSetProcessResult process(RegistrationImageSet registrationImageSet) throws IOException {
        // TODO use multi-threading here.
        ICapturedImageSet registrationImage = this._readRegistrationImage(registrationImageSet.getIterator().next());

        IPolarizationImageSet[] channelImages = this._segmentRegistrationImageIntoChannels(registrationImage);

        RegistrationSetProcessResult preprocessResult = _processChannels(channelImages);

        this._channelComposites = this._createCompositeImages(registrationImageSet, channelImages, preprocessResult);

        return preprocessResult;
    }

    private IPolarizationImageSetComposites[] _createCompositeImages(RegistrationImageSet registrationImageSet,
            IPolarizationImageSet[] channelImages, RegistrationSetProcessResult preprocessResult) throws IOException {
        this._realignPolarizationImageOfChannels(channelImages, preprocessResult);

        IPolarizationImageSetComposites[] composites = new IPolarizationImageSetComposites[this._numChannels];

        for (int channel = 1; channel <= this._numChannels; channel++) {
            this._compositeImageCreator.create(channelImages[channel - 1]);
        }

        return composites;

    }

    private RegistrationSetProcessResult _processChannels(IPolarizationImageSet[] channelImages) {
        RegistrationSetProcessResult preprocessResult = new RegistrationSetProcessResult(this._numChannels);
        this._registerChannels(channelImages, preprocessResult);
        this._estimateChannelsDarkBackground(channelImages, preprocessResult);
        return preprocessResult;
    }

    private ICapturedImageSet _readRegistrationImage(ICapturedImageFileSet fileSet) throws IOException {
        ICapturedImageSet imageSet = this._registrationImageReader.read(fileSet);
        this._closeCapturedImageReaderResources();

        return imageSet;
    }

    /**
     * Returns polarization image of each channel as an array.
     * memory.
     */
    private IPolarizationImageSet[] _segmentRegistrationImageIntoChannels(ICapturedImageSet registrationImageSet) {
        IPolarizationImageSet[] channelImages = new IPolarizationImageSet[this._numChannels];

        this._segmenter.setCapturedImage(registrationImageSet);
        for (int channel = 1; channel <= this._numChannels; channel++) {
            channelImages[channel - 1] = this._segmenter.segment(channel);
        }

        return channelImages;
    }

    /**
     * Register each channel of the registration images.
     */
    private void _registerChannels(IPolarizationImageSet[] channelImages,
            RegistrationSetProcessResult preprocessResult) {
        for (int channel = 1; channel <= this._numChannels; channel++) {
            IChannelRegistrationResult registrationResult = this._registrator.register(channelImages[channel - 1]);
            preprocessResult.setRegistrationResult(channel, registrationResult);
        }

    }

    /**
     * Estimate dark background for each channel.
     */
    private void _estimateChannelsDarkBackground(IPolarizationImageSet[] channelImages,
            RegistrationSetProcessResult preprocessResult) {
        for (int channel = 1; channel <= this._numChannels; channel++) {
            IChannelDarkBackground darkBackground = this._darkBackgroundEstimator.estimate(channelImages[channel - 1]);
            preprocessResult.setDarkBackground(channel, darkBackground);
        }
    }

    /**
     * Realign the polarization image set equivalent of each channel registration
     * image set.
     * 
     * @param channelImages
     * @param preprocessResult
     */
    private void _realignPolarizationImageOfChannels(IPolarizationImageSet[] channelImages,
            RegistrationSetProcessResult preprocessResult) {
        for (int channel = 1; channel <= this._numChannels; channel++) {
            IChannelRegistrationResult channelResult = preprocessResult.getRegistrationResult(channel);
            IChannelRealigner channelRealigner = ChannelRealigner.create(channelResult);
            channelRealigner.realign(channelImages[channel - 1]);
        }
    }

    /**
     * Close any resources associated with the readers.
     * 
     * @throws IOException
     */
    private void _closeCapturedImageReaderResources() throws IOException {
        this._registrationImageReader.close();
    }

    @Override
    public IPolarizationImageSetComposites getRegistrationComposite(int channel) {
        ChannelUtils.checkChannel(channel, this._numChannels);
        return this._channelComposites[channel - 1];
    }

}