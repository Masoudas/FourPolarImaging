package fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model;

/**
 * Raised when no converter from the given Image interface to {@link ConverterToImgLib2NotFound} is
 * found. 
 */
public class ConverterToImgLib2NotFound extends Exception{

    /**
     *
     */
    private static final long serialVersionUID = 7172771883429023553L;

    @Override
    public String getMessage() {
        return "No converter from the given Image interface to Img is found";
    }
}