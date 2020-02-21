package fr.fresnel.fourPolar.core.image.generic;

import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;

/**
 * Interface for factories of Image implementations.
 */
public interface ImageFactory<T extends PixelType> {
    /**
     * Create an image, with the given dimensions and type.
     * 
     * @return
     */
    public Image<T> create(long[] dim, T pixelType);

}