package fr.fresnel.fourPolar.io.image.captured;

import java.io.IOException;

import fr.fresnel.fourPolar.core.image.captured.ICapturedImage;
import fr.fresnel.fourPolar.core.image.captured.fileContainer.ICapturedImageFileSet;

/**
 * An interface for reading the captured image.
 */
public interface ICapturedImageReader {
    /**
     * Read an image of the file set labeled by fileLabel. Several images can
     * be read with the same interface.
     * 
     * @param fileSet
     * @param fileLabel The file label of the image to be read. Should be from
     *                  {@link Cameras}
     * @return
     * @throws IllegalArgumentException Thrown in case the given fileLabel is not in
     *                                  the file set.
     * @throws IOException              In case of file IO issues.
     */
    public ICapturedImage read(ICapturedImageFileSet fileSet, String fileLabel)
            throws IllegalArgumentException, IOException;
}