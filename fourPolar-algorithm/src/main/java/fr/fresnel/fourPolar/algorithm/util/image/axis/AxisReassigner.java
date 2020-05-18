package fr.fresnel.fourPolar.algorithm.util.image.axis;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
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
     * appended to the new image with dimension one. Returns the same image if it is
     * XYCZT.
     * 
     * @throws IllegalArgumentException if the old or new axis don't start with XY.
     */
    public static <T extends PixelType> Image<T> reassignToXYCZT(Image<T> image, T t) {
        if (image.getMetadata().axisOrder() == AxisOrder.XYCZT) {
            return image;
        }

        return ReassingerToXYCZT.reassign(image.getCursor(), image.getMetadata(), image.getFactory(), t);
    }

    /**
     * Reassigns the axis of an image using its cursor. Note that if an axis does
     * not exist, it's appended to the new image with dimension one. Returns the
     * same image if it is XYCZT.
     * 
     * @param cursor   is the cursor of the image.
     * @param metadata is the metadata of the associated image.
     * @param factory  is the factory for creating the image inteface.
     * @param t        is the image pixel type.
     * 
     * @throws IllegalArgumentException if the old or new axis don't start with XY,
     *                                  or if dimension of cursor and metadata don't
     *                                  match.
     */
    public static <T extends PixelType> Image<T> reassignToXYCZT(IPixelCursor<T> cursor, IMetadata metadata,
            ImageFactory factory, T t) {
        return ReassingerToXYCZT.reassign(cursor, metadata, factory, t);
    }

    /**
     * Breaks down the channels of the given image and returns an array of images,
     * where each element is a channel image. Note that axis order of the new image
     * would be that of
     * 
     * @param <T>
     * @param image
     * @param t
     * @return
     */
    public static <T extends PixelType> Image<T>[] breakChannels(Image<T> image, T t) {
        if (image.getMetadata().numChannels() == 1) {
            return new Image[] { image };
        }

    }

}