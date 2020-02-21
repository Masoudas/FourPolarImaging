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
     * @return the proper converter for the given ImgLib2 type.
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
}