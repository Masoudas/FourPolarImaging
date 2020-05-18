package fr.fresnel.fourPolar.algorithm.util.image.axis;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;

public class ReassignerToXYCZTTest {
    @Test
    public void reassign_XYImage_ReturnsCorrectXYCZTImage() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2 }).axisOrder(AxisOrder.XY).build();
        Image<UINT16> img = new ImgLib2ImageFactory().create(metadata, UINT16.zero());

        int val = 0;
        for (IPixelCursor<UINT16> cursor = img.getCursor(); cursor.hasNext();) {
            cursor.next();
            cursor.setPixel(new Pixel<UINT16>(new UINT16(val++)));
        }

        Image<UINT16> reassignedImg = new ReassingerToXYCZT().reassign(img, UINT16.zero());
        IPixelRandomAccess<UINT16> ra = reassignedImg.getRandomAccess();
        for (IPixelCursor<UINT16> cursor = img.getCursor(); cursor.hasNext();) {
            IPixel<UINT16> pixel = cursor.next();

            long[] position = cursor.localize();
            ra.setPosition(new long[] { position[0], position[1], 0, 0, 0 });

            assertTrue(ra.getPixel().value().get() == pixel.value().get());
        }

        assertArrayEquals(reassignedImg.getMetadata().getDim(), new long[] { 2, 2, 1, 1, 1 });
    }

    @Test
    public void reassign_XYZImage_ReturnsCorrectXYCZTImage() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2, 2 }).axisOrder(AxisOrder.XYZ).build();
        Image<UINT16> img = new ImgLib2ImageFactory().create(metadata, UINT16.zero());

        int val = 0;
        for (IPixelCursor<UINT16> cursor = img.getCursor(); cursor.hasNext();) {
            cursor.next();
            cursor.setPixel(new Pixel<UINT16>(new UINT16(val++)));
        }

        Image<UINT16> reassignedImg = new ReassingerToXYCZT().reassign(img, UINT16.zero());
        assertArrayEquals(reassignedImg.getMetadata().getDim(), new long[] { 2, 2, 1, 2, 1 });

        IPixelRandomAccess<UINT16> ra = reassignedImg.getRandomAccess();
        for (IPixelCursor<UINT16> cursor = img.getCursor(); cursor.hasNext();) {
            IPixel<UINT16> pixel = cursor.next();

            long[] position = cursor.localize();
            ra.setPosition(new long[] { position[0], position[1], 0, position[2], 0 });

            assertTrue(ra.getPixel().value().get() == pixel.value().get());
        }

    }

    @Test
    public void reassign_XYZTImage_ReturnsCorrectXYCZTImage() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2, 1, 3 }).axisOrder(AxisOrder.XYZT).build();
        Image<UINT16> img = new ImgLib2ImageFactory().create(metadata, UINT16.zero());

        int val = 0;
        for (IPixelCursor<UINT16> cursor = img.getCursor(); cursor.hasNext();) {
            cursor.next();
            cursor.setPixel(new Pixel<UINT16>(new UINT16(val++)));
        }

        Image<UINT16> reassignedImg = new ReassingerToXYCZT().reassign(img, UINT16.zero());
        assertArrayEquals(reassignedImg.getMetadata().getDim(), new long[] { 2, 2, 1, 1, 3 });

        IPixelRandomAccess<UINT16> ra = reassignedImg.getRandomAccess();
        for (IPixelCursor<UINT16> cursor = img.getCursor(); cursor.hasNext();) {
            IPixel<UINT16> pixel = cursor.next();

            long[] position = cursor.localize();
            ra.setPosition(new long[] { position[0], position[1], 0, position[2], position[3] });

            assertTrue(ra.getPixel().value().get() == pixel.value().get());
        }

    }

    @Test
    public void reassign_XYCZTImage_ReturnsCorrectXYCZTImage() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2, 2, 2, 3 }).axisOrder(AxisOrder.XYCZT)
                .build();
        Image<UINT16> img = new ImgLib2ImageFactory().create(metadata, UINT16.zero());

        int val = 0;
        for (IPixelCursor<UINT16> cursor = img.getCursor(); cursor.hasNext();) {
            cursor.next();
            cursor.setPixel(new Pixel<UINT16>(new UINT16(val++)));
        }

        Image<UINT16> reassignedImg = new ReassingerToXYCZT().reassign(img, UINT16.zero());
        assertArrayEquals(reassignedImg.getMetadata().getDim(), new long[] { 2, 2, 2, 2, 3 });

        IPixelRandomAccess<UINT16> ra = reassignedImg.getRandomAccess();
        for (IPixelCursor<UINT16> cursor = img.getCursor(); cursor.hasNext();) {
            IPixel<UINT16> pixel = cursor.next();

            long[] position = cursor.localize();
            ra.setPosition(new long[] { position[0], position[1], position[2], position[3], position[4] });

            assertTrue(ra.getPixel().value().get() == pixel.value().get());
        }

    }
}