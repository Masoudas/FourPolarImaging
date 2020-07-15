package fr.fresnel.fourPolar.core.image.generic.AWTModel;

import java.util.Objects;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.IPixelRandomAccess;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.IPixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.util.image.ImageUtil;
import fr.fresnel.fourPolar.core.util.image.metadata.MetadataUtil;

/**
 * Provides static methods for Converting the given {@link Image} implementation
 * to {@link AWTBufferedImage}.
 */
public class ImageToAWTBufferedImageConverter {
    private static final AWTBufferedImageFactory _BUFFERED_IMAGE_FACTORY = new AWTBufferedImageFactory();

    private ImageToAWTBufferedImageConverter() {
        throw new AssertionError();
    }

    /**
     * Convert the given image interface. If image is already of the buffered image
     * type, just returns the backend implementation, otherwise creates a new
     * buffered image and returns it.
     * 
     * @param image     is the image to be converted.
     * @param pixelType is the pixel type of the image.
     * @return a buffered image type.
     */
    public static AWTBufferedImage<UINT16> convert(Image<UINT16> image, UINT16 pixelType) {
        Objects.requireNonNull(image, "image can't be null.");
        Objects.requireNonNull(pixelType, "pixelType can't be null.");

        if (image instanceof AWTBufferedImage) {
            return (AWTBufferedImage<UINT16>) image;
        } else {
            Image<UINT16> uint16BufferedImage = _createBufferedImageOfSameSize(image.getMetadata(), pixelType);
            _copyImageToBuffuredImage(image, uint16BufferedImage);
            return (AWTBufferedImage<UINT16>) uint16BufferedImage;
        }

    }

    /**
     * Converts the demanded plane to and awt buffered image.
     * 
     * @param image      is the image.
     * @param pixelType  is the pixel type of the image.
     * @param planeIndex is the plane index we wish to convert.
     * 
     * @return an awt buffered image implementation of {@link Image}.
     * @throws IndexOutOfBounds if the plane index is less than one or greater than
     *                          number of planes.
     */
    public static AWTBufferedImage<UINT16> convertPlane(Image<UINT16> image, UINT16 pixelType, int planeIndex) {
        long[] planeDim = MetadataUtil.getPlaneDim(image.getMetadata());
        AxisOrder planeAxisOrder = AxisOrder.planeAxisOrder(image.getMetadata().axisOrder());

        IMetadata metadata = new Metadata.MetadataBuilder(planeDim).axisOrder(planeAxisOrder)
                .bitPerPixel(pixelType.getType()).build();
        Image<UINT16> bufferedImg = _BUFFERED_IMAGE_FACTORY.create(metadata, pixelType);

        IPixelRandomAccess<UINT16> bufferedImgRA = bufferedImg.getRandomAccess();
        for (IPixelCursor<UINT16> planeCursor = ImageUtil.getPlaneCursor(image, planeIndex); planeCursor.hasNext();) {
            IPixel<UINT16> pixel = planeCursor.next();
            bufferedImgRA.setPosition(planeCursor.localize());
            bufferedImgRA.setPixel(pixel);
        }

        return (AWTBufferedImage<UINT16>)bufferedImg;

    }

    private static <T extends PixelType> Image<T> _createBufferedImageOfSameSize(IMetadata metadata, T pixelType) {
        return new AWTBufferedImageFactory().create(metadata, pixelType);
    }

    private static <T extends PixelType> void _copyImageToBuffuredImage(Image<T> src, Image<T> dest) {
        ImageUtil.copy(src, dest);
    }
}