package fr.fresnel.fourPolar.io.image;

import java.io.File;

/**
 * IImageChecker
 */
public interface IImageChecker {
    /**
     * Returns the image extension type for which the interface is implemented.
     * The extension should not contain dot (see for example {@link} TiffImageChecker).
     * @return
     */
    public String getExtension();
    
        /**
     * Checks compatibility and returns a boolean.
     * @param imagePath
     * @return
     */
    public boolean checkCompatible(File imagePath);

}