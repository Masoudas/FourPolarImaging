package fr.fresnel.fourPolar.io.image.generic;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;

/**
 * ّAn interface for writing {@code Image}.
 */
public interface ImageWriter<T extends PixelType> {
    /**
     * Writes the given {@code Image} to the given destination. 
     * 
     * @param path
     * @param image
     * @throws IOException
     */
    public void write(final File path, final Image<T> image) throws IOException;

    /**
     * Writes the given {@code Image} to the given destination, with the given Metadata. 
     * 
     * @param path
     * @param image
     * @throws IOException
     */
    public void write(final File path, final IMetadata metadata, final Image<T> image) throws IOException;

    /**
     * Close any resources associated with the writer.
     * 
     * @throws IOException
     */
    public void close() throws IOException;
}