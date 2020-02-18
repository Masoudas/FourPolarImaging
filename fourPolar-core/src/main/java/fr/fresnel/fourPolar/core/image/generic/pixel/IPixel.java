package fr.fresnel.fourPolar.core.image.generic.pixel;

/**
 * An interface that models the pixels of an image.
 * The generic type takes the pixel types.
 */
public interface IPixel<T> {
    /**
     * Returns the value associated with the pixel.
     */
    public T value();

    
}