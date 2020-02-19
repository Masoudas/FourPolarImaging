package fr.fresnel.fourPolar.core.image.generic;

import java.util.Iterator;

import fr.fresnel.fourPolar.core.image.generic.pixel.IPixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;

/**
 * An interface for iterating over the pixels of an image. It extends the basic
 * {@code Iterator} class of java.
 */
public interface IPixelCursor extends Iterator<IPixel<PixelType>> {
    /**
     * Return the current pixel coordinate of the iterator, as [column, row, ...]
     * 
     * @return
     */
    public long[] localize();

    /**
     * Set the pixel value of the current position.
     * 
     * @param pixel
     */
    public void setPixel(IPixel<PixelType> pixel);
}