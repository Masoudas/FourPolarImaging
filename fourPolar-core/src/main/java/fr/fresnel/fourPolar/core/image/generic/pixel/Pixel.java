package fr.fresnel.fourPolar.core.image.generic.pixel;

/**
 * Represents the pixel entity, and implements the {@link IPixel}.
 */
public class Pixel<T> implements IPixel<T> {
    T _value;

    /**
     * Represents the pixel entity, and implements the {@link IPixel}.
     * 
     * @param value
     */
    public Pixel(T value) {
        this._value = value;
    }

    @Override
    public T value() {
        return _value;
    }

}