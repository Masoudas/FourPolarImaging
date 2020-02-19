package fr.fresnel.fourPolar.core.image.generic.imglib2Model;

import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.float32;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.uint16;
import net.imglib2.img.Img;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.FloatType;

/**
 * ImageToImgLib2Converter
 */
public class ImageToImgLib2Converter {
    public static Img<UnsignedShortType> getImg(Image<uint16> image, uint16 pixelType) {
        Img<UnsignedShortType> img = null;

        if (image instanceof ImgLib2Image) {
            img = (Img<UnsignedShortType>) (image);
        }

        return img;
    }

    public static Img<FloatType> getImg(Image<float32> image, float32 pixelType) {
        Img<FloatType> img = null;

        if (image instanceof ImgLib2Image) {
            img = (Img<FloatType>) (image);
        }

        return img;
    }

    public static Img<ARGBType> getImg(Image<RGB16> image, RGB16 pixelType) {
        Img<ARGBType> img = null;

        if (image instanceof ImgLib2Image) {
            img = (Img<ARGBType>) (image);
        }

        return img;
    }

    // public static <U extends NativeType<U>, V extends PixelType> Img<U> getImg(Image<V> image) {
    //     Img<U> img = null;

    //     if (image instanceof ImgLib2Image) {
    //         img = _getByCast(image);
    //     }

    //     return img;

    // }

    // private static <U extends NativeType<U>, V extends PixelType> Img<U>
    // _getByCast(Image<V> image) {
    // Img<U> img;
    // switch (image.getPixelType()) {
    // case UINT_16:
    // img = (Img<UnsignedShortType>)image;
    // break;

    // case FLOAT_32:

    // break;

    // case RGB_16:

    // break;

    // default:
    // break;
    // }

    // }

}