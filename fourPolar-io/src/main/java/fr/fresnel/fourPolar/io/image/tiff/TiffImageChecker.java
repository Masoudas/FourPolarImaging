package fr.fresnel.fourPolar.io.image.tiff;

import java.io.File;

import fr.fresnel.fourPolar.io.image.IImageChecker;

/**
 * A class for checking the compatibility of a tiff image with the software
 * criteria.
 */
public class TiffImageChecker implements IImageChecker {

    @Override
    public boolean checkCompatible(File imagePath) {
        return true;
    }

    
}