package fr.fresnel.fourPolar.io.image.polarization;

import java.io.IOException;

import fr.fresnel.fourPolar.core.image.captured.fileSet.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;

/**
 * An interface for reading a {@link IPolarizationImageSet}.
 */
public interface IPolarizationImageSetReader {
    /**
     * Reads the given {@link IPolarizationImageSet}.
     * @param fileSet is the set of image files associated with the polarization images. 
     * @return the polarization image set.
     */
    public IPolarizationImageSet read(ICapturedImageFileSet fileSet) throws IOException;

    /**
     * Close all resources associated with this reader.
     */
    public void close() throws IOException;
    
}
