package fr.fresnel.fourPolar.algorithm.preprocess.segmentation;

import fr.fresnel.fourPolar.core.image.captured.ICapturedImage;
import fr.fresnel.fourPolar.core.image.captured.ICapturedImageSet;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.IFieldOfView;

/**
 * Segements the given {@link ICapturedImage} of a bead image (an image that is
 * used for registration) to yield a {@link IPolarizationImageSet}. See
 * {@link IChannelRegistrator} for a discussion on how the images are segmented.
 */
public class BeadCapturedImageSetSegmenter {
    private PolarizationImageSetCreator _poleImageCreator = null;

    /**
     * To avoid creating the segmenter every time a new CapturedImageSet is
     * provided, we cache an instance of both.
     */
    private ChannelPolarizationSegmenter _singleChannelSegmenter = new BeadSingleChannelPolarizationSegmenter();
    private ChannelPolarizationSegmenter _multiChannelSegmenter = new BeadMultiChannelPolarizationSegmenter();

    /**
     * 
     * @param fov         is the field of view of the polarizations.
     * @param cameras     is the number of cameras for this setup.
     * @param numChannels is the number of channels.
     */
    public BeadCapturedImageSetSegmenter(IFieldOfView fov, Cameras cameras, int numChannels) {
        this._poleImageCreator = this._chooseCameraSegmenter(fov, cameras, numChannels);
    }

    private PolarizationImageSetCreator _chooseCameraSegmenter(IFieldOfView fov, Cameras cameras, int numChannels) {
        if (cameras == Cameras.One) {
            _poleImageCreator = new OneCameraPolararizationImageSetCreator(fov, numChannels);
        } else if (cameras == Cameras.Two) {
            _poleImageCreator = new TwoCameraPolararizationImageSetCreator(fov, numChannels);
        } else {
            _poleImageCreator = new FourCameraPolararizationImageSetCreator(fov, numChannels);
        }

        return _poleImageCreator;
    }

    public void setCapturedImage(ICapturedImageSet capturedImageSet) {
        if (capturedImageSet.hasMultiChannelImage()) {
            this._poleImageCreator.setSegmenter(this._multiChannelSegmenter);
        } else {
            this._poleImageCreator.setSegmenter(this._singleChannelSegmenter);
        }

        this._poleImageCreator.setCapturedImageSet(capturedImageSet);

    }

    /**
     * Segements the given channel of the captured images and returns the
     * corresponding polarization set.
     */
    public IPolarizationImageSet segment(int channel) {
        return this._poleImageCreator.create(channel);
    }

}