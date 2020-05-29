package fr.fresnel.fourPolar.core.image.generic.imgLib2Model;

import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.ConverterToImgLib2NotFound;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;

import net.imglib2.img.Img;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.FloatType;

/**
 * Using this class, we can convert an {@code Image} interface to a {@code Img}
 * interface. Note that converters only generate an interface for access using
 * the new interface, in other words, no new image type is created underneath.
 * 
 */
public class ImageToImgLib2Converter {
    /**
     * This method transforms {@code Image} of type UINT16 to {@code Img} of type
     * UnsignedShortType.
     * 
     * @param image     is the {@code Image} instance.
     * @param pixelType is the pixel type associated with the image.
     * @return the {@link Img} interface, which is the basic image interface of
     *         ImgLib2.
     * @throws ConverterToImgLib2NotFound
     */
    @SuppressWarnings("unchecked")
    public static Img<UnsignedShortType> getImg(Image<UINT16> image, UINT16 pixelType)
            throws ConverterToImgLib2NotFound {
        Img<UnsignedShortType> img = null;

        if (image instanceof ImgLib2Image) {
            ImgLib2Image<UINT16, UnsignedShortType> implementation = (ImgLib2Image<UINT16, UnsignedShortType>) image;
            img = implementation.getImg();
        } else {
            throw new ConverterToImgLib2NotFound();
        }

        return img;
    }

    /**
     * This method transforms {@code Image} of type Float32 to {@code Img} of type
     * FloatType.
     * 
     * @param image     is the {@code Image} instance.
     * @param pixelType is the pixel type associated with the image.
     * @return the {@link Img} interface, which is the basic image interface of
     *         ImgLib2.
     * @throws ConverterToImgLib2NotFound
     */
    @SuppressWarnings("unchecked")
    public static Img<FloatType> getImg(Image<Float32> image, Float32 pixelType) throws ConverterToImgLib2NotFound {
        Img<FloatType> img = null;

        if (image instanceof ImgLib2Image) {
            ImgLib2Image<Float32, FloatType> implementation = (ImgLib2Image<Float32, FloatType>) image;
            img = implementation.getImg();
        } else {
            throw new ConverterToImgLib2NotFound();
        }

        return img;
    }

    /**
     * This method transforms {@code Image} of type RGB16 to {@code Img} of type
     * ARGBType.
     * 
     * @param image     is the {@code Image} instance.
     * @param pixelType is the pixel type associated with the image.
     * @return the {@link Img} interface, which is the basic image interface of
     *         ImgLib2.
     */
    @SuppressWarnings("unchecked")
    public static Img<ARGBType> getImg(Image<RGB16> image, RGB16 pixelType) throws ConverterToImgLib2NotFound {
        Img<ARGBType> img = null;

        if (image instanceof ImgLib2Image) {
            ImgLib2Image<RGB16, ARGBType> implementation = (ImgLib2Image<RGB16, ARGBType>) image;
            img = implementation.getImg();
        } else {
            throw new ConverterToImgLib2NotFound();
        }

        return img;
    }
}