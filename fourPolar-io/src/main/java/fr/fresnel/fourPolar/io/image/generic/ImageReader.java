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
     * @param path
     * @return
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