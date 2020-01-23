package fr.fresnel.fourPolar.io.image.tiff;

import java.io.IOException;

import fr.fresnel.fourPolar.core.image.captured.ICapturedImage;
import fr.fresnel.fourPolar.core.imageSet.acquisition.ICapturedImageFileSet;
import fr.fresnel.fourPolar.io.image.ICapturedImageReader;

/**
 * Used for reading a tiff image and converting to the unsigned short.
 */
public class TiffCapturedImageReader implements ICapturedImageReader {

    @Override
    public ICapturedImage read(ICapturedImageFileSet fileSet, String fileLabel)
            throws IllegalArgumentException, IOException {
        _checkFileLabel(fileSet, fileLabel);        

        return null;
    }

    private void _checkFileLabel(ICapturedImageFileSet fileSet, String fileLabel) {
        if (!fileSet.hasLabel(fileLabel)){
            throw new IllegalArgumentException("The given file label is not in the file set.");
        }
    }

    
}