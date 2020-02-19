package fr.fresnel.fourPolar.core.image.generic.imglib2Model.types;

import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;
import net.imglib2.type.NativeType;

public interface TypeConverter<U extends NativeType<U>> {
    /**
     * Wraps the given {@code NativeType} into the proper type of our own.
     */
    public PixelType getPixel(U type);

    /**
     * Assigns the given pixel to the type.
     * 
     * @param pixel
     * @param type
     */
    public void setNativeType(PixelType pixel, U type);
}