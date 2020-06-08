package fr.fresnel.fourPolar.io.image.captured;

import java.io.IOException;

import fr.fresnel.fourPolar.core.image.captured.ICapturedImageSet;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.io.exceptions.image.captured.CapturedImageReadFailure;

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
     * @throws CapturedImageReadFailure In case of file IO issues.
     */
    public ICapturedImageSet read(ICapturedImageFileSet fileSet) throws CapturedImageReadFailure;

    /**
     * Close any resources associated with the Reader.
     * 
     * @throws IOException
     */
    public void close() throws IOException;

}