package fr.fresnel.fourPolar.algorithm.util.image.converters;

import java.util.Arrays;
import java.util.Objects;

import fr.fresnel.fourPolar.algorithm.util.image.stats.ImageStatistics;
import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.ConverterToImgLib2NotFound;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.IPixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelTypes;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RealType;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.util.shape.IPointShape;
import net.imglib2.converter.Converter;
import net.imglib2.converter.RealLUTConverter;
import net.imglib2.display.ColorTable8;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.type.numeric.real.DoubleType;

/**
 * Using this class, we can convert GrayScale types (e.g, {@link UINT16}) to
 * color types {@link RGB16}.
 */
public class GrayScaleToColorConverter {
    /**
     * Convertes the given channel of {@link UINT16} image to an {@link RGB16}
     * image. Note that an 8 bit lookup table is used for the conversion, hence
     * there are only 256 white pixels. Note that each image plane is scaled with
     * respect to it's minimum and maximum (not the maximum of the entire image.).
     * If a plane has no maximum, all pixels will be black.
     * 
     * 
     * @throws ConverterToImgLib2NotFound if the image model can't be converted to
     *                                    ImgLib2 model.
     * @throws IllegalArgumentException   if the @param grayImage is not XYCZT, or
     *                                    channel is not in the image.
     * 
     */
    public static <T extends RealType> Image<RGB16> useMaxEachPlane_ImageXYCZT(final Image<T> grayImage, int channel)
            throws ConverterToImgLib2NotFound {
        Objects.requireNonNull(grayImage, "grayImage cannot be null");
        Objects.requireNonNull(grayImage, "colorImage cannot be null");

        if (grayImage.getMetadata().axisOrder() != AxisOrder.XYCZT) {
            throw new IllegalArgumentException("Cannot convert, because the grayImage is not XYCZT");
        }

        if (grayImage.getMetadata().getDim()[2] < channel) {
            throw new IllegalArgumentException(
                    "Cannot convert, because gray image does not have the specified channel. ");
        }

        long[] grayImgDim = grayImage.getMetadata().getDim();

        long[] colorImgDim = { grayImgDim[0], grayImgDim[1], grayImgDim[3], grayImgDim[4] };
        IMetadata colorMetadata = new Metadata.MetadataBuilder(colorImgDim).axisOrder(AxisOrder.XYZT)
                .bitPerPixel(PixelTypes.RGB_16).build();
        Image<RGB16> colorImage = grayImage.getFactory().create(colorMetadata, RGB16.zero());

        // We use ImgLib2 classes, i.e, we convert to Img. Then we convert the Image.
        // Finaly, we create a new Image instance and fill it, and return it.
        final ColorTable8 cTable8 = new ColorTable8();
        final IPixelCursor<T> grayCursor = grayImage.getCursor();
        final IPixelCursor<RGB16> colorCursor = colorImage.getCursor();

        final DoubleType doubleType = new DoubleType();
        final ARGBType argbType = new ARGBType();

        final double[][] minMax = _getPlaneMinMax(grayImage);

        IPointShape planeDim = ImageStatistics.getPlaneDim(grayImage);
        final long planeSize = planeDim.point()[0] * planeDim.point()[1];

        int planeNo = 1;
        int planePixelCounter = 0;
        while (grayCursor.hasNext()) {
            if (++planePixelCounter > planeSize) {
                planePixelCounter = 0;
                planeNo++;
            }

            final Converter<DoubleType, ARGBType> converter = new RealLUTConverter<DoubleType>(minMax[0][planeNo - 1],
                    minMax[1][planeNo - 1], cTable8);
            final IPixel<RGB16> pixel = colorCursor.next();

            doubleType.set(grayCursor.next().value().getRealValue());
            converter.convert(doubleType, argbType);

            final int color = argbType.get();
            pixel.value().set(ARGBType.red(color), ARGBType.green(color), ARGBType.blue(color));
            colorCursor.setPixel(pixel);
        }

        return colorImage;
    }

    private static <T extends RealType> double[][] _getPlaneMinMax(final Image<T> grayImage) {
        final double[][] minMax = ImageStatistics.getPlaneMinMax(grayImage);
        for (int i = 0; i < minMax[0].length; i++) {
            if (minMax[0][i] == minMax[1][i]) {
                minMax[1][i]++;
            }
        }
        return minMax;
    }
}