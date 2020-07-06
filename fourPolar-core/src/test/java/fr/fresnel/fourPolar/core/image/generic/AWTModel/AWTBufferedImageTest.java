package fr.fresnel.fourPolar.core.image.generic.AWTModel;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.IPixelRandomAccess;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelTypes;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;

/**
 * Dedicated to testing the non-abstract methods of the class. Note that all
 * methods of this are independent of {@link PixelType} and AWT buffered image
 * types.
 */
public class AWTBufferedImageTest {
    @Test
    public void getPlaneNumber_XYImage_Return1ForPosition00() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 1, 1 }).axisOrder(AxisOrder.XY)
                .bitPerPixel(PixelTypes.UINT_16).build();

        DummyAWTBufferedImage<?> image = new DummyAWTBufferedImage<>(metadata, UINT16.zero());

        int planeNo = image.getPlaneNumber(new long[] { 0, 0 });

        assertTrue(planeNo == 1);
    }

    @Test
    public void getPlaneNumber_XYZImage_Return3ForPosition002() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 1, 1, 3 }).axisOrder(AxisOrder.XYZ)
                .bitPerPixel(PixelTypes.UINT_16).build();

        DummyAWTBufferedImage<?> image = new DummyAWTBufferedImage<>(metadata, UINT16.zero());

        int planeNo = image.getPlaneNumber(new long[] { 0, 0, 2 });

        assertTrue(planeNo == 3);
    }

    @Test
    public void getPlaneNumber_XYZTImage_Return3ForPosition0020() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2, 3, 3 }).axisOrder(AxisOrder.XYZT)
                .bitPerPixel(PixelTypes.UINT_16).build();

        DummyAWTBufferedImage<?> image = new DummyAWTBufferedImage<>(metadata, UINT16.zero());

        int planeNo = image.getPlaneNumber(new long[] { 0, 0, 2, 0 });

        assertTrue(planeNo == 3);
    }

    @Test
    public void getPlaneNumber_XYZTImage_Return0ForPosition0022() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2, 3, 3 }).axisOrder(AxisOrder.XYZT)
                .bitPerPixel(PixelTypes.UINT_16).build();

        DummyAWTBufferedImage<?> image = new DummyAWTBufferedImage<>(metadata, UINT16.zero());

        int planeNo = image.getPlaneNumber(new long[] { 0, 0, 2, 2 });

        assertTrue(planeNo == 9);
    }

    @Test
    public void getPlaneNumber_XYZCTImage_Return2ForPosition00001() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 1, 1, 1, 1, 3 }).axisOrder(AxisOrder.XYCZT)
                .bitPerPixel(PixelTypes.UINT_16).build();

        DummyAWTBufferedImage<?> image = new DummyAWTBufferedImage<>(metadata, UINT16.zero());

        int planeNo = image.getPlaneNumber(new long[] { 0, 0, 0, 0, 2 });
        
        assertTrue(planeNo == 3);
    }

}

class DummyAWTBufferedImage<T extends PixelType> extends AWTBufferedImage<T> {
    DummyAWTBufferedImage(IMetadata metadata, T type) {
        super(metadata, new DummyImageFactory(), type);
    }

    @Override
    public IPixelCursor<T> getCursor() {
        throw new UnsupportedOperationException("Not defined");
    }

    @Override
    public IPixelCursor<T> getCursor(long[] bottomCorner, long[] len) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Not defined");
    }

    @Override
    public IPixelRandomAccess<T> getRandomAccess() {
        throw new UnsupportedOperationException("Not defined");
    }

}

class DummyImageFactory implements ImageFactory {

    @Override
    public <T extends PixelType> Image<T> create(IMetadata metadata, T pixelType) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Not defined");
    }

}