package fr.fresnel.fourPolar.io.image.generic;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;

/**
 * An interface for reading Image instances.
 */
public interface ImageReader<T extends PixelType> {
    /**
     * Read the given path and return the proper {@code Image} interface.
     * 
     * Note: If some property of metadata is not properly set (like the axis order),
     * the image is read with the incorrect metadata.
     * 
     * @param path is the path to image.
     * @return the image interface of the image read from the disk.
     * @throws IOException in case of IO issues.
     */
    public Image<T> read(final File path) throws IOException;

    /**
     * Close any resources associated with the Reader.
     * 
     * @throws IOException
     */
    public void close() throws IOException;

}