package fr.fresnel.fourPolar.core.image.generic;

import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;

public interface Image<T extends PixelType> {
    /**
     * Returns the interface for iterating over the pixels of an image in an ordered
     * fashion.
     * 
     * @return
     */
    public IPixelCursor<T> getCursor();

    /**
     * Creates a cursor over the specified start and end interval. Note that the
     * {@link IPixelCursor#localize()} method offsets all the locations to [0, 0,
     * 0]. Hence, the location of bottomCorner would be zero in the new iterator.
     * 
     * @param bottomCorner is the bottom corner of the interval, starting from
     *                     [0,0,0, ...]. This is the actual address of the pixel
     * @param len          is the length of the interval. The length includes the
     *                     first pixel as well, hence it can't be zero.
     * @return a cursor that iterates over this interval.
     * @throws IllegalArgumentException if len or bottomCorner don't have same
     *                                  dimension as image, or bottomCorner is
     *                                  negative, or len is not at least one, or
     *                                  that bottomCorner + len >= dimension image.
     */
    public IPixelCursor<T> getCursor(long[] bottomCorner, long[] len) throws IllegalArgumentException;

    /**
     * Returns the interface for randomly accessing the pixels of an image.
     * 
     * @return
     */
    public IPixelRandomAccess<T> getRandomAccess();

    /**
     * Returns the {@link ImageFactory} associated with this image.
     */
    public ImageFactory getFactory();

    /**
     * Returns the {@link IMetadata} associated with this image.
     * 
     * @return
     */
    public IMetadata getMetadata();

}