package fr.fresnel.fourPolar.core.image.generic.AWTModel;

import java.util.Objects;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.util.image.ImageUtil;

/**
 * Provides static methods for Converting the given {@link Image} implementation
 * to {@link AWTBufferedImage}.
 */
public class ImageToAWTBufferedImageConverter {
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

    private static <T extends PixelType> Image<T> _createBufferedImageOfSameSize(IMetadata metadata, T pixelType) {
        return new AWTBufferedImageFactory().create(metadata, pixelType);
    }

    private static <T extends PixelType> void _copyImageToBuffuredImage(Image<T> src, Image<T> dest) {
        ImageUtil.copy(src, dest);
    }
}