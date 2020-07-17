package fr.fresnel.fourPolar.io.image.vector;

import java.io.File;

import fr.fresnel.fourPolar.core.image.vector.VectorImage;
import fr.fresnel.fourPolar.io.exceptions.image.vector.VectorImageIOIssues;

/**
 * An interface for writing a {@link VectorImage} image.
 */
public interface VectorImageWriter {
    /**
     * Writes the given vector image to the root with the given name. root must not
     * provide any file name, and imageName must not provide any file extension.
     * 
     * @param root        is the root folder of where the image is stored.
     * @param imageName   is the name under which this image is stored.
     * @param vectorImage is the image instance.
     * @throws VectorImageIOIssues in case of IO issues when writing the image.
     */
    public void write(File root, String imageName, VectorImage vectorImage) throws VectorImageIOIssues;

    /**
     * Closes all resources associated with this writer.
     * 
     * @throws VectorImageIOIssues in case of IO issues when closing the resources.
     */
    public void close() throws VectorImageIOIssues;
}