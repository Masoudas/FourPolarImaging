package fr.fresnel.fourPolar.core.exceptions.physics.dipole;

/**
 * Thrown when an orientation angle is not in the accepted range.
 */
public class OrientationAngleOutOfRange extends IllegalArgumentException{
    private static final long serialVersionUID = 536871008L;

    public OrientationAngleOutOfRange(String message) {
        super(message);
    }
}