package fr.fresnel.fourPolar.core.image.generic;

import java.util.Iterator;

import fr.fresnel.fourPolar.core.image.generic.pixel.IPixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;

/**
 * An interface for iterating over the pixels of an image. It extends the basic
 * {@code Iterator} class of java.
 */
public interface IPixelCursor<T extends PixelType> extends Iterator<IPixel<T>> {
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
    public void setPixel(IPixel<T> pixel);

    /**
     * Reset the cursor, that is put it to where it would be if newly created.
     */
    public void reset();

    /**
     * Returns the number of pixels associated with this iterator.
     * @return
     */
    public long size();
}