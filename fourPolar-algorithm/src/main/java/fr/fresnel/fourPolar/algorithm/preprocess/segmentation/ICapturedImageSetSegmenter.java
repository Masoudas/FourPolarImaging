package fr.fresnel.fourPolar.algorithm.preprocess.segmentation;

import fr.fresnel.fourPolar.core.image.captured.ICapturedImageSet;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;

/**
 * An interface for segmenting captured images (be it registration or sample).
 */
public interface ICapturedImageSetSegmenter {
    /**
     * Segements the demanded channel of the captured images and returns the
     * corresponding polarization set.
     */
    public IPolarizationImageSet segment(ICapturedImageSet capturedImageSet, int channel);

}