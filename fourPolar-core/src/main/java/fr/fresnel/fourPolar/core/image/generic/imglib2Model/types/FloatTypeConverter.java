package fr.fresnel.fourPolar.core.image.generic.imglib2Model.types;

import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.float32;
import net.imglib2.type.numeric.real.FloatType;

/**
 * Creates a suitable pixel type (of ours) based on the passed data type of
 * Imglib2.
 */
class FloatTypeConverter implements TypeConverter<FloatType> {
    @Override
    public PixelType getPixel(FloatType type) {
        FloatType value = (FloatType) type;
        return new float32(value.get());
    }

    @Override
    public void setNativeType(PixelType pixel, FloatType type) {
        float32 value = (float32)pixel;
        type.set(value.get());
    }
}