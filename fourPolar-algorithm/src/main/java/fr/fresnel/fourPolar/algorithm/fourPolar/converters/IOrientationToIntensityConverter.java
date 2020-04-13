package fr.fresnel.fourPolar.algorithm.fourPolar.converters;

import fr.fresnel.fourPolar.core.physics.dipole.IOrientationVector;
import fr.fresnel.fourPolar.core.physics.polarization.IntensityVector;

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
    public void convert(IOrientationVector orientationVector, IntensityVector intensityVector);
}