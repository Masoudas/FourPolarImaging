package fr.fresnel.fourPolar.io.image.orientation;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.core.exceptions.image.orientation.CannotFormOrientationImage;
import fr.fresnel.fourPolar.core.image.captured.fileSet.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.orientation.IOrientationImage;

/**
 * And interface for reading an orientation image from the disk.
 */
public interface IOrientationImageReader {
    /**
     * Read the orientation and return the corresponding {@link IOrientationImage}.
     * 
     * @param rootFolder is the root folder of the captured images.
     * @param fileSet is the captured file set corresponding to this orientation image.
     * @return
     */
    public IOrientationImage read(File rootFolder, ICapturedImageFileSet fileSet) throws IOException, CannotFormOrientationImage;

    /**
     * Close all resources associated with this reader.
     * @throws IOException
     */
    public void close() throws IOException;
}