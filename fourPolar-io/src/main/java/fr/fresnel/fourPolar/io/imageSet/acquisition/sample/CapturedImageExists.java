package fr.fresnel.fourPolar.io.imageSet.acquisition.sample;

import java.io.File;

import fr.fresnel.fourPolar.core.exceptions.image.acquisition.CorruptCapturedImage;
import fr.fresnel.fourPolar.core.image.captured.ICapturedImageChecker;
import fr.fresnel.fourPolar.core.imageSet.acquisition.RejectedCapturedImage;

/**
 * This is a checker class, which only checks whether the image exists or not.
 * This checker ensures that when sample set is read the second time, all the
 * images still exist.
 */
class CapturedImageExists implements ICapturedImageChecker {
    private static String notExists = "The captured image has been removed.";

    @Override
    public String getExtension() {
        return null;
    }

    @Override
    public void checkCompatible(File imagePath) throws CorruptCapturedImage {
        if (!imagePath.exists()){
            throw new CorruptCapturedImage(new RejectedCapturedImage(imagePath, notExists));
        }
    }

    
}