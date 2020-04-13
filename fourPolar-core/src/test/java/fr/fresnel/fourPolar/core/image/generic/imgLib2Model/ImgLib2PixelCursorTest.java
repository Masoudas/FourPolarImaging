package fr.fresnel.fourPolar.core.image.generic.imgLib2Model;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.Pixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;

public class ImgLib2PixelCursorTest {
    @Test
    public void localize_UINT16Image_ReturnsAllPositionsInTheImage() {
        long[] dimensions = new long[] { 2, 2 };
        Image<UINT16> image = new ImgLib2ImageFactory().create(dimensions, UINT16.zero());

        IPixelCursor<UINT16> cursor = image.getCursor();

        List<long[]> list = new ArrayList<>();

        while (cursor.hasNext()) {
            cursor.next();
            list.add(cursor.localize());
        }

        assertArrayEquals(new long[] { 0, 0 }, list.get(0));
        assertArrayEquals(new long[] { 1, 0 }, list.get(1));
        assertArrayEquals(new long[] { 0, 1 }, list.get(2));
        assertArrayEquals(new long[] { 1, 1 }, list.get(3));

    }

    @Test
    public void setPixel_UINT16Image_SetsPixelsToDefinedValues() {
        long[] dimensions = new long[] { 2, 2 };
        Image<UINT16> image = new ImgLib2ImageFactory().create(dimensions, UINT16.zero());
        IPixelCursor<UINT16> cursor = image.getCursor();

        int value = 1;
        while (cursor.hasNext()) {
            cursor.next();
            cursor.setPixel(new Pixel<UINT16>(new UINT16(value)));
            value++;
        }

        cursor.reset();
        value = 1;
        boolean equals = true;
        while (cursor.hasNext()) {
            equals = cursor.next().value().get() == value++;
        }

        assertTrue(equals);
    }

    @Test
    public void setPixel_Float32Image_SetsPixelsToDefinedValues() {
        long[] dimensions = new long[] { 2, 2 };
        Image<Float32> image = new ImgLib2ImageFactory().create(dimensions, Float32.zero());
        IPixelCursor<Float32> cursor = image.getCursor();

        float value = 1.1f;
        while (cursor.hasNext()) {
            cursor.next();
            cursor.setPixel(new Pixel<Float32>(new Float32(value)));
            value++;
        }

        cursor.reset();
        value = 1.1f;
        boolean equals = true;
        while (cursor.hasNext()) {
            equals = cursor.next().value().get() == value++;
        }

        assertTrue(equals);
    }

    @Test
    public void setPixel_RGB16Image_SetsPixelsToDefinedValues() {
        long[] dimensions = new long[] { 2, 2 };
        Image<RGB16> image = new ImgLib2ImageFactory().create(dimensions, RGB16.zero());
        IPixelCursor<RGB16> cursor = image.getCursor();

        int value = 0;
        while (cursor.hasNext()) {
            cursor.next();
            cursor.setPixel(new Pixel<RGB16>(new RGB16(++value, 0, 0)));
        }

        boolean equals = true;
        cursor.reset();
        value = 0;
        while (cursor.hasNext()) {
            equals = cursor.next().value().getR() == ++value;
        }

        assertTrue(equals);
    }

}