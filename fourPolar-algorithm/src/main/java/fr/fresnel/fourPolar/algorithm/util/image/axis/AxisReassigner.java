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
     * Breaks down the channels of the given image and returns an array of images, where each
     * element is a channel image. Note that axis order of the new image would be that of 
     * @param <T>
     * @param image
     * @param t
     * @return
     */
    public static <T extends PixelType> Image<T>[] breakChannels(Image<T> image, T t) {
        if (image.getMetadata().numChannels() == 1){
            return new Image[] {image};
        }
        
    }

}