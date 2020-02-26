package fr.fresnel.fourPolar.io.image.polarization;

import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.image.polarization.fileContainer.IPolarizationImageFileSet;

/**
 * An interface for reading a {@link IPolarizationImageSet}.
 */
public interface IPolarizationImageSetReader {
    /**
     * Reads the given {@link IPolarizationImageSet}.
     * @param fileSet is the set of image files associated with the polarization images. 
     * @return the polarization image set.
     */
    public IPolarizationImageSet read(IPolarizationImageFileSet fileSet);
    
}
