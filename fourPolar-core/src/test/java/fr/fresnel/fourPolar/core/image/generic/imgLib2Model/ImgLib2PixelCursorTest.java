package fr.fresnel.fourPolar.core.image.generic.imgLib2Model;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.Pixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;

public class ImgLib2PixelCursorTest {
    @Test
    public void localize_UINT16Image_ReturnsAllPositionsInTheImage() {
        long[] dimensions = new long[] { 2, 2 };
        Image<UINT16> image = new ImgLib2ImageFactory().create(dimensions, new UINT16());

        IPixelCursor<UINT16> cursor = image.getCursor();

        List<long[]> list = new ArrayList<>();

        while (cursor.hasNext()) {
            cursor.next();
            list.add(cursor.localize());
        }

        assertArrayEquals(new long[]{0, 0}, list.get(0));
        assertArrayEquals(new long[]{1, 0}, list.get(1));
        assertArrayEquals(new long[]{0, 1}, list.get(2));
        assertArrayEquals(new long[]{1, 1}, list.get(3));
        
    }

    @Test
    public void setPixel_UINT16Image_SetsPixelsToDefinedValues() {
        long[] dimensions = new long[] { 2, 2 };
        Image<UINT16> image = new ImgLib2ImageFactory().create(dimensions, new UINT16());
        IPixelCursor<UINT16> cursor = image.getCursor();
 
        int value = 1;
        while (cursor.hasNext()) {
            value++;
            cursor.next();
            cursor.setPixel(new Pixel<UINT16>(new UINT16(value)));
        }

        List<UINT16> pixels = new ArrayList<>();
        
        while (cursor.hasNext()) {
            pixels.add(cursor.next().value());
        }
        
        assertTrue(
            pixels.get(0).get() == 1 &&
            pixels.get(1).get() == 2 &&
            pixels.get(2).get() == 3 && 
            pixels.get(3).get() == 4);
    }
    
}