package fr.fresnel.fourPolar.io.image.orientation;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.core.image.orientation.IOrientationImage;

/**
 * And interface for writing an orientation image to the disk.
 */
public interface IOrientationImageWriter {
    /**
     * Write the orientation image to the {@link IOrientationImageFileSet} paths.
     * 
     * @param rootFolder is the root folder of the captured images.
     * @param image is the orientation image.
     * @throws IOException thrown in case of low-level problems.
     */
    public void write(File rootFolder, IOrientationImage image) throws IOException;

    /**
     * Close all resources associated with this writer.
     * @throws IOException
     */
    public void close() throws IOException;
}