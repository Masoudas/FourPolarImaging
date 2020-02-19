package fr.fresnel.fourPolar.core.image.generic;

import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Type;

public interface Image<T extends PixelType> {
    /**
     * Returns the dimensions of the underlying image as [columns, rows, ...]
     * 
     * @return
     */
    public long[] getDims();

    /**
     * Returns the interface for iterating over the pixels of an image in an ordered
     * fashion.
     * 
     * @return
     */
    public IPixelCursor getCursor();

    /**
     * Returns the interface for randomly accessing the pixels of an image.
     * 
     * @return
     */
    public IPixelRandomAccess getRandomAccess();


    /**
     * Returns the pixel type associated with this image.
     * @return
     */
    public Type getPixelType();

}