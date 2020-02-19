package fr.fresnel.fourPolar.core.image.generic.imglib2Model.types;

import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.uint16;

import net.imglib2.type.numeric.integer.UnsignedShortType;

/**
 * Creates a suitable pixel type (of ours) based on the passed data type of
 * Imglib2.
 */
class UnsignedShortTypeConverter implements TypeConverter<UnsignedShortType> {
    @Override
    public PixelType getPixel(UnsignedShortType type) {
        return new uint16(type.get());
    }

    @Override
    public void setNativeType(PixelType pixel, UnsignedShortType type) {
        uint16 pixelVal = (uint16)pixel;
        type.set(pixelVal.get());
    }
}