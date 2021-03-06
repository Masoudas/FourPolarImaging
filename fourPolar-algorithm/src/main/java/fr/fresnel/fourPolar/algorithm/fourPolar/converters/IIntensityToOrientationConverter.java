package fr.fresnel.fourPolar.algorithm.fourPolar.converters;

import fr.fresnel.fourPolar.algorithm.exceptions.fourPolar.converters.ImpossibleOrientationVector;
import fr.fresnel.fourPolar.core.physics.dipole.IOrientationVector;
import fr.fresnel.fourPolar.core.physics.polarization.IntensityVector;

/**
 * Interface for converting an {@link IPolarizationIntensity} to a
 * {@link IOrientationVector}.
 */
public interface IIntensityToOrientationConverter {
    /**
     * Convert a given set of polarization intensities to the orientation vector.
     * 
     * 
     * @param orientationVector
     * @return
     */
    public void convert(IntensityVector intensity, IOrientationVector orientationVector) throws ImpossibleOrientationVector;
}