package fr.fresnel.fourPolar.core.image.generic.AWTModel;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.IPixelRandomAccess;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.Pixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelTypes;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;

public class UINT16PixelRandomAccessTest {
    @Test
    public void getPixel_22XYImage_ReturnsZeroForDefaultImageValues() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2 }).axisOrder(AxisOrder.XY)
                .bitPerPixel(PixelTypes.UINT_16).build();
        UINT16BufferedImage image = new UINT16BufferedImage(metadata, new U16BIDummyImageFactory(), UINT16.zero());

        IPixelRandomAccess<UINT16> ra = image.getRandomAccess();
        assertTrue(_getPixel(ra, new long[] { 0, 0 }) == 0);
        assertTrue(_getPixel(ra, new long[] { 0, 1 }) == 0);
        assertTrue(_getPixel(ra, new long[] { 1, 0 }) == 0);
        assertTrue(_getPixel(ra, new long[] { 1, 1 }) == 0);
    }

    @Test
    public void getPixel_222XYZImage_ReturnsZeroForDefaultImageValues() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2, 2 }).axisOrder(AxisOrder.XYZ)
                .bitPerPixel(PixelTypes.UINT_16).build();

        UINT16BufferedImage image = new UINT16BufferedImage(metadata, new U16BIDummyImageFactory(), UINT16.zero());

        IPixelRandomAccess<UINT16> ra = image.getRandomAccess();
        assertTrue(_getPixel(ra, new long[] { 0, 0, 0 }) == 0);
        assertTrue(_getPixel(ra, new long[] { 0, 1, 0 }) == 0);
        assertTrue(_getPixel(ra, new long[] { 1, 0, 0 }) == 0);
        assertTrue(_getPixel(ra, new long[] { 1, 1, 0 }) == 0);
        assertTrue(_getPixel(ra, new long[] { 0, 0, 1 }) == 0);
        assertTrue(_getPixel(ra, new long[] { 0, 1, 1 }) == 0);
        assertTrue(_getPixel(ra, new long[] { 1, 0, 1 }) == 0);
        assertTrue(_getPixel(ra, new long[] { 1, 1, 1 }) == 0);
    }

    @Test
    public void getPixel_22112XYCZTImage_ReturnsZeroForDefaultImageValues() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2, 1, 1, 2 }).axisOrder(AxisOrder.XYZCT)
                .bitPerPixel(PixelTypes.UINT_16).build();

        UINT16BufferedImage image = new UINT16BufferedImage(metadata, new U16BIDummyImageFactory(), UINT16.zero());

        IPixelRandomAccess<UINT16> ra = image.getRandomAccess();
        assertTrue(_getPixel(ra, new long[] { 0, 0, 0, 0, 0 }) == 0);
        assertTrue(_getPixel(ra, new long[] { 0, 1, 0, 0, 0 }) == 0);
        assertTrue(_getPixel(ra, new long[] { 1, 0, 0, 0, 0 }) == 0);
        assertTrue(_getPixel(ra, new long[] { 1, 1, 0, 0, 0 }) == 0);
        assertTrue(_getPixel(ra, new long[] { 0, 0, 0, 0, 1 }) == 0);
        assertTrue(_getPixel(ra, new long[] { 0, 1, 0, 0, 1 }) == 0);
        assertTrue(_getPixel(ra, new long[] { 1, 0, 0, 0, 1 }) == 0);
        assertTrue(_getPixel(ra, new long[] { 1, 1, 0, 0, 1 }) == 0);
    }

    @Test
    public void setPixel_22XYImage_ReturnsCorrectPixelValueForEachPosition() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2 }).axisOrder(AxisOrder.XY)
                .bitPerPixel(PixelTypes.UINT_16).build();
        UINT16BufferedImage image = new UINT16BufferedImage(metadata, new U16BIDummyImageFactory(), UINT16.zero());

        IPixelRandomAccess<UINT16> ra = image.getRandomAccess();
        long[] pos00 = new long[] { 0, 0 };
        int val00 = 1;
        _setPixel(ra, pos00, val00);

        long[] pos01 = new long[] { 0, 1 };
        int val01 = 2;
        _setPixel(ra, pos01, val01);

        long[] pos10 = new long[] { 1, 0 };
        int val10 = 3;
        _setPixel(ra, pos10, val10);

        long[] pos11 = new long[] { 1, 1 };
        int val11 = 4;
        _setPixel(ra, pos11, val11);

        assertTrue(_getPixel(ra, pos00) == 1);
        assertTrue(_getPixel(ra, pos01) == 2);
        assertTrue(_getPixel(ra, pos10) == 3);
        assertTrue(_getPixel(ra, pos11) == 4);

    }

    @Test
    public void setPixel_222XYZImage_ReturnsCorrectPixelValueForEachPosition() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2, 2 }).axisOrder(AxisOrder.XYZ)
                .bitPerPixel(PixelTypes.UINT_16).build();
        UINT16BufferedImage image = new UINT16BufferedImage(metadata, new U16BIDummyImageFactory(), UINT16.zero());

        IPixelRandomAccess<UINT16> ra = image.getRandomAccess();
        long[] pos000 = new long[] { 0, 0, 0 };
        int val000 = 1;
        _setPixel(ra, pos000, val000);

        long[] pos010 = new long[] { 0, 1, 0 };
        int val010 = 2;
        _setPixel(ra, pos010, val010);

        long[] pos100 = new long[] { 1, 0, 0 };
        int val100 = 3;
        _setPixel(ra, pos100, val100);

        long[] pos110 = new long[] { 1, 1, 0 };
        int val110 = 4;
        _setPixel(ra, pos110, val110);

        long[] pos001 = new long[] { 0, 0, 1 };
        int val001 = 5;
        _setPixel(ra, pos001, val001);

        long[] pos011 = new long[] { 0, 1, 1 };
        int val011 = 6;
        _setPixel(ra, pos011, val011);

        long[] pos101 = new long[] { 1, 0, 1 };
        int val101 = 7;
        _setPixel(ra, pos101, val101);

        long[] pos111 = new long[] { 1, 1, 1 };
        int val111 = 8;
        _setPixel(ra, pos111, val111);

        assertTrue(_getPixel(ra, pos000) == 1);
        assertTrue(_getPixel(ra, pos010) == 2);
        assertTrue(_getPixel(ra, pos100) == 3);
        assertTrue(_getPixel(ra, pos110) == 4);
        assertTrue(_getPixel(ra, pos001) == 5);
        assertTrue(_getPixel(ra, pos011) == 6);
        assertTrue(_getPixel(ra, pos101) == 7);
        assertTrue(_getPixel(ra, pos111) == 8);

    }

    @Test
    public void setPixel_22112XYCZTImage_ReturnsCorrectPixelValueForEachPosition() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2, 1, 1, 2 }).axisOrder(AxisOrder.XYCZT)
                .bitPerPixel(PixelTypes.UINT_16).build();
        UINT16BufferedImage image = new UINT16BufferedImage(metadata, new U16BIDummyImageFactory(), UINT16.zero());

        IPixelRandomAccess<UINT16> ra = image.getRandomAccess();
        long[] pos00000 = new long[] { 0, 0, 0, 0, 0 };
        int val00000 = 1;
        _setPixel(ra, pos00000, val00000);

        long[] pos01000 = new long[] { 0, 1, 0, 0, 0 };
        int val01000 = 2;
        _setPixel(ra, pos01000, val01000);

        long[] pos10000 = new long[] { 1, 0, 0, 0, 0 };
        int val10000 = 3;
        _setPixel(ra, pos10000, val10000);

        long[] pos11000 = new long[] { 1, 1, 0, 0, 0 };
        int val11000 = 4;
        _setPixel(ra, pos11000, val11000);

        long[] pos00101 = new long[] { 0, 0, 0, 0, 1 };
        int val00101 = 5;
        _setPixel(ra, pos00101, val00101);

        long[] pos01001 = new long[] { 0, 1, 0, 0, 1 };
        int val01001 = 6;
        _setPixel(ra, pos01001, val01001);

        long[] pos10001 = new long[] { 1, 0, 0, 0, 1 };
        int val10001 = 7;
        _setPixel(ra, pos10001, val10001);

        long[] pos11001 = new long[] { 1, 1, 0, 0, 1 };
        int val11001 = 8;
        _setPixel(ra, pos11001, val11001);

        assertTrue(_getPixel(ra, pos00000) == 1);
        assertTrue(_getPixel(ra, pos01000) == 2);
        assertTrue(_getPixel(ra, pos10000) == 3);
        assertTrue(_getPixel(ra, pos11000) == 4);
        assertTrue(_getPixel(ra, pos00101) == 5);
        assertTrue(_getPixel(ra, pos01001) == 6);
        assertTrue(_getPixel(ra, pos10001) == 7);
        assertTrue(_getPixel(ra, pos11001) == 8);

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

class U16PRADummyImageFactory implements ImageFactory {

    @Override
    public <T extends PixelType> Image<T> create(IMetadata metadata, T pixelType) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Not defined");
    }

}