package fr.fresnel.fourPolar.core.image.generic.imgLib2Model;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.cell.CellImg;
import net.imglib2.type.numeric.integer.UnsignedShortType;

public class ImgLib2ImageTest {
    @Test
    public void getDimensions_UINT16Image_CreatesImageOfSameDimension() {
        long[] dimensions = new long[] { 1, 1, 1, 1, 1 };
        Image<UINT16> image = new ImgLib2ImageFactory<UINT16>().create(dimensions, new UINT16());
        
        assertArrayEquals(image.getDimensions(), dimensions);
    }

    @Test
    public void getDimensions_Float32Image_CreatesImageOfSameDimension() {
        long[] dimensions = new long[] { 1, 1, 1, 1, 1 };
        Image<Float32> image = new ImgLib2ImageFactory<Float32>().create(dimensions, new Float32());

        assertArrayEquals(image.getDimensions(), dimensions);
    }

    @Test
    public void getDimensions_RGBImage_CreatesImageOfSameDimension() {
        long[] dimensions = new long[] { 1, 1, 1, 1, 1 };
        Image<RGB16> image = new ImgLib2ImageFactory<RGB16>().create(dimensions, new RGB16());

        assertArrayEquals(image.getDimensions(), dimensions);
    }

    @Test
    public void checkImageType_SmallDimension_CreatesAnArrayImage() {
        long[] dimensions = new long[] { 1, 1, 1, 1, 1 };
        Image<UINT16> image = new ImgLib2ImageFactory<UINT16>().create(dimensions, new UINT16());

        assertTrue(image.toString().contains("ArrayImg"));
    }

    @Test
    public void checkImageType_LargeDimension_CreatesACellImage() {
        long[] dimensions = new long[] { 250, 250, 1024, 2 };
        Image<UINT16> image = new ImgLib2ImageFactory<UINT16>().create(dimensions, new UINT16());
        
        assertTrue(image.toString().contains("CellImg"));
    }

    @Test
    public void () {
        
    }
    
}