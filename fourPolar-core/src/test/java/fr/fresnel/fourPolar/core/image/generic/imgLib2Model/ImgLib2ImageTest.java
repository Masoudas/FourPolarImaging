package fr.fresnel.fourPolar.core.image.generic.imgLib2Model;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.types.ConverterNotFound;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.types.TypeConverter;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.types.TypeConverterFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Type;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.FloatType;

public class ImgLib2ImageTest {
    @Test
    public void dimensions_UnsignedShortImage_AllMethodsShouldReturnTheSameResult() throws ConverterNotFound {
        long[] dimensions = new long[] { 1, 1, 1, 1 };
        UnsignedShortType type = new UnsignedShortType();
        Img<UnsignedShortType> img = new ArrayImgFactory<UnsignedShortType>(type).create(dimensions);
        TypeConverter<UINT16, UnsignedShortType> converter = TypeConverterFactory.getConverter(UINT16.zero(), type);

        ImgLib2Image<UINT16, UnsignedShortType> image = new ImgLib2Image<UINT16, UnsignedShortType>(img, converter, null);

        assertArrayEquals(
            dimensions, image.getDimensions());
    }

    @Test
    public void getPixelType_UnsignedShortImage_ImageTypeIsUINT16() throws ConverterNotFound {
        long[] dimensions = new long[] { 1 };
        UnsignedShortType type = new UnsignedShortType();
        Img<UnsignedShortType> img = new ArrayImgFactory<UnsignedShortType>(type).create(dimensions);
        TypeConverter<UINT16, UnsignedShortType> converter = TypeConverterFactory.getConverter(UINT16.zero(), type);

        ImgLib2Image<UINT16, UnsignedShortType> image = new ImgLib2Image<UINT16, UnsignedShortType>(
            img, converter, null);

        assertTrue(image.getPixelType() == Type.UINT_16);
    }

    @Test
    public void getPixelType_FloatTypeImage_ImageTypeIsFloat32() throws ConverterNotFound {
        long[] dimensions = new long[] { 1 };
        FloatType type = new FloatType();
        Img<FloatType> img = new ArrayImgFactory<FloatType>(type).create(dimensions);
        TypeConverter<Float32, FloatType> converter = TypeConverterFactory.getConverter(Float32.zero(), type);

        ImgLib2Image<Float32, FloatType> image = new ImgLib2Image<Float32, FloatType>(img, converter, null);

        assertTrue(image.getPixelType() == Type.FLOAT_32);
    }

    @Test
    public void getPixelType_ARGBTypeImage_ImageTypeIsRGB16() throws ConverterNotFound {
        long[] dimensions = new long[] { 1 };
        ARGBType type = new ARGBType();
        Img<ARGBType> img = new ArrayImgFactory<ARGBType>(type).create(dimensions);
        TypeConverter<RGB16, ARGBType> converter = TypeConverterFactory.getConverter(RGB16.zero(), type);

        ImgLib2Image<RGB16, ARGBType> image = new ImgLib2Image<RGB16, ARGBType>(img, converter, null);

        assertTrue(image.getPixelType() == Type.RGB_16);
    }


}

