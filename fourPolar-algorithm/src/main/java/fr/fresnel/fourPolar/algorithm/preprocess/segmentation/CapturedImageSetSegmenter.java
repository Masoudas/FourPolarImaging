package fr.fresnel.fourPolar.algorithm.preprocess.segmentation;

import fr.fresnel.fourPolar.core.image.captured.ICapturedImage;
import fr.fresnel.fourPolar.core.image.captured.ICapturedImageSet;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.IFieldOfView;

/**
 * Segements the given {@link ICapturedImage} to yield a
 * {@link IPolarizationImageSet}
 */
public class CapturedImageSetSegmenter {
    private ConstellationSegmenter _segmenter = null;

    /**
     * 
     * @param fov         is the field of view of the polarizations.
     * @param cameras     is the number of cameras for this setup.
     * @param numChannels is the number of channels.
     */
    public CapturedImageSetSegmenter(IFieldOfView fov, Cameras cameras, int numChannels) {
        this._segmenter = this._chooseCameraSegmenter(fov, cameras, numChannels);
    }

    private ConstellationSegmenter _chooseCameraSegmenter(IFieldOfView fov, Cameras cameras, int numChannels) {
        if (cameras == Cameras.One) {
            _segmenter = new OneCameraSegmenter(fov, numChannels);
        } else if (cameras == Cameras.Two) {
            _segmenter = new TwoCameraSegmenter(fov, numChannels);
        } else {
            _segmenter = new FourCameraSegmenter(fov, numChannels);
        }

        return _segmenter;
    }

    public void setCapturedImage(ICapturedImageSet capturedImageSet) {
        this._segmenter.setCapturedImageSet(capturedImageSet);

    }

    /**
     * Segements the given channel of the captured images and returns the
     * corresponding polarization set.
     */
    public IPolarizationImageSet segment(int channel) {
        return this._segmenter.segment(channel);
    }

}