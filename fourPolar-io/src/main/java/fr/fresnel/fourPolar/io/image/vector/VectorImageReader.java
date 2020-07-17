package fr.fresnel.fourPolar.io.image.vector;

import java.io.File;

import fr.fresnel.fourPolar.core.image.vector.VectorImage;
import fr.fresnel.fourPolar.io.exceptions.image.vector.VectorImageIOIssues;

/**
 * An interface for reading {@link VectorImage} from the disk, as written by
 * {@link VectorImageReader}.
 */
public interface VectorImageReader {
    /**
     * Read the given image name from the given root folder and return the vector
     * image. root must not provide any file name, and imageName must not provide
     * any file extension.
     * 
     * @param root      is the root folder of where the image is located.
     * @param imageName is the name of the image (not including extension).
     * @return the vector image.
     * @throws VectorImageIOIssues if there are IO issues when reading the image
     *                             from disk.
     */
    public VectorImage read(File root, String imageName) throws VectorImageIOIssues;

    /**
     * Close all resources associated with this reader.
     * 
     * @throws VectorImageIOIssues
     */
    public void close() throws VectorImageIOIssues;
}