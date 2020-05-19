package fr.fresnel.fourPolar.algorithm.util.image.axis;

import java.util.Objects;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.IPixelRandomAccess;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.IPixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;

class ReassingerToXYCZT {
    private ReassingerToXYCZT() {
        throw new AssertionError();
    }

    /**
     * Reassigns the axis of an image using its cursor. Note that if an axis does
     * not exist, it's appended to the new image with dimension one.
     * 
     * @param image is the image interface to be reassigned.
     * @param t     is the image pixel type.
     * 
     * @throws IllegalArgumentException if the old or new axis don't start with XY,
     *                                  or if dimension of cursor and metadata don't
     *                                  match.
     */
    public static <T extends PixelType> Image<T> reassign(Image<T> image, T t) {
        Objects.requireNonNull(image, "image can't be null");
        Objects.requireNonNull(t, "t can't be null");

        if (!image.getMetadata().axisOrder().name().substring(0, 2).equals("XY")) {
            throw new IllegalArgumentException("Axis does not start with XY");
        }

        IMetadata metadata_xyczt = _createMetadata(image.getMetadata());
        Image<T> reassignedImage = image.getFactory().create(metadata_xyczt, t);

        AxisOrder oldAxisOrder = image.getMetadata().axisOrder();
        IPixelRandomAccess<T> ra = reassignedImage.getRandomAccess();
        for (IPixelCursor<T> cursor = image.getCursor(); cursor.hasNext();) {
            IPixel<T> pixel = cursor.next();
            long[] position_xyczt = _convertPositionToXYCZT(cursor.localize(), oldAxisOrder);

            ra.setPosition(position_xyczt);
            ra.setPixel(pixel);
        }

        return reassignedImage;
    }

    /**
     * Create metdata of the reassigned image. If an axis does not exist, set it to
     * one.
     * 
     * @param metadata
     * @return
     */
    private static IMetadata _createMetadata(IMetadata metadata) {
        int z_axis = metadata.axisOrder().z_axis;
        int c_axis = metadata.axisOrder().c_axis;
        int t_axis = metadata.axisOrder().t_axis;

        long[] dim = metadata.getDim();
        long[] dim_xyczt = { dim[0], dim[1], 0, 0, 0 };

        dim_xyczt[2] = c_axis > 0 ? dim[c_axis] : 1;
        dim_xyczt[3] = z_axis > 0 ? dim[z_axis] : 1;
        dim_xyczt[4] = t_axis > 0 ? dim[t_axis] : 1;

        return new Metadata.MetadataBuilder(dim_xyczt).axisOrder(AxisOrder.XYCZT).bitPerPixel(metadata.bitPerPixel())
                .build();
    }

    private static long[] _convertPositionToXYCZT(long[] position, AxisOrder oldAxisOrder) {
        long[] position_xyczt = { position[0], position[1], 0, 0, 0 };

        if (oldAxisOrder.c_axis > 0)
            position_xyczt[2] = position[oldAxisOrder.c_axis];
        if (oldAxisOrder.z_axis > 0)
            position_xyczt[3] = position[oldAxisOrder.z_axis];
        if (oldAxisOrder.t_axis > 0)
            position_xyczt[4] = position[oldAxisOrder.t_axis];

        return position_xyczt;
    }
}