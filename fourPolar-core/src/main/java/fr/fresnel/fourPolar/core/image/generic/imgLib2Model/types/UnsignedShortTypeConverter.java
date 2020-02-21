package fr.fresnel.fourPolar.core.image.generic.imgLib2Model.types;

import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;

import net.imglib2.type.numeric.integer.UnsignedShortType;

/**
 * Creates a suitable pixel type (of ours) based on the passed data type of
 * Imglib2.
 */
class UnsignedShortTypeConverter implements TypeConverter<UnsignedShortType> {
    @Override
    public PixelType getPixelType(UnsignedShortType type) {
        return new UINT16(type.get());
    }

    @Override
    public void setNativeType(PixelType pixel, UnsignedShortType type) {
        UINT16 pixelVal = (UINT16)pixel;
        type.set(pixelVal.get());
    }
}