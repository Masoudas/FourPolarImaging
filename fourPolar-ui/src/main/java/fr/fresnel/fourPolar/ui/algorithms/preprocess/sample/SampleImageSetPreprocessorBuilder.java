package fr.fresnel.fourPolar.ui.algorithms.preprocess.sample;

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

public class SampleImageSetPreprocessorBuilder extends ISampleImageSetPreprocessorBuilder {
    private ICapturedImageSetReader _capturedImageSetReader;

    private final ICapturedImageSetSegmenter _segmenter;
    private final IChannelDarkBackgroundRemover[] _backgroundRemovers;
    private final IChannelRealigner[] _realigners;

    private final int _numChannels;

    /**
     * Initialize the builder, setting all parameters to defaults. This includes
     * reading captured images in tiff, using ImgLib2 image model,
     * 
     * @param imagingSetup
     * @param registrationprocessresult
     */
    public SampleImageSetPreprocessorBuilder(IFourPolarImagingSetup imagingSetup,
            RegistrationSetProcessResult registrationprocessresult) {
        this._numChannels = imagingSetup.getNumChannel();

        this._segmenter = this._setCapturedImageSetSegmenter(imagingSetup);
        this._realigners = this._setChannelRealigners(this._numChannels, registrationprocessresult);
        this._backgroundRemovers = this._setChannelDarkBackgroundRemover(this._numChannels, registrationprocessresult);

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
     */
    private IChannelRealigner[] _setChannelRealigners(int numChannels,
            RegistrationSetProcessResult registrationprocessresult) {
        IChannelRealigner[] realigners = new IChannelRealigner[numChannels];
        for (int channel = 1; channel <= this._numChannels; channel++) {
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

        for (int channel = 1; channel <= this._numChannels; channel++) {
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
        ChannelUtils.checkChannel(channel, _numChannels);
        return this._backgroundRemovers[channel - 1];
    }

    @Override
    ICapturedImageSetReader getCapturedImageSetReader() {
        return this._capturedImageSetReader;
    }

    @Override
    int getNumChannels() {
        return this._numChannels;
    }

    @Override
    IChannelRealigner getRealigners(int channel) {
        ChannelUtils.checkChannel(channel, _numChannels);
        return this._realigners[channel - 1];
    }

    @Override
    ICapturedImageSetSegmenter getSegmenter() {
        return this._segmenter;
    }

}