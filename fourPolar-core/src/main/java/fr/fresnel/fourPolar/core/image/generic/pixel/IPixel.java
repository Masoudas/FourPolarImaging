package fr.fresnel.fourPolar.core.image.generic.pixel;

import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;

/**
 * An interface that models the pixels of an image. The generic type takes the
 * pixel types.
 * 
 * @param <T> is a {@code PixelType} 
 */
public interface IPixel<T extends PixelType> {
    /**
     * Returns the value associated with the pixel. This value would be the actual
     * underlying pixel, hence it can be modified directly.
     */
    public T value();
}