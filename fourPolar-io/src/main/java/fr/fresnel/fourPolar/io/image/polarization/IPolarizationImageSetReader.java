package fr.fresnel.fourPolar.io.image.polarization;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.core.exceptions.image.polarization.CannotFormPolarizationImageSet;
import fr.fresnel.fourPolar.core.image.captured.fileSet.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;

/**
 * Interface for reading the {@link IPolarizationImageSet} that corresponds to
 * the given {@link ICapturedImageFileSet}
 */
public interface IPolarizationImageSetReader {
    /**
     * Reads the given {@link IPolarizationImageSet}.
     * 
     * @param fileSet is the set of image files associated with the polarization
     *                images.
     * @return the polarization image set.
     */
    public IPolarizationImageSet read(File rootFolder, ICapturedImageFileSet fileSet) throws IOException,
        CannotFormPolarizationImageSet;

    /**
     * Close all resources associated with this reader.
     */
    public void close() throws IOException;

}
