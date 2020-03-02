package fr.fresnel.fourPolar.algorithm.fourPolar.inversePropagation;

import fr.fresnel.fourPolar.algorithm.exceptions.fourPolar.propagation.OpticalPropagationNotInvertible;
import fr.fresnel.fourPolar.core.exceptions.physics.propagation.PropagationFactorNotFound;
import fr.fresnel.fourPolar.core.physics.propagation.IInverseOpticalPropagation;
import fr.fresnel.fourPolar.core.physics.propagation.IOpticalPropagation;

/**
 * An interface for calculating the inverse propagation factors.
 */
public interface IInverseOpticalPropagationCalculator {
    /**
     * Returns the inverse propagation factor from the given {@link Polarization} to
     * the given {@link DipoleSquaredComponent}
     * 
     * @param polarization
     * @param component
     */
    public IInverseOpticalPropagation getInverse(IOpticalPropagation opticalPropagation)
            throws PropagationFactorNotFound, OpticalPropagationNotInvertible;

}