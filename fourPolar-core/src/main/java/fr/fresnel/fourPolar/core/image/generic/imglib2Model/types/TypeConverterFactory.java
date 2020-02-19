package fr.fresnel.fourPolar.core.image.generic.imglib2Model.types;

import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.types.ConverterNotFound;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.FloatType;

/**
 * Creates a suitable pixel type (of ours) based on the passed data type of
 * Imglib2.
 */
public class TypeConverterFactory {
    public static <T extends NativeType<T>> TypeConverter<T> create(T type) throws ConverterNotFound {
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