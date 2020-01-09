package fr.fresnel.fourPolar.io.image.tiff;

import java.io.File;

import fr.fresnel.fourPolar.core.imageSet.acquisition.ICapturedImageChecker;

/**
 * A class for checking the compatibility of a tiff image with the software
 * criteria.
 */
public class TiffImageChecker implements ICapturedImageChecker {

    @Override
    public boolean checkCompatible(File imagePath) {
        return true;
    }

    @Override
    public String getExtension() {
        return "tif";
    }

    
}