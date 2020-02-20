package fr.fresnel.fourPolar.core.image.generic.imglib2Model;

import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.types.ConverterNotFound;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.cell.CellImgFactory;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.FloatType;

/**
 * A concrete factory to generate instances of images that comply with the {@code Image}
 * using ImgLib2.
 * 
 * @param <T>
 */
public class ImgLib2ImageFactory<T extends PixelType> implements ImageFactory<T> {
    /**
     * This is the threshold for the number of pixels, after which we opt for a cell
     * image rather than an array image. This is roughly 256MB for an image of type
     * float.
     */
    private static long _cellImgThr = 256 * 256 * 1024;
    private Image<T> _image;

    /**
     * Create an empty image with the given type and dimension.
     * 
     * @param dim is the image dimension.
     * @param pixelType is the associated pixel type.
     */
    public ImgLib2ImageFactory(long[] dim, T pixelType) {
        switch (pixelType.getType()) {
            case UINT_16:
                try {
                    UnsignedShortType type = new UnsignedShortType();
                    Img<UnsignedShortType> img = _chooseImgFactory(dim, type);

                    _image = new ImgLib2Image<T, UnsignedShortType>(img, new UnsignedShortType());
                } catch (ConverterNotFound e) {
                    // Exception never caught, because of proper creation of image.
                }
                break;

            case FLOAT_32:
                try {
                    FloatType type = new FloatType();
                    Img<FloatType> img = _chooseImgFactory(dim, type);

                    _image = new ImgLib2Image<T, FloatType>(img, new FloatType());
                } catch (ConverterNotFound e) {
                    // Exception never caught, because of proper creation of image.
                }
                break;

            case RGB_16:
                try {
                    ARGBType type = new ARGBType();
                    Img<ARGBType> img = _chooseImgFactory(dim, type);

                    _image = new ImgLib2Image<T, ARGBType>(img, new ARGBType());
                } catch (ConverterNotFound e) {
                    // Exception never caught, because of proper creation of image.
                }

            default:
                break;
        }
    }

    public <V extends NativeType<V>> ImgLib2ImageFactory(Img<V> img, V imgLib2Type) throws ConverterNotFound {
        this._image = new ImgLib2Image<T, V>(img, imgLib2Type);
    }

    /**
     * Creates the proper image for the given dimensionality.
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
     * Calculate the number of pixels associated with the image.
     * 
     * @param dim is the dimension of the image.
     * @return
     */
    private static long _getNPixels(long[] dim) {
        long nPixels = 1;
        for (long size : dim) {
            nPixels = nPixels * size;
        }

        return nPixels;
    }

    @Override
    public Image<T> create() {
        return this._image;
    }

}