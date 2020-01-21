package fr.fresnel.fourPolar.core.exceptions.image.acquisition;

/**
 * Exception thrown when the content of the given captured image is corrupt
 * (including metadata, etc).
 */
public class CorruptCapturedImage extends Exception{
    private static final long serialVersionUID = 53687232131008L;

    public CorruptCapturedImage(String message) {
        super(message);
    }

    public CorruptCapturedImage(Throwable e) {
        super(e);
    }
    
}