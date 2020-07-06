package fr.fresnel.fourPolar.core.image.generic.AWTModel;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.IPixelRandomAccess;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.Pixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelTypes;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;

public class ImageToAWTBufferedImageConverterTest {
    @Test
    public void convert_UINT16AWTBufferedImage_ReturnsTheSameImageReference() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 1, 1 }).axisOrder(AxisOrder.XY)
                .bitPerPixel(PixelTypes.UINT_16).build();

        Image<UINT16> image = new AWTBufferedImageFactory().create(metadata, UINT16.zero());
        Image<UINT16> convertedImage = ImageToAWTBufferedImageConverter.convert(image, UINT16.zero());

        assertTrue(image == convertedImage);
    }

    @Test
    public void convert_UINT16ImgLib2Image_ConvertedImageIsEqualToOriginalImage() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 1, 1 }).axisOrder(AxisOrder.XY)
                .bitPerPixel(PixelTypes.UINT_16).build();

        Image<UINT16> image = new ImgLib2ImageFactory().create(metadata, UINT16.zero());
        IPixelRandomAccess<UINT16> ra = image.getRandomAccess();
        long[] pos00 = new long[] { 0, 0 };
        int val00 = 1;
        _setPixel(ra, pos00, val00);

        Image<UINT16> convertedImage = ImageToAWTBufferedImageConverter.convert(image, UINT16.zero());
        assertTrue(_getPixel(convertedImage.getRandomAccess(), new long[]{0,0}) == 1);
    }

    private void _setPixel(IPixelRandomAccess<UINT16> ra, long[] position, int value) {
        ra.setPosition(position);
        ra.setPixel(new Pixel<UINT16>(new UINT16(value)));
    }

    private int _getPixel(IPixelRandomAccess<UINT16> ra, long[] position) {
        ra.setPosition(position);
        return ra.getPixel().value().get();
    }

}