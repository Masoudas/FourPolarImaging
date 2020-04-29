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
     * Creates a cursor over the specified start and end interval.
     * 
     * @param bottomCorner is the bottom corner of the interval, starting from
     *                     [0,0,0, ...].
     * @param len          is the length of the interval.
     * @return a cursor that iterates over this interval.
     * @throws IllegalArgumentException is thrown in case dimension of bottomCorner
     *                                  and len don't match that of image or
     *                                  bottomCorner + len >= dimension image.
     */
    public IPixelCursor<T> getCursor(long[] bottomCorner, long[] len) throws IllegalArgumentException;

    /**
     * Returns the interface for randomly accessing the pixels of an image.
     * 
     * @return
     */
    public IPixelRandomAccess<T> getRandomAccess();

    /**
     * Returns the pixel type associated with this image.
     * 
     * @return
     */
    public Type getPixelType();

    /**
     * Returns the {@link ImageFactory} associated with this image.
     */
    public ImageFactory getFactory();

    /**
     * Returns the {@link IMetadata} associated with this image.
     * @return
     */
    public IMetadata gMetadata();

}