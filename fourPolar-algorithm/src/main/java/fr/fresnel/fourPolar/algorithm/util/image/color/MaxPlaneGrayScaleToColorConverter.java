package fr.fresnel.fourPolar.algorithm.util.image.color;

import java.util.Objects;

import fr.fresnel.fourPolar.algorithm.util.image.stats.ImageStatistics;
import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.ConverterToImgLib2NotFound;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.metadata.MetadataUtil;
import fr.fresnel.fourPolar.core.image.generic.pixel.IPixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelTypes;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RealType;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import net.imglib2.converter.Converter;
import net.imglib2.converter.RealLUTConverter;
import net.imglib2.display.ColorTable8;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.type.numeric.real.DoubleType;

/**
 * Convertes the given channel of {@link UINT16} image to an {@link RGB16}
 * image. Note that an 8 bit lookup table is used for the conversion, hence
 * there are only 256 white pixels. Note that each image plane is scaled with
 * respect to it's minimum and maximum (not the maximum of the entire image.).
 * If a plane has no maximum, all pixels will be black.
 * 
 */
class MaxPlaneGrayScaleToColorConverter {
    public MaxPlaneGrayScaleToColorConverter() {
        throw new AssertionError();
    }

    /**
     * @throws ConverterToImgLib2NotFound if the image model can't be converted to
     *                                    ImgLib2 model.
     * 
     */
    public static <T extends RealType> Image<RGB16> convert(final Image<T> grayImage)
            throws ConverterToImgLib2NotFound {
        Objects.requireNonNull(grayImage, "grayImage cannot be null");
        Objects.requireNonNull(grayImage, "colorImage cannot be null");

        IMetadata colorMetadata = _copyGaryMetadataForColorImage(grayImage.getMetadata());
        Image<RGB16> colorImage = grayImage.getFactory().create(colorMetadata, RGB16.zero());

        // We use ImgLib2 classes, i.e, we convert to Img. Then we convert the Image.
        // Finaly, we create a new Image instance and fill it, and return it.
        final ColorTable8 cTable8 = new ColorTable8();
        final IPixelCursor<T> grayCursor = grayImage.getCursor();
        final IPixelCursor<RGB16> colorCursor = colorImage.getCursor();

        final DoubleType doubleType = new DoubleType();
        final ARGBType argbType = new ARGBType();

        final double[][] minMax = _getPlaneMinMax(grayImage);

        final long planeSize = MetadataUtil.getPlaneSize(grayImage.getMetadata());

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

    /**
     * Except for the obvious parameters, copy all the metadata of the gray image
     * for color.
     */
    private static IMetadata _copyGaryMetadataForColorImage(IMetadata grayMetadata) {
        return new Metadata.MetadataBuilder(grayMetadata).bitPerPixel(PixelTypes.RGB_16).build();
    }
}