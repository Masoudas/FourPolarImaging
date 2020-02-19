package fr.fresnel.fourPolar.core.image.generic.imglib2Model;

import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.types.ConverterNotFound;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.cell.CellImgFactory;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.FloatType;

public class ImgLib2ImageFactory {
    /**
     * This is the threshold for the number of pixels, after which we opt for a cell
     * image rather than an array image. This is roughly 256MB for an image of type
     * float.
     */
    private static long _cellImgThr = 256 * 256 * 1024;

    public static <T extends PixelType> Image<T> create(long[] dim, T pixelType) {
        Image<T> image = null;

        switch (pixelType.getType()) {
            case UINT_16:
                try {
                    UnsignedShortType type = new UnsignedShortType();
                    Img<UnsignedShortType> img = _chooseImgFactory(dim, type);

                    return new ImgLib2Image<T, UnsignedShortType>(img, new UnsignedShortType());
                } catch (ConverterNotFound e) {
                    // Exception never caught, because of proper creation of image.
                }
                break;

            case FLOAT_32:
                try {
                    FloatType type = new FloatType();
                    Img<FloatType> img = _chooseImgFactory(dim, type);

                    return new ImgLib2Image<T, FloatType>(img, new FloatType());
                } catch (ConverterNotFound e) {
                    // Exception never caught, because of proper creation of image.
                }
                break;

            case RGB_16:
                try {
                    ARGBType type = new ARGBType();
                    Img<ARGBType> img = _chooseImgFactory(dim, type);

                    return new ImgLib2Image<T, ARGBType>(img, new ARGBType());
                } catch (ConverterNotFound e) {
                    // Exception never caught, because of proper creation of image.
                }

            default:
                break;
        }

        return image;

    }

    public static <U extends PixelType, V extends NativeType<V>> Image<U> create(Img<V> img, V imgLib2Type)
            throws ConverterNotFound {
        return new ImgLib2Image<U, V>(img, imgLib2Type);
    }

    /**
     * Returns the proper image factory for the given dimension.
     * 
     * @param <T>
     * @param dim
     * @param t
     * @return
     */
    private static <T extends NativeType<T>> Img<T> _chooseImgFactory(long[] dim, T t) {
        long nPixels = _getNPixels(dim);

        if (nPixels < _cellImgThr) {
            return new ArrayImgFactory<T>(t).create(dim);
        } else {
            return new CellImgFactory<T>(t).create(dim);
        }
    }

    /**
     * Calculate the number of pixels.
     * 
     * @param dim
     * @return
     */
    private static long _getNPixels(long[] dim) {
        long nPixels = 1;
        for (long size : dim) {
            nPixels = nPixels * size;
        }

        return nPixels;
    }

}