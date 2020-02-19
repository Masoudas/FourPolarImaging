package fr.fresnel.fourPolar.core.image.generic;

import fr.fresnel.fourPolar.core.image.generic.pixel.IPixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;

/**
 * Using this interface, we can randomly access a location inside the image, and
 * set the value for that pixel.
 */
public interface IPixelRandomAccess {
    /**
     * Sets the iterator position to the position specified. If position is out of
     * range, the iterator position is not set.
     * 
     * @param location
     */
    public void setlocation(long[] location);

    /**
     * Sets the pixel associated with the position.
     * 
     * @param pixel is the pixel at this location
     */
    public void setPixel(IPixel<PixelType> pixel);

    /**
     * Returns the pixel associated with the position.
     * 
     * @param pixel is the pixel at this location
     */
    public IPixel<PixelType> getPixel();
}