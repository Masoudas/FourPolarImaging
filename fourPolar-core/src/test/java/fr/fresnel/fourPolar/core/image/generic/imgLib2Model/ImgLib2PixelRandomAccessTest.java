package fr.fresnel.fourPolar.core.image.generic.imgLib2Model;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.IPixelRandomAccess;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.Pixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;

public class ImgLib2PixelRandomAccessTest {
    @Test
    public void setPixel_UINT16Image_SetsPixelsToDefinedValues() {
        long[] dimensions = new long[] { 2, 2 };
        Image<UINT16> image = new ImgLib2ImageFactory().create(dimensions, UINT16.zero());

        IPixelRandomAccess<UINT16> rAccess = image.getRandomAccess();

        int pixelValue = 0;
        rAccess.setPosition(new long[]{0,0});
        rAccess.setPixel(new Pixel<UINT16>(new UINT16(++pixelValue)));

        rAccess.setPosition(new long[]{1,0});
        rAccess.setPixel(new Pixel<UINT16>(new UINT16(++pixelValue)));

        rAccess.setPosition(new long[]{0,1});
        rAccess.setPixel(new Pixel<UINT16>(new UINT16(++pixelValue)));

        rAccess.setPosition(new long[]{1,1});
        rAccess.setPixel(new Pixel<UINT16>(new UINT16(++pixelValue)));

        ArrayList<UINT16> pixels = new ArrayList<>();

        rAccess.setPosition(new long[]{0,0});
        pixels.add(rAccess.getPixel().value());

        rAccess.setPosition(new long[]{1,0});
        pixels.add(rAccess.getPixel().value());

        rAccess.setPosition(new long[]{0,1});
        pixels.add(rAccess.getPixel().value());

        rAccess.setPosition(new long[]{1,1});
        pixels.add(rAccess.getPixel().value());

        assertTrue(
            pixels.get(0).get() == 1 &&
            pixels.get(1).get() == 2 &&
            pixels.get(2).get() == 3 &&
            pixels.get(3).get() == 4
         );
    }

    @Test
    public void setPixel_Float32Image_SetsPixelsToDefinedValues() {
        long[] dimensions = new long[] { 2, 2 };
        Image<Float32> image = new ImgLib2ImageFactory().create(dimensions, Float32.zero());

        IPixelRandomAccess<Float32> rAccess = image.getRandomAccess();

        float pixelValue = 0.1f;
        rAccess.setPosition(new long[]{0,0});
        rAccess.setPixel(new Pixel<Float32>(new Float32(++pixelValue)));

        rAccess.setPosition(new long[]{1,0});
        rAccess.setPixel(new Pixel<Float32>(new Float32(++pixelValue)));

        rAccess.setPosition(new long[]{0,1});
        rAccess.setPixel(new Pixel<Float32>(new Float32(++pixelValue)));

        rAccess.setPosition(new long[]{1,1});
        rAccess.setPixel(new Pixel<Float32>(new Float32(++pixelValue)));

        ArrayList<Float32> pixels = new ArrayList<>();

        rAccess.setPosition(new long[]{0,0});
        pixels.add(rAccess.getPixel().value());

        rAccess.setPosition(new long[]{1,0});
        pixels.add(rAccess.getPixel().value());

        rAccess.setPosition(new long[]{0,1});
        pixels.add(rAccess.getPixel().value());

        rAccess.setPosition(new long[]{1,1});
        pixels.add(rAccess.getPixel().value());

        assertTrue(
            pixels.get(0).get() == 1.1f &&
            pixels.get(1).get() == 2.1f &&
            pixels.get(2).get() == 3.1f &&
            pixels.get(3).get() == 4.1f
         );
    }

    @Test
    public void setPixel_RGB16Image_SetsPixelsToDefinedValues() {
        long[] dimensions = new long[] { 2, 2 };
        Image<RGB16> image = new ImgLib2ImageFactory().create(dimensions, RGB16.zero());

        IPixelRandomAccess<RGB16> rAccess = image.getRandomAccess();

        int pixelValue = 0;
        rAccess.setPosition(new long[]{0,0});
        rAccess.setPixel(new Pixel<RGB16>(new RGB16(++pixelValue, 0, 0)));

        rAccess.setPosition(new long[]{1,0});
        rAccess.setPixel(new Pixel<RGB16>(new RGB16(++pixelValue, 0, 0)));

        rAccess.setPosition(new long[]{0,1});
        rAccess.setPixel(new Pixel<RGB16>(new RGB16(++pixelValue, 0, 0)));

        rAccess.setPosition(new long[]{1,1});
        rAccess.setPixel(new Pixel<RGB16>(new RGB16(++pixelValue, 0, 0)));

        ArrayList<RGB16> pixels = new ArrayList<>();

        rAccess.setPosition(new long[]{0,0});
        pixels.add(rAccess.getPixel().value());

        rAccess.setPosition(new long[]{1,0});
        pixels.add(rAccess.getPixel().value());

        rAccess.setPosition(new long[]{0,1});
        pixels.add(rAccess.getPixel().value());

        rAccess.setPosition(new long[]{1,1});
        pixels.add(rAccess.getPixel().value());

        assertTrue(
            pixels.get(0).getR() == 1 && pixels.get(0).getG() == 0 && pixels.get(0).getB() == 0 &&
            pixels.get(1).getR() == 2 && pixels.get(1).getG() == 0 && pixels.get(1).getB() == 0 &&
            pixels.get(2).getR() == 3 && pixels.get(2).getG() == 0 && pixels.get(2).getB() == 0 &&
            pixels.get(3).getR() == 4 && pixels.get(3).getG() == 0 && pixels.get(3).getB() == 0
         );
    }

    @Test
    public void getPixel_OutOfBoundPixel_ThrowsArrayIndexOutOfBound() {
        long[] dimensions = new long[] { 2, 2 };
        Image<UINT16> image = new ImgLib2ImageFactory().create(dimensions, UINT16.zero());

        IPixelRandomAccess<UINT16> rAccess = image.getRandomAccess();

        rAccess.setPosition(new long[]{2,2});
        assertThrows(ArrayIndexOutOfBoundsException.class, ()->{rAccess.getPixel();});
        
    }

    @Test
    public void setPixel_OutOfBoundPixel_ThrowsArrayIndexOutOfBound() {
        long[] dimensions = new long[] { 2, 2 };
        Image<UINT16> image = new ImgLib2ImageFactory().create(dimensions, UINT16.zero());

        IPixelRandomAccess<UINT16> rAccess = image.getRandomAccess();

        rAccess.setPosition(new long[]{2,2});
        assertThrows(ArrayIndexOutOfBoundsException.class, ()->{rAccess.setPixel(new Pixel<UINT16>(UINT16.zero()));});
        
    }

    
}