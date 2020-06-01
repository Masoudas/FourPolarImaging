package fr.fresnel.fourPolar.algorithm.preprocess.segmentation;

import fr.fresnel.fourPolar.core.image.captured.ICapturedImageSet;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;

/**
 * An interface for segmenting captured images (be it bead or sample).
 */
public interface ICapturedImageSetSegmenter {
    /**
     * Set the captured image set
     * 
     * @param capturedImageSet
     */
    public void setCapturedImage(ICapturedImageSet capturedImageSet);

    /**
     * Segements the demanded channel of the captured images and returns the
     * corresponding polarization set.
     */
    public IPolarizationImageSet segment(int channel);

}