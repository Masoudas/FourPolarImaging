package fr.fresnel.fourPolar.core.image.generic.ImageJ1Model;

import org.apache.commons.lang.IllegalClassException;

import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.ConverterToImgLib2NotFound;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImageToImgLib2Converter;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import ij.ImagePlus;
import javassist.tools.reflect.CannotCreateException;
import net.imglib2.img.Img;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.FloatType;

/**
 * Using this class, we can convert an {@code Image} interface to an
 * {@code ImgPlus} interface. Note that converters only generate an interface
 * for access using the new interface, in other words, no new image type is
 * created underneath.
 * 
 */
public class ImageToImageJ1Conveter {
    private ImageToImageJ1Conveter() {
        throw new AssertionError();
    }

    public static ImagePlus convertToImgPlus(Image<RGB16> image, RGB16 pixelType) {
        if (image.getFactory() instanceof ImgLib2ImageFactory) {
            Img<ARGBType> imgLib2Image = null;
            try {
                imgLib2Image = ImageToImgLib2Converter.getImg(image, pixelType);
            } catch (ConverterToImgLib2NotFound e) {
            }
            return ImageJFunctions.wrapRGB(imgLib2Image, "");
        } else{
            throw new IllegalClassException("Can't create ImageJ1 type because no converter is defined.");
        }
        
    }

    public static ImagePlus convertToImgPlus(Image<UINT16> image, UINT16 pixelType) {
        if (image.getFactory() instanceof ImgLib2ImageFactory){
            Img<UnsignedShortType> imgLib2Image = null;
            try {
                imgLib2Image = ImageToImgLib2Converter.getImg(image, pixelType);
            } catch (ConverterToImgLib2NotFound e) {
            }
            return ImageJFunctions.wrapUnsignedShort(imgLib2Image, "");
        } else{
            throw new IllegalClassException("Can't create ImageJ1 type because no converter is defined.");
        }        
    }

    public static ImagePlus convertToImgPlus(Image<Float32> image, Float32 pixelType) {
        if (image.getFactory() instanceof ImgLib2ImageFactory){
            Img<FloatType> imgLib2Image = null;
            try {
                imgLib2Image = ImageToImgLib2Converter.getImg(image, pixelType);
            } catch (ConverterToImgLib2NotFound e) {
            }
            return ImageJFunctions.wrapFloat(imgLib2Image, "");
        } else{
            throw new IllegalClassException("Can't create ImageJ1 type because no converter is defined.");
        }        
    }

}