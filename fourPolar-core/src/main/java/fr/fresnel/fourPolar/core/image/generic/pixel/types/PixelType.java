package fr.fresnel.fourPolar.core.image.generic.pixel.types;

/**
 * A common base class for all the pixel types.
 */
public interface PixelType {
    /**
     * Returns the type of the pixel
     * @return
     */
    public PixelTypes getType();

    /**
     * Returns a new reference, with the current value (a deep copy).
     * @return
     */
    public PixelType copy();
}