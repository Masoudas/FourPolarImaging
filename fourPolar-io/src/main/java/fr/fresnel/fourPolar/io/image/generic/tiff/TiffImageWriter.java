package fr.fresnel.fourPolar.io.image.generic.tiff;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.core.image.generic.ITiffMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;
import fr.fresnel.fourPolar.io.image.generic.ImageWriter;

/**
 * An interface for writing tiff images.
 */
public interface TiffImageWriter<T extends PixelType> extends ImageWriter<T> {
    /**
     * Write the given {@code Image} to the destionation, with the given metadata.
     * 
     * @param path
     * @param metadata
     * @param tiff
     * @throws IOException
     */
    public void write(File path, ITiffMetadata metadata, Image<T> tiff) throws IOException;
}