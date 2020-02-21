package fr.fresnel.fourPolar.core.image.generic.imgLib2Model.type;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.types.ConverterNotFound;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.types.TypeConverter;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.types.TypeConverterFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.FloatType;

public class UnsignedShortTypeConverterTest {

    @Test
    public void getPixel_UnsignedShortType_WrapsValueInUINT16() throws ConverterNotFound {
        UnsignedShortType val1 = new UnsignedShortType(0);
        UnsignedShortType val2 = new UnsignedShortType(0xffff);

        TypeConverter<UnsignedShortType> converter = TypeConverterFactory.getConverter(new UnsignedShortType());

        UINT16 val1Uint16 = (UINT16)converter.getPixelType(val1);
        UINT16 val2Uint16 = (UINT16)converter.getPixelType(val2);

        assertTrue(val1Uint16.get() == val1.get() && val2Uint16.get() == val2.get());
    }

    @Test
    public void setNativeType_UINT16_SetsUnsignedShortTypeToSameValue() throws ConverterNotFound {
        UINT16 val1Uint16 = new UINT16(1);
        UINT16 val2Uint16 = new UINT16(0xffff);

        TypeConverter<UnsignedShortType> converter = TypeConverterFactory.getConverter(new UnsignedShortType());

        UnsignedShortType val1 = new UnsignedShortType();
        UnsignedShortType val2 = new UnsignedShortType();

        converter.setNativeType(val1Uint16, val1);
        converter.setNativeType(val2Uint16, val2);

        assertTrue(val1Uint16.get() == val1.get() && val2Uint16.get() == val2.get());
    }

    @Test
    public void getPixel_FloatType_WrapsValueInFloat32() throws ConverterNotFound {
        FloatType val1 = new FloatType(-1e-4f);
        FloatType val2 = new FloatType(1e-4f);

        TypeConverter<FloatType> converter = TypeConverterFactory.getConverter(new FloatType());

        Float32 val1Float32 = (Float32)converter.getPixelType(val1);
        Float32 val2Float32 = (Float32)converter.getPixelType(val2);

        assertTrue(val1Float32.get() == val1.get() && val2Float32.get() == val2.get());
    }

    @Test
    public void setNativeType_Float32_SetsFloatTypeToSameValue() throws ConverterNotFound {
        Float32 val1Float32 = new Float32(-1e-4f);
        Float32 val2Float32 = new Float32(1e-4f);

        TypeConverter<FloatType> converter = TypeConverterFactory.getConverter(new FloatType());

        FloatType val1 = new FloatType();
        FloatType val2 = new FloatType();

        converter.setNativeType(val1Float32, val1);
        converter.setNativeType(val2Float32, val2);

        assertTrue(val1Float32.get() == val1.get() && val2Float32.get() == val2.get());
    }

    @Test
    public void getPixel_ARGBType_WrapsValueInRGB16() throws ConverterNotFound {
        ARGBType val1 = new ARGBType(ARGBType.rgba(1, 0, 0, 0));
        ARGBType val2 = new ARGBType(ARGBType.rgba(0, 1, 0, 0));
        ARGBType val3 = new ARGBType(ARGBType.rgba(0, 0, 1, 0));

        TypeConverter<ARGBType> converter = TypeConverterFactory.getConverter(new ARGBType());

        RGB16 val1RGB16 = (RGB16)converter.getPixelType(val1);
        RGB16 val2RGB16 = (RGB16)converter.getPixelType(val2);
        RGB16 val3RGB16 = (RGB16)converter.getPixelType(val3);

        assertTrue(
            val1RGB16.getR() == ARGBType.red(val1.get()) && 
            val1RGB16.getG() == ARGBType.green(val1.get()) &&
            val1RGB16.getB() == ARGBType.blue(val1.get()) &&
            val2RGB16.getR() == ARGBType.red(val2.get()) && 
            val2RGB16.getG() == ARGBType.green(val2.get()) &&
            val2RGB16.getB() == ARGBType.blue(val2.get()) &&
            val3RGB16.getR() == ARGBType.red(val3.get()) && 
            val3RGB16.getG() == ARGBType.green(val3.get()) &&
            val3RGB16.getB() == ARGBType.blue(val3.get()));
    }

    @Test
    public void setNativeType_RGB16_SetsARGBTypeToSameValue() throws ConverterNotFound {
        RGB16 val1RGB16 = new RGB16(1, 0, 0);
        RGB16 val2RGB16 = new RGB16(0, 1, 0);
        RGB16 val3RGB16 = new RGB16(0, 0, 1);

        TypeConverter<ARGBType> converter = TypeConverterFactory.getConverter(new ARGBType());

        ARGBType val1 = new ARGBType();
        ARGBType val2 = new ARGBType();
        ARGBType val3 = new ARGBType();

        converter.setNativeType(val1RGB16, val1);
        converter.setNativeType(val2RGB16, val2);
        converter.setNativeType(val3RGB16, val3);

        assertTrue(
            val1RGB16.getR() == ARGBType.red(val1.get()) && 
            val1RGB16.getG() == ARGBType.green(val1.get()) &&
            val1RGB16.getB() == ARGBType.blue(val1.get()) &&
            val2RGB16.getR() == ARGBType.red(val2.get()) && 
            val2RGB16.getG() == ARGBType.green(val2.get()) &&
            val2RGB16.getB() == ARGBType.blue(val2.get()) &&
            val3RGB16.getR() == ARGBType.red(val3.get()) && 
            val3RGB16.getG() == ARGBType.green(val3.get()) &&
            val3RGB16.getB() == ARGBType.blue(val3.get()));

    }
    
}
