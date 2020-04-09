package fr.fresnel.fourPolar.algorithm.util.image.converters;

import java.util.Arrays;
import java.util.Objects;

import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.ConverterToImgLib2NotFound;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.IPixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import net.imglib2.converter.Converter;
import net.imglib2.converter.RealLUTConverter;
import net.imglib2.display.ColorTable8;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.type.numeric.integer.UnsignedShortType;

/**
 * Using this class, we can convert GrayScale types (e.g, {@link UINT16}) to
 * color types {@link RGB16}.
 */
public class GrayScaleToColorConverter {
    /**
     * Convertes an {@link UINT16} to an {@link RGB16} image. Note that an 8 bit
     * lookup table is used for the conversion.
     * 
     * @throws ConverterToImgLib2NotFound
     */
    public static void convertImage(final Image<UINT16> grayImage, final Image<RGB16> colorImage)
            throws ConverterToImgLib2NotFound {
        Objects.requireNonNull(grayImage, "grayImage cannot be null");
        Objects.requireNonNull(grayImage, "colorImage cannot be null");

        if (!Arrays.equals(grayImage.getDimensions(), colorImage.getDimensions())) {
            throw new ArrayIndexOutOfBoundsException(
                    "Cannot convert, because the two images don't have same dimension.");
        }
        
        // We use ImgLib2 classes, i.e, we convert to Img. Then we convert the Image.
        // Finaly, we create a new Image instance and fill it, and return it.
        final ColorTable8 cTable8 = new ColorTable8();
        final Converter<UnsignedShortType, ARGBType> converter = new RealLUTConverter<UnsignedShortType>(0, 0xffff,
                cTable8);

        final IPixelCursor<UINT16> grayCursor = grayImage.getCursor();
        final IPixelCursor<RGB16> colorCursor = colorImage.getCursor();
        
        UnsignedShortType uShortType = new UnsignedShortType();
        ARGBType argbType = new ARGBType();
        while (grayCursor.hasNext()) {
            IPixel<RGB16> pixel = colorCursor.next();
            uShortType.set(grayCursor.next().value().get());
            converter.convert(uShortType, argbType);

            int color = argbType.get();
            pixel.value().set(ARGBType.red(color), ARGBType.green(color), ARGBType.blue(color));
            colorCursor.setPixel(pixel);
        }
    }
}