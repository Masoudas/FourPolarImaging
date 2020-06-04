package fr.fresnel.fourPolar.algorithm.visualization.figures.registration;

import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.visualization.figures.polarization.IPolarizationImageSetComposites;

/**
 * An interface for creating {@link IPolarizationImageSetComposites} from a
 * {@link IPolarizationImageSet}.
 */
public interface IPolarizationImageSetCompositesCreater {
    /**
     * Create the composite for the given polarization image set.
     */
    public IPolarizationImageSetComposites create(IPolarizationImageSet polImageSet);
}