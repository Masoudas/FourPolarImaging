package fr.fresnel.fourPolar.algorithm.exceptions.fourPolar.propagation;

import fr.fresnel.fourPolar.core.physics.propagation.IOpticalPropagation;

/**
 * Thrown when a calculated optical propagation does not have inverse.
 */
public class OpticalPropagationNotInvertible extends Exception{
    private static final long serialVersionUID = 1542352352543218989L;

    @Override
    public String getMessage() {
        return "Cannot compute the inverse optical propagation for the channel";
    }
}