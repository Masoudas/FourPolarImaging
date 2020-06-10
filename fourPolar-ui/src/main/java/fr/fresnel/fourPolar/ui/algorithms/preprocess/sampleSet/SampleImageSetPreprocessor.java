package fr.fresnel.fourPolar.ui.algorithms.preprocess.sampleSet;

import java.io.IOException;

import fr.fresnel.fourPolar.algorithm.preprocess.darkBackground.IChannelDarkBackgroundRemover;
import fr.fresnel.fourPolar.algorithm.preprocess.realignment.IChannelRealigner;
import fr.fresnel.fourPolar.algorithm.preprocess.segmentation.ICapturedImageSetSegmenter;
import fr.fresnel.fourPolar.core.image.captured.ICapturedImageSet;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.io.exceptions.image.captured.CapturedImageReadFailure;
import fr.fresnel.fourPolar.io.image.captured.ICapturedImageSetReader;
import fr.fresnel.fourPolar.ui.exceptions.algorithms.preprocess.sampleSet.SampleSetPreprocessFailure;

class SampleImageSetPreprocessor implements ISampleImageSetPreprocessor {
    private final int _numChannels;

    private final ICapturedImageSetSegmenter _capturedImageSetSegmenter;
    private final ICapturedImageSetReader _capturedImageSetReader;

    private final IChannelDarkBackgroundRemover[] _backgroundRemovers;
    private final IChannelRealigner[] _realigners;

    public SampleImageSetPreprocessor(ISampleImageSetPreprocessorBuilder builder) {
        this._numChannels = this._setNumChannels(builder);

        this._capturedImageSetReader = this._setCapturedImageSetReader(builder);
        this._capturedImageSetSegmenter = this._setCapturedImageSetSegmenter(builder);

        this._realigners = this._setChannelRealigners(builder);
        this._backgroundRemovers = this._setChannelDarkBackgroundRemover(builder);
    }

    @Override
    public void setCapturedImageSet(ICapturedImageFileSet capturedImageFileSet) throws SampleSetPreprocessFailure {
        ICapturedImageSet capturedImageSet = this._readCapturedImageSet(capturedImageFileSet);

        // Keep the captured image set in the segmenter, for it to be segmented with
        // each call for a channel.
        this._capturedImageSetSegmenter.setCapturedImage(capturedImageSet);
    }

    private ICapturedImageSet _readCapturedImageSet(ICapturedImageFileSet capturedImageFileSet)
            throws SampleSetPreprocessFailure {
        try {
            return this._capturedImageSetReader.read(capturedImageFileSet);
        } catch (CapturedImageReadFailure e) {
            throw new SampleSetPreprocessFailure(e.getMessage());
        }
    }

    @Override
    public IPolarizationImageSet getPolarizationImageSet(int channel) {
        IPolarizationImageSet polImageSet = this._createChannelPolarizationImages(channel);

        this._realignChannelPolarizationImages(polImageSet);
        this._removeChannelDarkBackgrounds(polImageSet);

        return polImageSet;
    }

    @Override
    public void closeResources() throws IOException {
        this._closeCapturedImageReaderResources();
    }

    /**
     * Set number of channels.
     */
    private int _setNumChannels(ISampleImageSetPreprocessorBuilder builder) {
        return builder.getNumChannels();
    }

    /**
     * Set captured image set segmenter.
     */
    private ICapturedImageSetSegmenter _setCapturedImageSetSegmenter(ISampleImageSetPreprocessorBuilder builder) {
        return builder.getSegmenter();
    }

    /**
     * Set the reader of captured image.
     */
    private ICapturedImageSetReader _setCapturedImageSetReader(ISampleImageSetPreprocessorBuilder builder) {
        return builder.getCapturedImageSetReader();
    }

    /**
     * Set channel realigners
     */
    private IChannelRealigner[] _setChannelRealigners(ISampleImageSetPreprocessorBuilder builder) {
        int numChannels = builder.getNumChannels();

        IChannelRealigner[] realigners = new IChannelRealigner[numChannels];
        for (int channel = 1; channel <= this._numChannels; channel++) {
            realigners[channel - 1] = builder.getRealigners(channel);
        }

        return realigners;
    }

    /**
     * Set channel dark back ground remover.
     */
    private IChannelDarkBackgroundRemover[] _setChannelDarkBackgroundRemover(
            ISampleImageSetPreprocessorBuilder builder) {
        int numChannels = builder.getNumChannels();

        IChannelDarkBackgroundRemover[] channelRemovers = new IChannelDarkBackgroundRemover[numChannels];
        for (int channel = 1; channel <= this._numChannels; channel++) {
            channelRemovers[channel - 1] = builder.getBackgroundRemovers(channel);
        }

        return channelRemovers;
    }

    private IPolarizationImageSet _createChannelPolarizationImages(int channel) {
        return this._capturedImageSetSegmenter.segment(channel);

    }

    private void _realignChannelPolarizationImages(IPolarizationImageSet polarizationImageSet) {
        int channel = polarizationImageSet.channel();
        this._realigners[channel - 1].realign(polarizationImageSet);
    }

    private void _removeChannelDarkBackgrounds(IPolarizationImageSet polarizationImageSet) {
        int channel = polarizationImageSet.channel();
        this._backgroundRemovers[channel - 1].remove(polarizationImageSet);
    }

    private void _closeCapturedImageReaderResources() throws IOException {
        this._capturedImageSetReader.close();
    }

}