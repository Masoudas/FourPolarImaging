package fr.fresnel.fourPolar.algorithm.fourPolar.propagation.exceptions.fourPolar.propagation;

import fr.fresnel.fourPolar.core.physics.dipole.DipoleSquaredComponent;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

/**
 * Exception thrown when an optical propagation factor cannot be calculated.
 */
public class CannotComputePropagationFactor extends Exception {
    private static final long serialVersionUID = 8932945435935043353L;

    private final DipoleSquaredComponent _component;
    private final Polarization _pol;

    /**
     * Exception thrown when an optical propagation factor cannot be calculated.
     * 
     * @param component
     * @param pol
     * @param message
     */
    public CannotComputePropagationFactor(DipoleSquaredComponent component, Polarization pol, String message) {
        super(message);
        _component = component;
        _pol = pol;

    }

    @Override
    public String getMessage() {
        return "Propagation factor from Dipole squared " + _component + " to polarization " + _pol + " could not be computed."   ;
    }
}