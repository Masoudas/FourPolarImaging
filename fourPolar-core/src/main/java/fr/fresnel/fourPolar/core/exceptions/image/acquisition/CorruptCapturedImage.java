package fr.fresnel.fourPolar.core.exceptions.image.acquisition;

import fr.fresnel.fourPolar.core.imageSet.acquisition.RejectedCapturedImage;

/**
 * Exception thrown when the content of the given captured image is corrupt
 * (including metadata, etc). The class returns the rejected image as a
 * {@link RejectedCapturedImage}.
 * 
 */
public class CorruptCapturedImage extends Exception{
    private static final long serialVersionUID = 53687232131008L;

    private RejectedCapturedImage _rejectedImage;
    
    public CorruptCapturedImage(RejectedCapturedImage rejectedImage) {
        this._rejectedImage = rejectedImage;    
    }

    /**
     * Return the rejected image.
     * @return
     */
    public RejectedCapturedImage getRejectedImage() {
        return _rejectedImage;
    }
    
}