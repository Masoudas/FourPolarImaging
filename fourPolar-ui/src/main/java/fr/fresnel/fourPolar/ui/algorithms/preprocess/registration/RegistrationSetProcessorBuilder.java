package fr.fresnel.fourPolar.ui.algorithms.preprocess.registration;

import java.util.Objects;

import fr.fresnel.fourPolar.algorithm.preprocess.darkBackground.estimator.IChannelDarkBackgroundEstimator;
import fr.fresnel.fourPolar.algorithm.preprocess.darkBackground.estimator.percentile.PercentileChannelDarkBackgroundEstimator;
import fr.fresnel.fourPolar.algorithm.preprocess.registration.IChannelRegistrator;
import fr.fresnel.fourPolar.algorithm.preprocess.registration.descriptorBased.DescriptorBasedRegistration;
import fr.fresnel.fourPolar.algorithm.preprocess.segmentation.ICapturedImageSetSegmenter;
import fr.fresnel.fourPolar.algorithm.preprocess.segmentation.RegistrationCapturedImageSetSegmenter;
import fr.fresnel.fourPolar.algorithm.util.image.color.GrayScaleToColorConverter.Color;
import fr.fresnel.fourPolar.algorithm.visualization.figures.polarization.IPolarizationImageSetCompositesCreater;
import fr.fresnel.fourPolar.algorithm.visualization.figures.polarization.RegistrationCompositeFigureCreator;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.imageSet.acquisition.registration.RegistrationImageSet;
import fr.fresnel.fourPolar.core.imagingSetup.IFourPolarImagingSetup;
import fr.fresnel.fourPolar.io.image.captured.ICapturedImageSetReader;
import fr.fresnel.fourPolar.io.image.captured.tiff.TiffCapturedImageSetReader;

/**
 * Processes a given {@link RegistrationImageSet} and
 */
public class RegistrationSetProcessorBuilder extends IRegistrationSetProcessorBuilder {
    private final int _numChannels;

    private ICapturedImageSetSegmenter _segmenter;
    private IChannelRegistrator _registrator;
    private IChannelDarkBackgroundEstimator _darkBackgroundEstimator;
    private IPolarizationImageSetCompositesCreater _compositeImageCreator;

    private ICapturedImageSetReader _registrationImageReader;

    /**
     * Start the build process. Note that using this constructor, all processor
     * parameters are set to defaults. Moreover, the composite images would have Red
     * for base and Green for to register image color. With this constructor, all
     * image instances will have ImgLib2 implementation.
     * 
     * @param imagingSetup is the imaging setup.
     */
    public RegistrationSetProcessorBuilder(IFourPolarImagingSetup imagingSetup) {
        Objects.requireNonNull(imagingSetup);

        this._numChannels = imagingSetup.getNumChannel();

        this._segmenter = new RegistrationCapturedImageSetSegmenter(imagingSetup.getFieldOfView(),
                imagingSetup.getCameras(), this._numChannels);
        this._registrator = new DescriptorBasedRegistration();
        this._darkBackgroundEstimator = new PercentileChannelDarkBackgroundEstimator(imagingSetup.getCameras());
        this._compositeImageCreator = new RegistrationCompositeFigureCreator(this._numChannels, Color.Red, Color.Green);

        this._registrationImageReader = new TiffCapturedImageSetReader(new ImgLib2ImageFactory());
    }

    /**
     * Set the channel registrator used for registering the images of each channel.
     */
    public RegistrationSetProcessorBuilder registrator(IChannelRegistrator registrator) {
        Objects.requireNonNull(registrator);

        this._registrator = registrator;
        return this;
    }

    /**
     * Set the reader for registration images, which are read as captured images.
     */
    public RegistrationSetProcessorBuilder registrationImageSetReader(ICapturedImageSetReader reader) {
        Objects.requireNonNull(reader);

        this._registrationImageReader = reader;
        return this;
    }

    /**
     * Set dark background estimator.
     */
    public RegistrationSetProcessorBuilder darkBackgroundEstimator(
            IChannelDarkBackgroundEstimator backgroundEstimator) {
        Objects.requireNonNull(backgroundEstimator);

        this._darkBackgroundEstimator = backgroundEstimator;
        return this;
    }

    /**
     * Set registration composite creator.
     */
    public RegistrationSetProcessorBuilder registrationCompositeCreator(
            IPolarizationImageSetCompositesCreater compositeCreator) {
        this._compositeImageCreator = compositeCreator;

        return this;
    }

    public IRegistrationSetProcessor build() {
        return new RegistrationSetProcessor(this);
    }

    @Override
    IChannelDarkBackgroundEstimator getDarkBackgroundEstimator() {
        return this._darkBackgroundEstimator;
    }

    @Override
    ICapturedImageSetReader getRegistrationImageReader() {
        return this._registrationImageReader;
    }

    @Override
    IChannelRegistrator getRegistrator() {
        return this._registrator;
    }

    @Override
    ICapturedImageSetSegmenter getSegmenter() {
        return this._segmenter;
    }

    @Override
    IPolarizationImageSetCompositesCreater getCompositeImageCreator() {
        return this._compositeImageCreator;
    }

    @Override
    int getNumChannels() {
        return this._numChannels;
    }

}