package fr.fresnel.fourPolar.core.image.generic.AWTModel;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.IPixelRandomAccess;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.IPixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.Pixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelTypes;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.util.image.generic.ImageUtil;

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
        assertTrue(_getPixel(convertedImage.getRandomAccess(), new long[] { 0, 0 }) == 1);
    }

    @Test
    public void convertPlane_2by2UINT16ImgLib2ImageFirstPlane_ReturnsCorrectImagePlane() {
        int planeIndex = 1;
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2 }).axisOrder(AxisOrder.XY)
                .bitPerPixel(PixelTypes.UINT_16).build();

        Image<UINT16> image = new ImgLib2ImageFactory().create(metadata, UINT16.zero());
        _setPlaneToValue(image, planeIndex, 4);

        AWTBufferedImage<UINT16> imagePlane = ImageToAWTBufferedImageConverter.convertPlane(image, UINT16.zero(),
                planeIndex);
        assertTrue(_cursorAndRAOfPlaneHaveSameValue(image, planeIndex, imagePlane));

    }

    @Test
    public void convertPlane_2by2by3UINT16ImgLib2ImageThirdPlane_ReturnsCorrectImagePlane() {
        int planeIndex = 3;
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2, 3 }).axisOrder(AxisOrder.XYZ)
                .bitPerPixel(PixelTypes.UINT_16).build();

        Image<UINT16> image = new ImgLib2ImageFactory().create(metadata, UINT16.zero());
        _setPlaneToValue(image, planeIndex, 4);

        AWTBufferedImage<UINT16> imagePlane = ImageToAWTBufferedImageConverter.convertPlane(image, UINT16.zero(),
                planeIndex);
        assertTrue(_cursorAndRAOfPlaneHaveSameValue(image, planeIndex, imagePlane));

    }

    @Test
    public void convertPlane_2by2by3by4UINT16ImgLib2ImageFifthPlane_ReturnsCorrectImagePlane() {
        int planeIndex = 5;
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2, 3, 4 }).axisOrder(AxisOrder.XYZT)
                .bitPerPixel(PixelTypes.UINT_16).build();

        Image<UINT16> image = new ImgLib2ImageFactory().create(metadata, UINT16.zero());
        _setPlaneToValue(image, planeIndex, 4);

        AWTBufferedImage<UINT16> imagePlane = ImageToAWTBufferedImageConverter.convertPlane(image, UINT16.zero(),
                planeIndex);
        assertTrue(_cursorAndRAOfPlaneHaveSameValue(image, planeIndex, imagePlane));

    }

    private void _setPixel(IPixelRandomAccess<UINT16> ra, long[] position, int value) {
        ra.setPosition(position);
        ra.setPixel(new Pixel<UINT16>(new UINT16(value)));
    }

    private int _getPixel(IPixelRandomAccess<UINT16> ra, long[] position) {
        ra.setPosition(position);
        return ra.getPixel().value().get();
    }

    private void _setPlaneToValue(Image<UINT16> image, long planeIndex, int value) {
        IPixelCursor<UINT16> pixelCursor = ImageUtil.getPlaneCursor(image, planeIndex);
        while (pixelCursor.hasNext()) {
            IPixel<UINT16> pixel = pixelCursor.next();
            pixel.value().set(value);
            pixelCursor.setPixel(pixel);
        }
    }

    private boolean _cursorAndRAOfPlaneHaveSameValue(Image<UINT16> srcImg, long planeIndex,
            AWTBufferedImage<UINT16> destImg) {
        IPixelCursor<UINT16> cursor = ImageUtil.getPlaneCursor(srcImg, planeIndex);
        IPixelRandomAccess<UINT16> ra = destImg.getRandomAccess();
        if (!cursor.hasNext()) {
            return false;
        }

        boolean pixelEqual = true;
        while (cursor.hasNext() && pixelEqual) {
            IPixel<UINT16> pixel = cursor.next();
            long[] position = cursor.localize();
            ra.setPosition(new long[] { position[0], position[1] });
            pixelEqual = pixel.value().get() == ra.getPixel().value().get();
        }

        return pixelEqual;
    }

}