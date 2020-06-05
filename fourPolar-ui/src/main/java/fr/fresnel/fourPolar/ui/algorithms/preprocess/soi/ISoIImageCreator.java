package fr.fresnel.fourPolar.ui.algorithms.preprocess.soi;

import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.image.soi.ISoIImage;

/**
 * An interface for creating sum of intensity images from polarization images.
 */
public interface ISoIImageCreator {
    /**
     * Create the soi image.
     */
    public ISoIImage create(IPolarizationImageSet polarizationImageSet);
}