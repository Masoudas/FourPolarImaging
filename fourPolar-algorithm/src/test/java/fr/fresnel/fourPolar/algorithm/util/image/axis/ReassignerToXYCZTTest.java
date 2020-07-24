package fr.fresnel.fourPolar.algorithm.util.image.axis;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
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
import fr.fresnel.fourPolar.core.util.image.metadata.axis.ReassingerToXYCZT;

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

        Image<UINT16> reassignedImg = ReassingerToXYCZT.reassign(img, UINT16.zero());
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

        Image<UINT16> reassignedImg = ReassingerToXYCZT.reassign(img, UINT16.zero());
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

        Image<UINT16> reassignedImg = ReassingerToXYCZT.reassign(img, UINT16.zero());
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
    public void reassign_XYCZTImage_ReturnsCorrectXYZCTImage() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2, 2, 2, 3 }).axisOrder(AxisOrder.XYZCT)
                .build();
        Image<UINT16> img = new ImgLib2ImageFactory().create(metadata, UINT16.zero());

        int val = 0;
        for (IPixelCursor<UINT16> cursor = img.getCursor(); cursor.hasNext();) {
            cursor.next();
            cursor.setPixel(new Pixel<UINT16>(new UINT16(val++)));
        }

        Image<UINT16> reassignedImg = ReassingerToXYCZT.reassign(img, UINT16.zero());
        assertArrayEquals(reassignedImg.getMetadata().getDim(), new long[] { 2, 2, 2, 2, 3 });

        IPixelRandomAccess<UINT16> ra = reassignedImg.getRandomAccess();
        for (IPixelCursor<UINT16> cursor = img.getCursor(); cursor.hasNext();) {
            IPixel<UINT16> pixel = cursor.next();

            long[] position = cursor.localize();
            ra.setPosition(new long[] { position[0], position[1], position[3], position[2], position[4] });

            assertTrue(ra.getPixel().value().get() == pixel.value().get());
        }

    }

    @Test
    public void reassignAndResize_XYImageAddX_ReturnsCorrectXYCZTImage() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 1, 1 }).axisOrder(AxisOrder.XY).build();
        Image<UINT16> img = new ImgLib2ImageFactory().create(metadata, UINT16.zero());

        _setPixel(img, 1);

        Image<UINT16> reassignedImg = ReassingerToXYCZT.reassignAndResize(img, UINT16.zero(),
                new long[] { 2, 1, 1, 1, 1 });

        assertArrayEquals(reassignedImg.getMetadata().getDim(), new long[] { 2, 1, 1, 1, 1 });
        assertTrue(_checkPixel(reassignedImg, 1, new long[] { 0, 0, 0, 0, 0 }));
        assertTrue(_checkPixel(reassignedImg, 0, new long[] { 1, 0, 0, 0, 0 }));

    }

    @Test
    public void reassignAndResize_XYImageAddXAndY_ReturnsCorrectXYCZTImage() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 1, 2 }).axisOrder(AxisOrder.XY).build();
        Image<UINT16> img = new ImgLib2ImageFactory().create(metadata, UINT16.zero());

        _setPixel(img, 1);

        Image<UINT16> reassignedImg = ReassingerToXYCZT.reassignAndResize(img, UINT16.zero(),
                new long[] { 2, 2, 1, 1, 1 });

        assertArrayEquals(reassignedImg.getMetadata().getDim(), new long[] { 2, 2, 1, 1, 1 });
        assertTrue(_checkPixel(reassignedImg, 1, new long[] { 0, 0, 0, 0, 0 }));
        assertTrue(_checkPixel(reassignedImg, 0, new long[] { 1, 0, 0, 0, 0 }));
        assertTrue(_checkPixel(reassignedImg, 1, new long[] { 0, 1, 0, 0, 0 }));
        assertTrue(_checkPixel(reassignedImg, 0, new long[] { 1, 1, 0, 0, 0 }));
    }

    @Test
    public void reassignAndResize_XYZImageAddXAndY_ReturnsCorrectXYCZTImage() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 1, 1, 1 }).axisOrder(AxisOrder.XYZ).build();
        Image<UINT16> img = new ImgLib2ImageFactory().create(metadata, UINT16.zero());

        _setPixel(img, 1);

        Image<UINT16> reassignedImg = ReassingerToXYCZT.reassignAndResize(img, UINT16.zero(),
                new long[] { 2, 2, 1, 1, 1 });

        assertArrayEquals(reassignedImg.getMetadata().getDim(), new long[] { 2, 2, 1, 1, 1 });
        assertTrue(_checkPixel(reassignedImg, 1, new long[] { 0, 0, 0, 0, 0 }));
        assertTrue(_checkPixel(reassignedImg, 0, new long[] { 1, 0, 0, 0, 0 }));
        assertTrue(_checkPixel(reassignedImg, 0, new long[] { 0, 1, 0, 0, 0 }));
        assertTrue(_checkPixel(reassignedImg, 0, new long[] { 1, 1, 0, 0, 0 }));
    }

    @Test
    public void reassignAndResize_TrivialXYImage_ReturnsCorrectXYCZTImage() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2 }).axisOrder(AxisOrder.XY).build();
        Image<UINT16> img = new ImgLib2ImageFactory().create(metadata, UINT16.zero());

        long[] newDim = { 3, 3, 2, 1, 1 };

        Image<UINT16> reassignedImg = ReassingerToXYCZT.reassignAndResize(img, UINT16.zero(), newDim);
        for (IPixelCursor<UINT16> cursor = reassignedImg.getCursor(); cursor.hasNext();) {
            IPixel<UINT16> pixel = cursor.next();
            assertTrue(pixel.value().get() == 0);
        }

        assertArrayEquals(reassignedImg.getMetadata().getDim(), newDim);
    }

    @Test
    public void reassignAndResize_XYImage_ReturnsCorrectXYCZTImage() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2 }).axisOrder(AxisOrder.XY).build();
        Image<UINT16> img = new ImgLib2ImageFactory().create(metadata, UINT16.zero());

        int val = 0;
        for (IPixelCursor<UINT16> cursor = img.getCursor(); cursor.hasNext();) {
            cursor.next();
            cursor.setPixel(new Pixel<UINT16>(new UINT16(val++)));
        }

        // Check positions that are one the unresized image have the save value after
        // resize.
        long[] newDim = { 3, 3, 2, 1, 1 };
        Image<UINT16> reassignedImg = ReassingerToXYCZT.reassignAndResize(img, UINT16.zero(), newDim);

        IPixelRandomAccess<UINT16> ra = reassignedImg.getRandomAccess();
        for (IPixelCursor<UINT16> cursor = img.getCursor(); cursor.hasNext();) {
            IPixel<UINT16> pixel = cursor.next();

            long[] position = cursor.localize();
            ra.setPosition(new long[] { position[0], position[1], 0, 0, 0 });

            assertTrue(ra.getPixel().value().get() == pixel.value().get());
        }

        assertArrayEquals(reassignedImg.getMetadata().getDim(), newDim);
    }

    @Test
    public void reassignAndResize_XYZImage_ReturnsCorrectXYCZTImage() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2, 3 }).axisOrder(AxisOrder.XYZ).build();
        Image<UINT16> img = new ImgLib2ImageFactory().create(metadata, UINT16.zero());

        int val = 0;
        for (IPixelCursor<UINT16> cursor = img.getCursor(); cursor.hasNext();) {
            cursor.next();
            cursor.setPixel(new Pixel<UINT16>(new UINT16(val++)));
        }

        // Check positions that are one the unresized image have the save value after
        // resize.
        long[] newDim = { 3, 3, 2, 4, 1 };
        Image<UINT16> reassignedImg = ReassingerToXYCZT.reassignAndResize(img, UINT16.zero(), newDim);

        IPixelRandomAccess<UINT16> ra = reassignedImg.getRandomAccess();
        for (IPixelCursor<UINT16> cursor = img.getCursor(); cursor.hasNext();) {
            IPixel<UINT16> pixel = cursor.next();

            long[] position = cursor.localize();
            ra.setPosition(new long[] { position[0], position[1], 0, position[2], 0 });

            assertTrue(ra.getPixel().value().get() == pixel.value().get());
        }

        assertArrayEquals(reassignedImg.getMetadata().getDim(), newDim);
    }

    @Test
    public void reassignAndResize_XYCZTImage_ReturnsCorrectXYCZTImage() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2, 2, 1, 2 }).axisOrder(AxisOrder.XYCZT)
                .build();
        Image<UINT16> img = new ImgLib2ImageFactory().create(metadata, UINT16.zero());

        int val = 0;
        for (IPixelCursor<UINT16> cursor = img.getCursor(); cursor.hasNext();) {
            cursor.next();
            cursor.setPixel(new Pixel<UINT16>(new UINT16(val++)));
        }

        // Check positions that are one the unresized image have the save value after
        // resize.
        long[] newDim = { 3, 3, 2, 1, 2 };
        Image<UINT16> reassignedImg = ReassingerToXYCZT.reassignAndResize(img, UINT16.zero(), newDim);

        IPixelRandomAccess<UINT16> ra = reassignedImg.getRandomAccess();
        for (IPixelCursor<UINT16> cursor = img.getCursor(); cursor.hasNext();) {
            IPixel<UINT16> pixel = cursor.next();

            long[] position = cursor.localize();
            ra.setPosition(position);

            assertTrue(ra.getPixel().value().get() == pixel.value().get());
        }

        assertArrayEquals(reassignedImg.getMetadata().getDim(), newDim);
    }

    private void _setPixel(Image<UINT16> pol, int value) {
        for (IPixelCursor<UINT16> cursor = pol.getCursor(); cursor.hasNext();) {
            IPixel<UINT16> pixel = cursor.next();
            pixel.value().set(value);
            cursor.setPixel(pixel);
        }

    }

    private boolean _checkPixel(Image<UINT16> img, int value, long[] position) {
        IPixelRandomAccess<UINT16> ra = img.getRandomAccess();

        ra.setPosition(position);
        return ra.getPixel().value().get() == value;
    }

}