package fr.fresnel.fourPolar.core.image.generic.imgLib2Model;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.IPixelRandomAccess;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.Pixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.ARGB8;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;

public class ImgLib2PixelRandomAccessTest {
    @Test
    public void setPixel_UINT16Image_SetsPixelsToDefinedValues() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2 }).axisOrder(AxisOrder.XY).build();

        Image<UINT16> image = new ImgLib2ImageFactory().create(metadata, UINT16.zero());
        IPixelRandomAccess<UINT16> rAccess = image.getRandomAccess();

        int pixelValue = 0;
        rAccess.setPosition(new long[] { 0, 0 });
        rAccess.setPixel(new Pixel<UINT16>(new UINT16(++pixelValue)));

        rAccess.setPosition(new long[] { 1, 0 });
        rAccess.setPixel(new Pixel<UINT16>(new UINT16(++pixelValue)));

        rAccess.setPosition(new long[] { 0, 1 });
        rAccess.setPixel(new Pixel<UINT16>(new UINT16(++pixelValue)));

        rAccess.setPosition(new long[] { 1, 1 });
        rAccess.setPixel(new Pixel<UINT16>(new UINT16(++pixelValue)));

        boolean equals = true;
        rAccess.setPosition(new long[] { 0, 0 });
        equals = rAccess.getPixel().value().get() == 1;

        rAccess.setPosition(new long[] { 1, 0 });
        equals = rAccess.getPixel().value().get() == 2;

        rAccess.setPosition(new long[] { 0, 1 });
        equals = rAccess.getPixel().value().get() == 3;

        rAccess.setPosition(new long[] { 1, 1 });
        equals = rAccess.getPixel().value().get() == 4;

        assertTrue(equals);
    }

    @Test
    public void setPixel_Float32Image_SetsPixelsToDefinedValues() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2 }).axisOrder(AxisOrder.XY).build();
        Image<Float32> image = new ImgLib2ImageFactory().create(metadata, Float32.zero());

        IPixelRandomAccess<Float32> rAccess = image.getRandomAccess();

        float pixelValue = 0.1f;
        rAccess.setPosition(new long[] { 0, 0 });
        rAccess.setPixel(new Pixel<Float32>(new Float32(++pixelValue)));

        rAccess.setPosition(new long[] { 1, 0 });
        rAccess.setPixel(new Pixel<Float32>(new Float32(++pixelValue)));

        rAccess.setPosition(new long[] { 0, 1 });
        rAccess.setPixel(new Pixel<Float32>(new Float32(++pixelValue)));

        rAccess.setPosition(new long[] { 1, 1 });
        rAccess.setPixel(new Pixel<Float32>(new Float32(++pixelValue)));

        boolean equals = true;
        rAccess.setPosition(new long[] { 0, 0 });
        equals = rAccess.getPixel().value().get() == 1.1f;

        rAccess.setPosition(new long[] { 1, 0 });
        equals = rAccess.getPixel().value().get() == 2.1f;

        rAccess.setPosition(new long[] { 0, 1 });
        equals = rAccess.getPixel().value().get() == 3.1f;

        rAccess.setPosition(new long[] { 1, 1 });
        equals = rAccess.getPixel().value().get() == 4.1f;

        assertTrue(equals);
    }

    @Test
    public void setPixel_RGB16Image_SetsPixelsToDefinedValues() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2 }).axisOrder(AxisOrder.XY).build();
        Image<ARGB8> image = new ImgLib2ImageFactory().create(metadata, ARGB8.zero());

        IPixelRandomAccess<ARGB8> rAccess = image.getRandomAccess();

        int pixelValue = 0;
        rAccess.setPosition(new long[] { 0, 0 });
        rAccess.setPixel(new Pixel<ARGB8>(new ARGB8(++pixelValue, 0, 0)));

        rAccess.setPosition(new long[] { 1, 0 });
        rAccess.setPixel(new Pixel<ARGB8>(new ARGB8(++pixelValue, 0, 0)));

        rAccess.setPosition(new long[] { 0, 1 });
        rAccess.setPixel(new Pixel<ARGB8>(new ARGB8(++pixelValue, 0, 0)));

        rAccess.setPosition(new long[] { 1, 1 });
        rAccess.setPixel(new Pixel<ARGB8>(new ARGB8(++pixelValue, 0, 0)));

        boolean equals = true;
        rAccess.setPosition(new long[] { 0, 0 });
        equals = rAccess.getPixel().value().getR() == 1;

        rAccess.setPosition(new long[] { 1, 0 });
        equals = rAccess.getPixel().value().getR() == 2;

        rAccess.setPosition(new long[] { 0, 1 });
        equals = rAccess.getPixel().value().getR() == 3;

        rAccess.setPosition(new long[] { 1, 1 });
        equals = rAccess.getPixel().value().getR() == 4;

        assertTrue(equals);
    }

    @Test
    public void getPixel_OutOfBoundPixel_ThrowsArrayIndexOutOfBound() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2 }).axisOrder(AxisOrder.XY).build();
        Image<UINT16> image = new ImgLib2ImageFactory().create(metadata, UINT16.zero());

        IPixelRandomAccess<UINT16> rAccess = image.getRandomAccess();

        rAccess.setPosition(new long[] { 2, 2 });
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            rAccess.getPixel();
        });

    }

    @Test
    public void setPixel_OutOfBoundPixel_ThrowsArrayIndexOutOfBound() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2 }).axisOrder(AxisOrder.XY).build();
        Image<UINT16> image = new ImgLib2ImageFactory().create(metadata, UINT16.zero());

        IPixelRandomAccess<UINT16> rAccess = image.getRandomAccess();

        rAccess.setPosition(new long[] { 2, 2 });
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            rAccess.setPixel(new Pixel<UINT16>(UINT16.zero()));
        });

    }

}