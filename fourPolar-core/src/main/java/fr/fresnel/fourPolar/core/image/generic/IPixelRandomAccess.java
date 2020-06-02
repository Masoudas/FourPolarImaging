package fr.fresnel.fourPolar.core.image.generic;

import fr.fresnel.fourPolar.core.image.generic.pixel.IPixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;

/**
 * Using this interface, we can randomly access a location inside the image, and
 * set the value for that pixel. Note that to salvage memory, only one instance
 * of {@link IPixel} and {@ PixelType} are created.
 */
public interface IPixelRandomAccess<U extends PixelType> {
    /**
     * Sets the iterator position to the position specified. If position is out of
     * range, the iterator position is not set.
     * 
     * @param position
     * 
     * @throws IllegalArgumentException if the position does not have the same
     *                                  dimension as the image.
     */
    public void setPosition(long[] position);

    /**
     * Sets the pixel associated with the position.
     * 
     * @param pixel is the pixel at this location
     * @throws ArrayIndexOutOfBoundsException in case the provided position does not
     *                                        exist.
     */
    public void setPixel(IPixel<U> pixel) throws ArrayIndexOutOfBoundsException;

    /**
     * Returns the pixel associated with the position.
     * 
     * @param pixel is the pixel at this location
     * @throws ArrayIndexOutOfBoundsException in case the provided position does not
     *                                        exist.
     */
    public IPixel<U> getPixel() throws ArrayIndexOutOfBoundsException;
}