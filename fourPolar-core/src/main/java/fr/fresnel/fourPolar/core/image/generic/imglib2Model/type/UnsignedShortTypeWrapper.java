package fr.fresnel.fourPolar.core.image.generic.imglib2Model.type;

import fr.fresnel.fourPolar.core.image.generic.pixel.IPixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.Pixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.uint16;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.integer.UnsignedShortType;

/**
 * Creates a suitable pixel type (of ours) based on the passed data type of
 * Imglib2.
 */
class UnsignedShortTypeWrapper implements ImgLib2TypeWrapper {
    @Override
    public <T extends NativeType<T>> IPixel<?> getPixel(T type) {
        UnsignedShortType value = (UnsignedShortType)type;
        return new Pixel<uint16>(new uint16(value.get()));
    }
}