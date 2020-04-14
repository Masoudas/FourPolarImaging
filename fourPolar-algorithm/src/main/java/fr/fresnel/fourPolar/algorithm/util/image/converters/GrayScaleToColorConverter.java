package fr.fresnel.fourPolar.algorithm.util.image.converters;

import java.util.Arrays;
import java.util.Objects;

import fr.fresnel.fourPolar.algorithm.util.image.stats.ImageStatistics;
import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.ConverterToImgLib2NotFound;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.IPixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RealType;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.util.DPoint;
import net.imglib2.converter.Converter;
import net.imglib2.converter.RealLUTConverter;
import net.imglib2.display.ColorTable8;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imagej.minmax.DefaultMinMaxMethod;

/**
 * Using this class, we can convert GrayScale types (e.g, {@link UINT16}) to
 * color types {@link RGB16}.
 */
public class GrayScaleToColorConverter {
    /**
     * Convertes an {@link UINT16} to an {@link RGB16} image. Note that an 8 bit
     * lookup table is used for the conversion, hence there are only 256 white
     * pixels. Note that each image plane is scaled with respect to it's minimum and
     * maximum (not the maximum of the entire image.)
     * 
     * @throws ConverterToImgLib2NotFound
     */
    public static <T extends RealType> void convertPlane(final Image<T> grayImage, final Image<RGB16> colorImage)
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
        final IPixelCursor<T> grayCursor = grayImage.getCursor();
        final IPixelCursor<RGB16> colorCursor = colorImage.getCursor();

        final DoubleType doubleType = new DoubleType();
        final ARGBType argbType = new ARGBType();

        final double[][] minMax = ImageStatistics.getPlaneMinMax(grayImage);
        
        DPoint planeDim = ImageStatistics.getPlaneDim(grayImage);
        final long planeSize = planeDim.x * planeDim.y;

        int planeNo = 1;
        int planePixelCounter = 0;
        while (grayCursor.hasNext()) {
            if (++planePixelCounter > planeSize) {
                planePixelCounter = 0;
                planeNo++;
            }

            final Converter<DoubleType, ARGBType> converter = new RealLUTConverter<DoubleType>(
                    minMax[0][planeNo - 1], minMax[1][planeNo - 1], cTable8);
            final IPixel<RGB16> pixel = colorCursor.next();

            doubleType.set(grayCursor.next().value().getRealValue());
            converter.convert(doubleType, argbType);

            final int color = argbType.get();
            pixel.value().set(ARGBType.red(color), ARGBType.green(color), ARGBType.blue(color));
            colorCursor.setPixel(pixel);
        }
    }
}