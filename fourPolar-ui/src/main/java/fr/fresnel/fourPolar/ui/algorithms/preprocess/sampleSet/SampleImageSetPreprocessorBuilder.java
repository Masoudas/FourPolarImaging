package fr.fresnel.fourPolar.ui.algorithms.preprocess.sampleSet;

import java.util.Objects;

import fr.fresnel.fourPolar.algorithm.preprocess.darkBackground.ChannelDarkBackgroundRemover;
import fr.fresnel.fourPolar.algorithm.preprocess.darkBackground.IChannelDarkBackgroundRemover;
import fr.fresnel.fourPolar.algorithm.preprocess.realignment.ChannelRealigner;
import fr.fresnel.fourPolar.algorithm.preprocess.realignment.IChannelRealigner;
import fr.fresnel.fourPolar.algorithm.preprocess.segmentation.ICapturedImageSetSegmenter;
import fr.fresnel.fourPolar.algorithm.preprocess.segmentation.SampleCapturedImageSetSegmenter;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.imagingSetup.IFourPolarImagingSetup;
import fr.fresnel.fourPolar.core.physics.channel.ChannelUtils;
import fr.fresnel.fourPolar.core.preprocess.RegistrationSetProcessResult;
import fr.fresnel.fourPolar.io.image.captured.ICapturedImageSetReader;
import fr.fresnel.fourPolar.io.image.captured.tiff.TiffCapturedImageSetReader;
import javassist.tools.reflect.CannotCreateException;

public class SampleImageSetPreprocessorBuilder extends ISampleImageSetPreprocessorBuilder {
    private ICapturedImageSetReader _capturedImageSetReader;

    private final ICapturedImageSetSegmenter _segmenter;
    private final IChannelDarkBackgroundRemover[] _backgroundRemovers;
    private final IChannelRealigner[] _realigners;

    private final IFourPolarImagingSetup _imagingSetup;

    /**
     * Initialize the builder, setting all parameters to defaults. This includes
     * reading captured images in tiff, using ImgLib2 image model,
     * 
     * @param imagingSetup
     * @param registrationprocessresult
     */
    public SampleImageSetPreprocessorBuilder(IFourPolarImagingSetup imagingSetup,
            RegistrationSetProcessResult registrationProcessResult) {
        this._imagingSetup = imagingSetup;

        this._segmenter = this._setCapturedImageSetSegmenter(this._imagingSetup);
        this._realigners = this._setChannelRealigners(getNumChannels(), registrationProcessResult);
        this._backgroundRemovers = this._setChannelDarkBackgroundRemover(getNumChannels(), registrationProcessResult);

        this._capturedImageSetReader = new TiffCapturedImageSetReader(new ImgLib2ImageFactory());

    }

    private ICapturedImageSetSegmenter _setCapturedImageSetSegmenter(IFourPolarImagingSetup imagingSetup) {
        return new SampleCapturedImageSetSegmenter(imagingSetup.getFieldOfView(), imagingSetup.getCameras(),
                imagingSetup.getNumChannel());
    }

    /**
     * Set the reader of captured image.
     */
    public void setCapturedImageSetReader(ICapturedImageSetReader reader) {
        Objects.requireNonNull(reader);

        this._capturedImageSetReader = reader;
    }

    /**
     * Set channel realigners
     * 
     * @throws CannotCreateException
     */
    private IChannelRealigner[] _setChannelRealigners(int numChannels,
            RegistrationSetProcessResult registrationprocessresult) {
        IChannelRealigner[] realigners = new IChannelRealigner[numChannels];
        for (int channel = 1; channel <= getNumChannels(); channel++) {
            realigners[channel - 1] = ChannelRealigner.create(registrationprocessresult.getRegistrationResult(channel));
        }

        return realigners;
    }

    /**
     * Set channel dark back ground remover.
     */
    private IChannelDarkBackgroundRemover[] _setChannelDarkBackgroundRemover(int numChannels,
            RegistrationSetProcessResult registrationprocessresult) {
        IChannelDarkBackgroundRemover[] channelRemovers = new IChannelDarkBackgroundRemover[numChannels];

        for (int channel = 1; channel <= getNumChannels(); channel++) {
            channelRemovers[channel - 1] = ChannelDarkBackgroundRemover
                    .create(registrationprocessresult.getDarkBackground(channel));
        }

        return channelRemovers;
    }

    public ISampleImageSetPreprocessor build() {
        return new SampleImageSetPreprocessor(this);
    }

    @Override
    IChannelDarkBackgroundRemover getBackgroundRemovers(int channel) {
        ChannelUtils.checkChannel(channel, getNumChannels());
        return this._backgroundRemovers[channel - 1];
    }

    @Override
    ICapturedImageSetReader getCapturedImageSetReader() {
        return this._capturedImageSetReader;
    }

    @Override
    int getNumChannels() {
        return this._imagingSetup.getNumChannel();
    }

    @Override
    IChannelRealigner getRealigners(int channel) {
        ChannelUtils.checkChannel(channel, getNumChannels());
        return this._realigners[channel - 1];
    }

    @Override
    ICapturedImageSetSegmenter getSegmenter() {
        return this._segmenter;
    }

}