package fr.fresnel.fourPolar.core.image.generic.imglib2Model.types;

import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;
import net.imglib2.type.NativeType;

/**
 * The interface to convert our pixel data types to ImgLib2 data types and back.
 * @param <U>
 */
public interface TypeConverter<U extends NativeType<U>> {
    /**
     * Converts the given data type of ImgLib2 to the proper type of our own.
     */
    public PixelType getPixel(U type);

    /**
     * Assigns the given pixel type value to the proper type of ImgLib2.
     * 
     * @param pixel
     * @param type
     */
    public void setNativeType(PixelType pixel, U type);
}