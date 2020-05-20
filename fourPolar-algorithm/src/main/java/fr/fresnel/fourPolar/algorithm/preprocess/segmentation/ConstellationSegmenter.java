package fr.fresnel.fourPolar.algorithm.preprocess.segmentation;

import fr.fresnel.fourPolar.core.image.captured.ICapturedImageSet;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;

/**
 * An interface for segmenting the captured images that comply to a particular
 * constellation (i.e, four polarizations in one image, two polarizations in two
 * images, one polarization in four images).
 */
interface ConstellationSegmenter {
    /**
     * Define the captured image set to be segmented.
     */
    public void setCapturedImageSet(ICapturedImageSet capturedImageSet);

    /**
     * Returns the polarization image set of the given channel.
     * 
     * @param channel
     * @return
     */
    public IPolarizationImageSet segment(int channel);

}