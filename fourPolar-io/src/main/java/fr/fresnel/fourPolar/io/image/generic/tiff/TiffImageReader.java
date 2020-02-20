package fr.fresnel.fourPolar.io.image.generic.tiff;

import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;
import fr.fresnel.fourPolar.io.image.generic.ImageReader;

/**
 * Interface for reading a tiff Image and returning the corresponding {@code Image}.
 */
public interface TiffImageReader<T extends PixelType> extends ImageReader<T>{
}