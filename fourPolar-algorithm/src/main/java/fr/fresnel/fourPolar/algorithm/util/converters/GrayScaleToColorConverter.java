package fr.fresnel.fourPolar.algorithm.util.converters;

import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.ConverterToImgLib2NotFound;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImageToImgLib2Converter;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.converter.Converter;
import net.imglib2.converter.Converters;
import net.imglib2.converter.RealLUTConverter;
import net.imglib2.display.ColorTable8;
import net.imglib2.img.Img;
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
    public static Image<RGB16> convertImage(final Image<UINT16> image) throws ConverterToImgLib2NotFound {
        // We use ImgLib2 classes, i.e, we convert to Img. Then we convert the Image.
        // Finaly, we create a new Image instance and fill it, and return it.
        Img<UnsignedShortType> imgLib2Model = ImageToImgLib2Converter.getImg(image, UINT16.zero());

        final RandomAccessibleInterval<UnsignedShortType> rai = imgLib2Model;
        final ColorTable8 cTable8 = new ColorTable8();
        final Converter<UnsignedShortType, ARGBType> converter = new RealLUTConverter<UnsignedShortType>(0, 0xffff,
                cTable8);
        final RandomAccessibleInterval<ARGBType> convertedRAI = Converters.convert(rai, converter, new ARGBType());

        final Image<RGB16> colorImage = image.getFactory().create(image.getDimensions(), RGB16.zero());
        final IPixelCursor<RGB16> cursor = colorImage.getCursor();
        while (cursor.hasNext()) {
            RGB16 pixel = cursor.next().value();
            int color = convertedRAI.getAt(cursor.localize()).get();
            pixel.set(ARGBType.red(color), ARGBType.green(color), ARGBType.blue(color));
        }

        return colorImage;
    }
}