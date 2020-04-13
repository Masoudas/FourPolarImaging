package fr.fresnel.fourPolar.core.image.generic.imgLib2Model.types;

import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Type;
import net.imglib2.type.NativeType;

/**
 * The interface to convert our pixel data types to ImgLib2 data types and back.
 * @param <U> is a {@code NativeType} 
 */
public interface TypeConverter<T extends PixelType, U extends NativeType<U>> {
    /**
     * Converts the given data type of ImgLib2 to the proper type of our own.
     */
    public void setPixelType(U type, T pixel);

    /**
     * Assigns the given pixel type value to the proper type of ImgLib2.
     * 
     * @param pixel
     * @param type
     */
    public void setNativeType(T pixel, U type);

    /**
     * Returns the {@link PixelType} that would be used for this converter.
     */
    public Type getPixelType();
}