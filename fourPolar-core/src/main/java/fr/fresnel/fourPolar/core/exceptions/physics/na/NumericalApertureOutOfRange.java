package fr.fresnel.fourPolar.core.exceptions.physics.na;

/**
 * Thrown when out of character NA values are given :D.
 */
public class NumericalApertureOutOfRange extends IllegalArgumentException{
    private static final long serialVersionUID = -1366336600962525700L;

    public NumericalApertureOutOfRange(String message) {
        super(message);
    }
    
}