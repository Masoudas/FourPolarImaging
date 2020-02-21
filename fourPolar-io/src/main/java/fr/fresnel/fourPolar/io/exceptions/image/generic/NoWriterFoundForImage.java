package fr.fresnel.fourPolar.io.exceptions.image.generic;

/**
 * Exception thrown when no proper writer is found for the given {@code Image}
 * type, or {@code PixelType} to the desired file extension.
 */
public class NoWriterFoundForImage extends Exception {
    private static final long serialVersionUID = 74237849237320L;

    @Override
    public String getMessage() {
        return "No writer was found the given Image instance or pixel type for the desired extension.";
    }
}