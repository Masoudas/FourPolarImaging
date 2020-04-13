package fr.fresnel.fourPolar.core.image.generic.imgLib2Model.types;

import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Type;
import net.imglib2.type.numeric.real.FloatType;

/**
 * Creates a suitable pixel type (of ours) based on the passed data type of
 * Imglib2.
 */
class FloatTypeConverter implements TypeConverter<Float32, FloatType> {
    @Override
    public void setPixelType(FloatType type, Float32 pixel) {
        pixel.set(type.get());   
    }

    @Override
    public void setNativeType(Float32 pixel, FloatType type) {
        Float32 value = (Float32)pixel;
        type.set(value.get());
    }

    @Override
    public Type getPixelType() {
        return Type.FLOAT_32;
    }
}