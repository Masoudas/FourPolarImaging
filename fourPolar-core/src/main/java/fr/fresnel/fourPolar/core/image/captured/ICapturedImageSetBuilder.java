package fr.fresnel.fourPolar.core.image.captured;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;

/**
 * A private interface for accessing build parameters 
 */
abstract class ICapturedImageSetBuilder {
    abstract ICapturedImageFileSet getFileSet();

    abstract ICapturedImage[] getCapturedImages(String label);

}