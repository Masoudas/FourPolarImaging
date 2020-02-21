package fr.fresnel.fourPolar.core.image.generic.imgLib2Model.types;

import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import net.imglib2.type.numeric.real.FloatType;

/**
 * Creates a suitable pixel type (of ours) based on the passed data type of
 * Imglib2.
 */
class FloatTypeConverter implements TypeConverter<FloatType> {
    @Override
    public PixelType getPixelType(FloatType type) {
        FloatType value = (FloatType) type;
        return new Float32(value.get());
    }

    @Override
    public void setNativeType(PixelType pixel, FloatType type) {
        Float32 value = (Float32)pixel;
        type.set(value.get());
    }
}