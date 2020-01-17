package fr.fresnel.fourPolar.core.exceptions.physics.channel;

/**
 * Thrown when calibration factor is out of range.
 */
public class CalibrationFactorOutOfRange extends IllegalArgumentException{
    private static final long serialVersionUID = 536871008113121L;

    public CalibrationFactorOutOfRange(String message) {
        super(message);
    }
    
}