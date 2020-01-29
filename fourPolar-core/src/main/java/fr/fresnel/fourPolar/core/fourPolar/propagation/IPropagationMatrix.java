package fr.fresnel.fourPolar.core.fourPolar.propagation;

import fr.fresnel.fourPolar.core.physics.dipole.DipoleDirection;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

/**
 * This interface models the propagation matrix of the four polar algorithm.
 */
public interface IPropagationMatrix {
    /**
     * Returns the propagation element from the given dipole direction
     * to the given polarization intensity. See also {@link DipoleDirection}.
     * @return
     */
    public double get(DipoleDirection direction, Polarization polarization);
    
}