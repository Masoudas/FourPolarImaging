package fr.fresnel.fourPolar.io.image.polarization.soi;

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
     * @param fileSet is the set of image files associated with the polarization
     *                images.
     * @return the polarization image set.
     */
    public ISoIImage read(File rootFolder, ICapturedImageFileSet fileSet) throws IOException;

    /**
     * Close all resources associated with this reader.
     */
    public void close() throws IOException;


}