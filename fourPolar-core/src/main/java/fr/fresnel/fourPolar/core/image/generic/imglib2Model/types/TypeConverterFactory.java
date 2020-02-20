package fr.fresnel.fourPolar.core.image.generic.imglib2Model.types;

import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.types.ConverterNotFound;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.float32;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.FloatType;

/**
 * Creates a suitable pixel type (of ours) based on the passed data type of
 * Imglib2.
 */
public class TypeConverterFactory {
    /**
     * Returns the proper concrete converter between ImgLib2 and our data type.
     * 
     * @param <T> is a data type of ImgLib2.
     * @param type is an instance of the data type we wish to convert.
     * @return
     * @throws ConverterNotFound
     */
    public static <T extends NativeType<T>> TypeConverter<T> getConverter(T type) throws ConverterNotFound {
        TypeConverter<T> converter = null;

        if (type instanceof FloatType) {
            converter = (TypeConverter<T>) new FloatTypeConverter();

        } else if (type instanceof UnsignedShortType) {
            converter = (TypeConverter<T>) new UnsignedShortTypeConverter();

        } else if (type instanceof ARGBType) {
            converter = (TypeConverter<T>) new ARGBConverter();

        } else {
            throw new ConverterNotFound();
        }

        return converter;
    }

    /**
     * Returns the suitable converter based on the given {@code PixelType}.
     * 
     * @param <T> is a native type of ImgLib2.
     * @param pixelType is the {@ PixelType} for which we seek to find a converter.
     * @return an instance of {@code TypeConverter} 
     * @throws ConverterNotFound in case the given type has no suitable converter.
     */
    public static <T extends NativeType<T>> TypeConverter<T> getConverter(PixelType pixelType) throws ConverterNotFound {
        TypeConverter<T> converter = null;

        switch (pixelType.getType()) {
            case FLOAT_32:
                converter = (TypeConverter<T>) new FloatTypeConverter();    
                break;

            case UINT_16:
                converter = (TypeConverter<T>) new UnsignedShortTypeConverter();
                break;

            case RGB_16:
                converter = (TypeConverter<T>) new ARGBConverter();
                break;                
        
            default:
                throw new ConverterNotFound();
        }
        
        return converter;
    }
}