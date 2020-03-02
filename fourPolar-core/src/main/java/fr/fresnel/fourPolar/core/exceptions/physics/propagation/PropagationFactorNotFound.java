package fr.fresnel.fourPolar.core.exceptions.physics.propagation;

import fr.fresnel.fourPolar.core.physics.dipole.DipoleSquaredComponent;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

/**
 * Exception thrown when an optical propagation factor does not exist or is
 * undefined.
 */
public class PropagationFactorNotFound extends Exception {
    private static final long serialVersionUID = 89329449821143353L;

    private final DipoleSquaredComponent _component;
    private final Polarization _pol;

    /**
     * Exception thrown when an optical propagation factor does not exist or is
     * undefined.
     * 
     * @param component
     * @param pol
     * @param message
     */
    public PropagationFactorNotFound(DipoleSquaredComponent component, Polarization pol) {
        _component = component;
        _pol = pol;

    }

    @Override
    public String getMessage() {
        return "Propagation factor from Dipole squared " + _component + " to polarization " + _pol + " does not exist."   ;
    }
}