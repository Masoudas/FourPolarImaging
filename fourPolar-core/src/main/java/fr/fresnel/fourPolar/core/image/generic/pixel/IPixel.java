package fr.fresnel.fourPolar.core.image.generic.pixel;

/**
 * An interface that models the pixels of an image.
 * The generic type takes the pixel types.
 *
 * @param <T> is the pixel type.
 */
public interface IPixel<T> {
    /**
     * Returns the value associated with the pixel. This value would be the actual
     * underlying pixel, hence it can be modified directly.
     */
    public T value();
    
}