package fr.fresnel.fourPolar.io.image.captured;

import java.io.IOException;

import fr.fresnel.fourPolar.core.image.captured.ICapturedImageSet;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;

/**
 * An interface for reading an {@link ICapturedImageSet} from the disk.
 */
public interface ICapturedImageSetReader {
    /**
     * Read an image of the file set labeled by fileLabel. Several images can be
     * read with the same interface.
     * 
     * @param fileSet is the file set that corresponds to the image set.
     * @return a captured image set containing all the images of this set.
     * @throws IOException In case of file IO issues.
     */
    public ICapturedImageSet read(ICapturedImageFileSet fileSet) throws IOException;

    /**
     * Close any resources associated with the Reader.
     * 
     * @throws IOException
     */
    public void close() throws IOException;

}