package fr.fresnel.fourPolar.core.exceptions.physics.channel;

/**
 * Thrown when wavelength is not in the working range of the setup.
 */
public class WavelengthOutOfRange extends IllegalArgumentException{
    private static final long serialVersionUID = -8580385723298602254L;

    public WavelengthOutOfRange(String message) {
        super(message);
        
    }

    
}