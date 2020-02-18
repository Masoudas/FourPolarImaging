package fr.fresnel.fourPolar.core.image.generic.imglib2Model.type;

import fr.fresnel.fourPolar.core.image.generic.pixel.IPixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.Pixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.float32;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.uint16;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.FloatType;

/**
 * Creates a suitable pixel type (of ours) based on the passed data type of
 * Imglib2.
 */
public class ImgLib2TypeWrapperFactory {
    public static <T extends NativeType<T>> ImgLib2TypeWrapper create(T type) {
        if (type instanceof FloatType) {
            return new FloatTypeWrapper();
            
        } else if (type instanceof UnsignedShortType) {
            return new UnsignedShortTypeWrapper();
            
        } else if (type instanceof ARGBType) {
            return new ARGBWrapper();
        
        }

        return null;
    }
}