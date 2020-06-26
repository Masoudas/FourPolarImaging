package fr.fresnel.fourPolar.io.image.captured.file;

import fr.fresnel.fourPolar.core.exceptions.imageSet.acquisition.IncompatibleCapturedImage;
import fr.fresnel.fourPolar.core.image.captured.checker.ICapturedImageChecker;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFile;

/**
 * This is a checker class, which only checks whether the image exists or not.
 * This checker ensures that a given captured image exists when using the image
 * for future applications.
 */
class CapturedImageExistsChecker implements ICapturedImageChecker {
    private static String notExists = "The captured image has been removed.";

    @Override
    public void check(ICapturedImageFile capturedImageFile) throws IncompatibleCapturedImage {
        // TODO: maybe we should check here that orientation and polarization and soi
        // image exist too.
        _checkCapturedImageFileIsOnDisk(capturedImageFile);

    }

    private void _checkCapturedImageFileIsOnDisk(ICapturedImageFile capturedImageFile)
            throws IncompatibleCapturedImage {
        if (!capturedImageFile.file().exists()) {
            throw new IncompatibleCapturedImage(new fr.fresnel.fourPolar.core.image.captured.file.RejectedCapturedImage(
                    capturedImageFile.file(), notExists));
        }
    }

}