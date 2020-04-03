package fr.fresnel.fourPolar.io.exceptions.image.generic;

/**
 * Exception thrown when no proper reader is found for the given {@link Image}
 * type, or the underlying {@link PixelType}, to the desired file extension.
 */
public class NoReaderFoundForImage extends Exception {

    private static final long serialVersionUID = 536871008423L;

    @Override
    public String getMessage() {
        return "No reader was found the given Image instance or pixel type for the desired extension.";
    }

    
}