package fr.fresnel.fourPolar.algorithm.util.image.axis;

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
    public static <T extends PixelType> Image<T> reassignAndResize(Image<T> image, T t, long[] newImgDim) {
        return ReassingerToXYCZT.reassignAndResize(image, t, newImgDim);
    }

}