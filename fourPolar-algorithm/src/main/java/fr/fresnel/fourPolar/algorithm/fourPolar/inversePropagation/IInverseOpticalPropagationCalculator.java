package fr.fresnel.fourPolar.algorithm.fourPolar.inversePropagation;

import fr.fresnel.fourPolar.core.physics.dipole.DipoleSquaredComponent;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

/**
 * An interface for calculating the inverse propagation factors.
 */
public interface IInverseOpticalPropagationCalculator {
    /**
     * Returns the inverse propagation factor from the given {@link Polarization} to the given
     * {@link DipoleSquaredComponent}
     * 
     * @param polarization
     * @param component
     */
    public double getInverseFactor(Polarization polarization, DipoleSquaredComponent component);
    
}