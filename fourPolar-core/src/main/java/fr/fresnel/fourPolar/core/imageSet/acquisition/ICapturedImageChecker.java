package fr.fresnel.fourPolar.core.imageSet.acquisition;

import java.io.File;

import fr.fresnel.fourPolar.core.exceptions.image.acquisition.CorruptCapturedImage;

/**
 * Used for checking that a captured image file has the desired format,
 */
public interface ICapturedImageChecker {
    /**
     * Returns the image extension type for which the interface is implemented.
     * The extension should not contain dot (see for example {@link} TiffCapturedImageChecker).
     * @return
     */
    public String getExtension();
    
    /**
     * Checks compatibility and throws exception. Checks include (but not limited to):
     * 1- Extension is correct
     * 2- File exists
     * 3- File is readable
     * 4- Content satisfies constraints.
     * 
     * @param imagePath
     * @return
     * @throws CorruptCapturedImage
     */
    public void checkCompatible(File imagePath) throws CorruptCapturedImage;    

}