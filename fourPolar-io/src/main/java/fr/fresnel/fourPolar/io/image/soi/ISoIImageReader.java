package fr.fresnel.fourPolar.io.image.soi;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.soi.ISoIImage;

/**
 * An interface for reading {@link ISoIImageReader}
 */
public interface ISoIImageReader {
    /**
     * Reads the {@link ISoIImage} corresponding to this captured file set.
     * 
     * @param root4PProject is the parent folder of the 4Polar project data.
     * @param fileSet       is the set of image files associated with the
     *                      polarization images.
     * @return the polarization image set.
     * 
     * @throws IOException in case of low-level IO issues, or if image file or
     *                     metadata is corrupt.
     */
    public ISoIImage read(File root4PProject, ICapturedImageFileSet fileSet, int channel) throws IOException;

    /**
     * Close all resources associated with this reader.
     */
    public void close() throws IOException;

}