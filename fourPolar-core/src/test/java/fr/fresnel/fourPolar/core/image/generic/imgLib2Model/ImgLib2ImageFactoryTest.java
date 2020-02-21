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
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.cell.CellImg;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.FloatType;

public class ImgLib2ImageFactoryTest {
    @Test
    public void createByDimension_UINT16Image_CreatesImageOfSameDimension() {
        long[] dimensions = new long[] { 1, 1, 1, 1, 1 };
        Image<UINT16> image = new ImgLib2ImageFactory().create(dimensions, new UINT16());
        
        assertArrayEquals(image.getDimensions(), dimensions);
    }

    @Test
    public void createByDimension_Float32Image_CreatesImageOfSameDimension() {
        long[] dimensions = new long[] { 1, 1, 1, 1, 1 };
        Image<Float32> image = new ImgLib2ImageFactory().create(dimensions, new Float32());

        assertArrayEquals(image.getDimensions(), dimensions);
    }

    @Test
    public void createByDimension_RGBImage_CreatesImageOfSameDimension() {
        long[] dimensions = new long[] { 1, 1, 1, 1, 1 };
        Image<RGB16> image = new ImgLib2ImageFactory().create(dimensions, new RGB16());

        assertArrayEquals(image.getDimensions(), dimensions);
    }

    @Test
    public void checkImageType_SmallDimension_CreatesAnArrayImage() {
        long[] dimensions = new long[] { 1, 1, 1, 1, 1 };
        Image<UINT16> image = new ImgLib2ImageFactory().create(dimensions, new UINT16());

        assertTrue(image.toString().contains("ArrayImg"));
    }

    @Test
    public void checkImageType_LargeDimension_CreatesACellImage() {
        long[] dimensions = new long[] { 250, 250, 1024, 2 };
        Image<UINT16> image = new ImgLib2ImageFactory().create(dimensions, new UINT16());
        
        assertTrue(image.toString().contains("CellImg"));
    }

    @Test
    public void createFromImgType_UnsignedShortType_CreatesUINT16ImageWithSameDimension() {
        long[] dimensions = new long[] { 1, 1, 1, 1 };
        UnsignedShortType type = new UnsignedShortType();
        Img<UnsignedShortType> img = new ArrayImgFactory<UnsignedShortType>(type).create(dimensions);
        Image<UINT16> image = new ImgLib2ImageFactory().create(img, type);

        assertArrayEquals(image.getDimensions(), dimensions);        
    }

    @Test
    public void createFromImgType_FloatType_CreatesFloat32ImageWithSameDimension() {
        long[] dimensions = new long[] { 1, 1, 1, 1 };
        FloatType type = new FloatType();
        Img<FloatType> img = new ArrayImgFactory<FloatType>(type).create(dimensions);
        Image<Float32> image = new ImgLib2ImageFactory().create(img, type);

        assertArrayEquals(image.getDimensions(), dimensions);        
    }

    @Test
    public void createFromImgType_ARGBType_CreatesRGB16ImageWithSameDimension() {
        long[] dimensions = new long[] { 1, 1, 1, 1 };
        ARGBType type = new ARGBType();
        Img<ARGBType> img = new ArrayImgFactory<ARGBType>(type).create(dimensions);
        Image<RGB16> image = new ImgLib2ImageFactory().create(img, type);

        assertArrayEquals(image.getDimensions(), dimensions);        
    }


}