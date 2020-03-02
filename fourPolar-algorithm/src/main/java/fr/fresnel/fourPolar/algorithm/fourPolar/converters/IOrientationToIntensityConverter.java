package fr.fresnel.fourPolar.algorithm.fourPolar.converters;

import fr.fresnel.fourPolar.core.physics.dipole.IOrientationVector;
import fr.fresnel.fourPolar.core.physics.polarization.IPolarizationsIntensity;

/**
 * Interface for converting an {@link IOrientationVector} to a
 * {@link IPolarizationIntensity}
 */
public interface IOrientationToIntensityConverter {
    /**
     * Convert a given orientation vector to the corresponding polarization
     * intensities.
     * 
     * @param orientationVector
     * @return
     */
    public IPolarizationsIntensity convert(IOrientationVector orientationVector);
}