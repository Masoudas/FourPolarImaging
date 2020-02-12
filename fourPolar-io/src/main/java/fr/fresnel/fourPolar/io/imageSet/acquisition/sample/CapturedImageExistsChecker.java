package fr.fresnel.fourPolar.io.imageSet.acquisition.sample;

import java.io.File;

import fr.fresnel.fourPolar.core.exceptions.imageSet.acquisition.IncompatibleCapturedImage;
import fr.fresnel.fourPolar.core.image.captured.ICapturedImageChecker;
import fr.fresnel.fourPolar.core.imageSet.acquisition.RejectedCapturedImage;

/**
 * This is a checker class, which only checks whether the image exists or not.
 * This checker ensures that a given captured image exists when using the image
 * for future applications.
 */
class CapturedImageExistsChecker implements ICapturedImageChecker {
    private static String notExists = "The captured image has been removed.";

    @Override
    public String getExtension() {
        return null;
    }

    @Override
    public void checkCompatible(File imagePath) throws IncompatibleCapturedImage {
        if (!imagePath.exists()) {
            throw new IncompatibleCapturedImage(new RejectedCapturedImage(imagePath, notExists));
        }
    }

}