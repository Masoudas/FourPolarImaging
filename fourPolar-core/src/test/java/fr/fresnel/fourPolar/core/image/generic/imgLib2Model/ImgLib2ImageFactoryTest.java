package fr.fresnel.fourPolar.core.image.generic.imgLib2Model;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.FloatType;

public class ImgLib2ImageFactoryTest {
    @Test
    public void createByDimension_UINT16Image_CreatesImageOfSameDimension() {
        long[] dimensions = new long[] { 1, 1, 1, 1, 1 };
        Image<UINT16> image = new ImgLib2ImageFactory().create(dimensions, UINT16.zero());
        
        assertArrayEquals(image.getDimensions(), dimensions);
    }

    @Test
    public void createByDimension_Float32Image_CreatesImageOfSameDimension() {
        long[] dimensions = new long[] { 1, 1, 1, 1, 1 };
        Image<Float32> image = new ImgLib2ImageFactory().create(dimensions, Float32.zero());

        assertArrayEquals(image.getDimensions(), dimensions);
    }

    @Test
    public void createByDimension_RGBImage_CreatesImageOfSameDimension() {
        long[] dimensions = new long[] { 1, 1, 1, 1, 1 };
        Image<RGB16> image = new ImgLib2ImageFactory().create(dimensions, RGB16.zero());

        assertArrayEquals(image.getDimensions(), dimensions);
    }

    @Test
    public void checkImageType_SmallDimension_CreatesAnArrayImage() {
        long[] dimensions = new long[] { 1, 1, 1, 1, 1 };
        Image<UINT16> image = new ImgLib2ImageFactory().create(dimensions, UINT16.zero());

        assertTrue(image.toString().contains("ArrayImg"));
    }

    @Test
    public void checkImageType_LargeDimension_CreatesACellImage() {
        long[] dimensions = new long[] { 1024, 1024, 1024, 2 };
        Image<UINT16> image = new ImgLib2ImageFactory().create(dimensions, UINT16.zero());
        
        assertTrue(image.toString().contains("CellImg"));
    }

    @Test
    public void createFromImgInterface_UnsignedShortType_CreatesUINT16ImageWithSameDimension() {
        long[] dimensions = new long[] { 1, 1, 1, 1 };
        UnsignedShortType type = new UnsignedShortType();
        Img<UnsignedShortType> img = new ArrayImgFactory<UnsignedShortType>(type).create(dimensions);
        Image<UINT16> image = new ImgLib2ImageFactory().create(img, type);

        assertArrayEquals(image.getDimensions(), dimensions);        
    }

    @Test
    public void createFromImgInterface_FloatType_CreatesFloat32ImageWithSameDimension() {
        long[] dimensions = new long[] { 1, 1, 1, 1 };
        FloatType type = new FloatType();
        Img<FloatType> img = new ArrayImgFactory<FloatType>(type).create(dimensions);
        Image<Float32> image = new ImgLib2ImageFactory().create(img, type);

        assertArrayEquals(image.getDimensions(), dimensions);        
    }

    @Test
    public void createFromImgInterface_ARGBType_CreatesRGB16ImageWithSameDimension() {
        long[] dimensions = new long[] { 1, 1, 1, 1 };
        ARGBType type = new ARGBType();
        Img<ARGBType> img = new ArrayImgFactory<ARGBType>(type).create(dimensions);
        Image<RGB16> image = new ImgLib2ImageFactory().create(img, type);

        assertArrayEquals(image.getDimensions(), dimensions);        
    }


}