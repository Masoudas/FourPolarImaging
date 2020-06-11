package fr.fresnel.fourPolar.ui.algorithms.preprocess.registrationSet;

import java.io.IOException;
import java.util.Optional;

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
import fr.fresnel.fourPolar.core.preprocess.registration.ChannelRegistrationResultUtils;
import fr.fresnel.fourPolar.core.preprocess.registration.IChannelRegistrationResult;
import fr.fresnel.fourPolar.core.preprocess.registration.RegistrationRule;
import fr.fresnel.fourPolar.core.visualization.figures.polarization.IPolarizationImageSetComposites;
import fr.fresnel.fourPolar.io.exceptions.image.captured.CapturedImageReadFailure;
import fr.fresnel.fourPolar.io.image.captured.ICapturedImageSetReader;
import fr.fresnel.fourPolar.ui.exceptions.algorithms.preprocess.registrationSet.IOIssueRegistrationSetProcessFailure;
import fr.fresnel.fourPolar.ui.exceptions.algorithms.preprocess.registrationSet.RegistrationIssueRegistrationSetProcessFailure;
import fr.fresnel.fourPolar.ui.exceptions.algorithms.preprocess.registrationSet.RegistrationSetProcessFailure;
import javassist.tools.reflect.CannotCreateException;

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
    public RegistrationSetProcessResult process(RegistrationImageSet registrationImageSet)
            throws RegistrationSetProcessFailure {
        // TODO use multi-threading here.
        ICapturedImageSet registrationImage = this._readRegistrationImage(registrationImageSet.getIterator().next());

        IPolarizationImageSet[] channelImages = this._segmentRegistrationImageIntoChannels(registrationImage);

        RegistrationSetProcessResult preprocessResult = _processChannels(channelImages);

        this._channelComposites = this._createCompositeImages(registrationImageSet, channelImages, preprocessResult);

        return preprocessResult;
    }

    private IPolarizationImageSetComposites[] _createCompositeImages(RegistrationImageSet registrationImageSet,
            IPolarizationImageSet[] channelImages, RegistrationSetProcessResult preprocessResult) {
        IPolarizationImageSetComposites[] composites = new IPolarizationImageSetComposites[this._numChannels];

        for (int channel = 1; channel <= this._numChannels; channel++) {
            this._compositeImageCreator.create(channelImages[channel - 1]);
        }

        return composites;

    }

    private RegistrationSetProcessResult _processChannels(IPolarizationImageSet[] channelImages)
            throws RegistrationIssueRegistrationSetProcessFailure {
        RegistrationSetProcessResult preprocessResult = new RegistrationSetProcessResult(this._numChannels);
        this._registerChannels(channelImages, preprocessResult);
        this._checkRegistrationIsSuccessful(preprocessResult);
        this._estimateChannelsDarkBackground(channelImages, preprocessResult);

        // Even though realignment is not part of process, we perform it here, so that
        // images are realigned for possible future uses, like creating composites.
        this._realignPolarizationImageOfChannels(channelImages, preprocessResult);

        return preprocessResult;
    }

    private ICapturedImageSet _readRegistrationImage(ICapturedImageFileSet fileSet)
            throws RegistrationSetProcessFailure {
        ICapturedImageSet imageSet;
        try {
            imageSet = this._registrationImageReader.read(fileSet);
        } catch (CapturedImageReadFailure e) {
            throw new IOIssueRegistrationSetProcessFailure(e.getMessage());
        }
        this._closeCapturedImageReaderResources();

        return imageSet;
    }

    /**
     * Returns polarization image of each channel as an array.
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
     * Checks whether registration was successful for all {@link RegistrationRule}s
     * of every channel. The exception thrown contains all registration rules for
     * which failure has occured.
     */
    private void _checkRegistrationIsSuccessful(RegistrationSetProcessResult preprocessResult)
            throws RegistrationIssueRegistrationSetProcessFailure {
        RegistrationIssueRegistrationSetProcessFailure failure = new RegistrationIssueRegistrationSetProcessFailure();
        for (int channel = 1; channel <= this._numChannels; channel++) {
            this._assignFailedRulesToException(failure, preprocessResult.getRegistrationResult(channel));
        }

        if (failure.hasFailure()) {
            throw failure;
        }
    }

    private void _assignFailedRulesToException(RegistrationIssueRegistrationSetProcessFailure failure,
            IChannelRegistrationResult result) {
        RegistrationRule[] failedRules = this._getChannelFailedRegistrationRules(result);
        for (RegistrationRule rule : failedRules) {
            failure.setRuleFailure(rule, result.channel());
        }
    }

    private RegistrationRule[] _getChannelFailedRegistrationRules(IChannelRegistrationResult result) {
        return ChannelRegistrationResultUtils.getFailedRegistrations(result);
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
            IChannelRealigner channelRealigner = _createChannelRealigner(preprocessResult, channel);
            channelRealigner.realign(channelImages[channel - 1]);
        }
    }

    private IChannelRealigner _createChannelRealigner(RegistrationSetProcessResult preprocessResult, int channel) {
        IChannelRegistrationResult channelResult = preprocessResult.getRegistrationResult(channel);
        IChannelRealigner channelRealigner = null;
        try {
            channelRealigner = ChannelRealigner.create(channelResult);

        } catch (CannotCreateException e) {
            // This exception is never caught, because if registration has failed before, we
            // throw a
            // RegistrationIssueRegistrationSetProcessFailure.
        }
        return channelRealigner;
    }

    /**
     * Close any resources associated with the readers.
     * 
     * @throws IOException
     */
    private void _closeCapturedImageReaderResources() throws RegistrationSetProcessFailure {
        try {
            this._registrationImageReader.close();
        } catch (IOException e) {
            throw new IOIssueRegistrationSetProcessFailure(e.getMessage());
        }
    }

    @Override
    public Optional<IPolarizationImageSetComposites> getRegistrationComposite(int channel) {
        ChannelUtils.checkChannel(channel, this._numChannels);
        return Optional.ofNullable(this._channelComposites[channel - 1]);
    }

}