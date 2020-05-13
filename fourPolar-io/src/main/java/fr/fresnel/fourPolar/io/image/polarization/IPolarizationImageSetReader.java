package fr.fresnel.fourPolar.io.image.polarization;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.core.exceptions.image.polarization.CannotFormPolarizationImageSet;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
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
     * @param channel is the channel number.
     * @return the polarization image set.
     * 
     * @throws IOException                    in case of low-level IO issues, or if
     *                                        the image files don't exist.
     * @throws CannotFormPolarizationImageSet in case the polarization set can't be
     *                                        formed if images don't have the same
     *                                        dimension or are not XYCZT.
     */
    public IPolarizationImageSet read(File root4PProject, ICapturedImageFileSet fileSet, int channel)
            throws IOException, CannotFormPolarizationImageSet;

    /**
     * Close all resources associated with this reader.
     */
    public void close() throws IOException;

}
