package fr.fresnel.fourPolar.core.image.generic.imgLib2Model.types;

import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelTypes;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;

import net.imglib2.type.numeric.integer.UnsignedShortType;

/**
 * Creates a suitable pixel type (of ours) based on the passed data type of
 * Imglib2.
 */
class UnsignedShortTypeConverter implements TypeConverter<UINT16, UnsignedShortType> {
    @Override
    public void setPixelType(UnsignedShortType type, UINT16 pixel) {
        pixel.set(type.get());
    }

    @Override
    public void setNativeType(UINT16 pixel, UnsignedShortType type) {
        UINT16 pixelVal = (UINT16)pixel;
        type.set(pixelVal.get());
    }

    @Override
    public PixelTypes getPixelType() {
        return PixelTypes.UINT_16;
    }
}