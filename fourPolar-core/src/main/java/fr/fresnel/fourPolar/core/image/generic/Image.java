package fr.fresnel.fourPolar.core.image.generic;

import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Type;

public interface Image<T extends PixelType> {
    /**
     * Returns the dimensions of the underlying image as [columns, rows, ...]
     * 
     * @return
     */
    public long[] getDimensions();

    /**
     * Returns the interface for iterating over the pixels of an image in an ordered
     * fashion.
     * 
     * @return
     */
    public IPixelCursor<T> getCursor();

    /**
     * Returns the interface for randomly accessing the pixels of an image.
     * 
     * @return
     */
    public IPixelRandomAccess<T> getRandomAccess();


    /**
     * Returns the pixel type associated with this image.
     * @return
     */
    public Type getPixelType();


    /**
     * Returns the {@link ImageFactory} associated with this image.
     */
    public ImageFactory getFactory();

}