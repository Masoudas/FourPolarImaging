package fr.fresnel.fourPolar.core.image.captured.checker;

import fr.fresnel.fourPolar.core.exceptions.imageSet.acquisition.IncompatibleCapturedImage;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFile;
import fr.fresnel.fourPolar.core.image.captured.file.RejectedCapturedImage;

/**
 * Used for checking that a captured image file has the desired format,
 */
public interface ICapturedImageChecker {
    /**
     * Checks compatibility and throws exception. Checks include (but not limited
     * to): 1- Extension is correct 2- File exists 3- File is readable 4- Content
     * satisfies additional constraints (number of channels or other constraints).
     * 
     * 
     * @throws IncompatibleCapturedImage in case a condition is violated. The
     *                                   exception contains a reference to
     *                                   {@link RejectedCapturedImage}.
     */
    public void check(ICapturedImageFile capturedImageFile) throws IncompatibleCapturedImage;

}