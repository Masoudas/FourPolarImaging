package fr.fresnel.fourPolar.core.util.image.generic.metadata.axis;

import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;

/**
 * Using this class, we can reassign the axis of an image. In other words, we
 * change the {@link AxisOrder} of an image to a new order.
 */
public class AxisReassigner {
    private AxisReassigner() {
        throw new AssertionError();
    }

    /**
     * Reassigns the axis of an image. Note that if an axis does not exist, it's
     * appended to the new image with dimension one.
     * 
     * @throws IllegalArgumentException if the old or new axis don't start with XY.
     */
    public static <T extends PixelType> Image<T> reassignToXYCZT(Image<T> image, T t) {
        return ReassingerToXYCZT.reassign(image, t);
    }

    /**
     * Reassigns the axis of an image using its cursor, and resize the new image as
     * specified. Note that if an axis does not exist, it's appended to the new
     * image. Moreover, all extra dimensions will have zero intensity.
     * 
     * @param image     is the image interface to be reassigned.
     * @param t         is the image pixel type.
     * @param newImgDim is the new image dimension (should be greater than equal
     *                  original image, and five dimensional)
     * 
     * @throws IllegalArgumentException if the old or new axis don't start with XY,
     *                                  or if new dimension is not 5 dimensional, or
     *                                  new dim is not greater than equal old
     *                                  dimension.
     */
    public static <T extends PixelType> Image<T> reassignToXYCZTAndResize(Image<T> image, T t, long[] newImgDim) {
        return ReassingerToXYCZT.reassignAndResize(image, t, newImgDim);
    }

    /**
     * Defines an axis order for the given image. Note that the new image refers to
     * the same underlying image, i.e, even though {@link IMetadata} changes, the
     * underlying image pixels are not.
     * 
     * @param <T>          is the pixel type of the image.
     * @param image        is the image.
     * @param newAxisOrder is the axis order to be assigned to the image.
     * 
     * @throws IllegalArgumentException if the given image already has an axis
     *                                  order, or the new axis order has unequal
     *                                  dimension to the image.
     * @return an image instance with axis order that contains the reference to the
     *         image with no axis order.
     */
    public static <T extends PixelType> Image<T> defineAxis(Image<T> image, AxisOrder newAxisOrder) {
        return AxisDefiner.defineAxis(image, newAxisOrder);
    }
}