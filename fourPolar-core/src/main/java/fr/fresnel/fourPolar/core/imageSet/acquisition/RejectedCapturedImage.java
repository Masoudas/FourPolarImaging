package fr.fresnel.fourPolar.core.imageSet.acquisition;

import java.io.File;

/**
 * A class for keeping a captured image that is not compatible with our format.
 */
public class RejectedCapturedImage {
    private File _image;
    private String _reason;

    /**
     * Set the file that has been rejected together with the reason.
     * @param image
     * @param reason
     */
    public RejectedCapturedImage(File image, String reason) {
        this._image = image;
        this._reason = reason;
    }

    /**
     * Returns the file that was rejected.
     * @return
     */
    public File getFile(){
        return this._image;
    }

    /**
     * Returns the reason why this image was rejected as a string.
     * @return
     */
    public String getReason(){
        return this._reason;
    }
    
}