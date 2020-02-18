package fr.fresnel.fourPolar.core.image.generic.imglib2Model.type;

import fr.fresnel.fourPolar.core.image.generic.pixel.IPixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.Pixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.float32;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.real.FloatType;

/**
 * Creates a suitable pixel type (of ours) based on the passed data type of
 * Imglib2.
 */
class FloatTypeWrapper implements ImgLib2TypeWrapper {
    @Override
    public <T extends NativeType<T>> IPixel<?> getPixel(T type) {
        FloatType value = (FloatType)type;
        return new Pixel<float32>(new float32(value.get()));
    }
}