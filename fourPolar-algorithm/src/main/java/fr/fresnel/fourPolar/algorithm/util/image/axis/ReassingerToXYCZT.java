package fr.fresnel.fourPolar.algorithm.util.image.axis;

import java.util.Objects;
import java.util.stream.IntStream;

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
     * @throws IllegalArgumentException if the old or new axis don't start with XY.
     */
    public static <T extends PixelType> Image<T> reassign(Image<T> image, T t) {
        Objects.requireNonNull(image, "image can't be null");
        Objects.requireNonNull(t, "t can't be null");

        _checkAxisOrderStartsWithXY(image);
        Image<T> reassignedImage = _createXYCZTImage(image, t);
        _copyImagePixelsToXYCZTImage(image, reassignedImage);

        return reassignedImage;
    }

    /**
     * Reassigns the axis of an image using its cursor. Note that if an axis does
     * not exist, it's appended to the new image. Moreover, all extra dimensions
     * will have zero intensity.
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
        Objects.requireNonNull(image, "image can't be null");
        Objects.requireNonNull(t, "t can't be null");
        _checkAxisOrderStartsWithXY(image);
        _checkNewDimIs5D(newImgDim);
        _checkNewDimIsGreaterThanEqualOldDim(image.getMetadata().getDim(), newImgDim);

        Image<T> reassignedImage = _createXYCZTImage(image, t, newImgDim);
        _copyImagePixelsToXYCZTImage(image, reassignedImage);

        return reassignedImage;
    }

    private static <T extends PixelType> Image<T> _createXYCZTImage(Image<T> image, T t) {
        IMetadata metadata_xyczt = _createXYCZTMetadata(image.getMetadata());
        return image.getFactory().create(metadata_xyczt, t);
    }

    private static <T extends PixelType> Image<T> _createXYCZTImage(Image<T> image, T t, long[] newDim) {
        IMetadata oldImageMetadata = image.getMetadata();
        IMetadata metadata_xyczt_resized = new Metadata.MetadataBuilder(newDim).axisOrder(AxisOrder.XYCZT)
                .bitPerPixel(oldImageMetadata.bitPerPixel()).build();
        return image.getFactory().create(metadata_xyczt_resized, t);
    }

    private static <T extends PixelType> void _checkAxisOrderStartsWithXY(Image<T> image) {
        if (!image.getMetadata().axisOrder().name().substring(0, 2).equals("XY")) {
            throw new IllegalArgumentException("Axis does not start with XY");
        }
    }

    private static void _checkNewDimIs5D(long[] newDim) {
        if (newDim.length != 5) {
            throw new IllegalArgumentException("New image dimension for resizing must 5 dimensional.");
        }
    }

    private static void _checkNewDimIsGreaterThanEqualOldDim(long[] oldDim, long[] newDim) {
        if (oldDim.length > newDim.length) {
            throw new IllegalArgumentException(
                    "New image dimension for resizing must be greater than equal old dimension.");
        }

        if (IntStream.range(0, oldDim.length).anyMatch((i) -> oldDim[i] < newDim[i])) {
            throw new IllegalArgumentException(
                    "New image dimension for resizing must be greater than equal old dimension.");
        }
    }

    /**
     * Create metdata of the reassigned image. If an axis does not exist, set it to
     * one.
     * 
     * @param metadata
     * @return
     */
    private static IMetadata _createXYCZTMetadata(IMetadata metadata) {
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

    private static <T extends PixelType> void _copyImagePixelsToXYCZTImage(Image<T> image, Image<T> reassignedImage) {
        AxisOrder oldAxisOrder = image.getMetadata().axisOrder();
        IPixelRandomAccess<T> ra = reassignedImage.getRandomAccess();
        for (IPixelCursor<T> cursor = image.getCursor(); cursor.hasNext();) {
            IPixel<T> pixel = cursor.next();
            long[] position_xyczt = _convertPositionToXYCZT(cursor.localize(), oldAxisOrder);

            ra.setPosition(position_xyczt);
            ra.setPixel(pixel);
        }
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