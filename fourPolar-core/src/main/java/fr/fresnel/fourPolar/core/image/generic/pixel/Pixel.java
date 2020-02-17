package fr.fresnel.fourPolar.core.image.generic.pixel;

/**
 * Represents the pixel entity, and implements the {@link IPixel}.
 */
public class Pixel<T> implements IPixel<T> {
    T _value;
    long[] _location;

    /**
     * Represents the pixel entity, and implements the {@link IPixel}.
     * @param location
     * @param value
     */
    public Pixel(long[] location, T value) {
        this._location = location;
        this._value = value;
    }

    @Override
    public long[] localize() {
        return _location;
    }

    @Override
    public T value() {
        return _value;
    }

}