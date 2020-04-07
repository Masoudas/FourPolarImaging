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

        List<UINT16> pixels = new ArrayList<>();

        cursor.reset();

        while (cursor.hasNext()) {
            pixels.add(cursor.next().value());
        }

        assertTrue(pixels.get(0).get() == 1 && pixels.get(1).get() == 2 && pixels.get(2).get() == 3
                && pixels.get(3).get() == 4);
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

        List<Float32> pixels = new ArrayList<>();

        cursor.reset();

        while (cursor.hasNext()) {
            pixels.add(cursor.next().value());
        }

        assertTrue(pixels.get(0).get() == 1.1f && pixels.get(1).get() == 2.1f && pixels.get(2).get() == 3.1f
                && pixels.get(3).get() == 4.1f);
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

        List<RGB16> pixels = new ArrayList<>();
        
        cursor.reset();
        while (cursor.hasNext()) {
            pixels.add(cursor.next().value());
        }
        
        assertTrue(
            pixels.get(0).getR() == 1 && pixels.get(0).getG() == 0 && pixels.get(0).getB() == 0 &&
            pixels.get(1).getR() == 2 && pixels.get(1).getG() == 0 && pixels.get(1).getB() == 0 &&
            pixels.get(2).getR() == 3 && pixels.get(2).getG() == 0 && pixels.get(2).getB() == 0 &&
            pixels.get(3).getR() == 4 && pixels.get(3).getG() == 0 && pixels.get(3).getB() == 0 );
    }

}