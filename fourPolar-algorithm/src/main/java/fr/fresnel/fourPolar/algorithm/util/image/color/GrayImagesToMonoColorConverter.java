package fr.fresnel.fourPolar.algorithm.util.image.color;

import java.util.Objects;

import fr.fresnel.fourPolar.algorithm.util.image.color.GrayScaleToColorConverter.Color;
import fr.fresnel.fourPolar.algorithm.util.image.stats.ImageStatistics;
import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.ConverterToImgLib2NotFound;
import fr.fresnel.fourPolar.core.image.generic.IPixelRandomAccess;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImageToImgLib2Converter;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2RandomAccessConverter;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.ARGB8;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.util.image.metadata.MetadataUtil;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessible;
import net.imglib2.converter.ChannelARGBConverter;
import net.imglib2.converter.ChannelARGBConverter.Channel;
import net.imglib2.converter.Converter;
import net.imglib2.converter.Converters;
import net.imglib2.converter.RealUnsignedByteConverter;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.integer.UnsignedShortType;

/**
 * Returns a mono color view of a gray scale image.
 */
class GrayImagesToMonoColorConverter {
    public GrayImagesToMonoColorConverter() {
        new AssertionError();
    }

    public static IPixelRandomAccess<ARGB8> convert(Image<UINT16> image, Color color) {
        Objects.requireNonNull(image);
        Objects.requireNonNull(color);

        Channel imageColor = _getImgLib2ColorChannel(color);

        RandomAccessible<UnsignedByteType> unsignedByteImgRA = _getUnsignedByteImage(image);

        Converter<UnsignedByteType, ARGBType> toColorConverter = new ChannelARGBConverter(imageColor);
        RandomAccess<ARGBType> monochromeImageView = Converters
                .convert(unsignedByteImgRA, toColorConverter, new ARGBType()).randomAccess();

        return ImgLib2RandomAccessConverter.convertARGBType(monochromeImageView);
    }

    public static RandomAccessible<UnsignedByteType> _getUnsignedByteImage(final Image<UINT16> uint16Image) {
        Img<UnsignedByteType> byteImg = new ArrayImgFactory<UnsignedByteType>(new UnsignedByteType())
                .create(uint16Image.getMetadata().getDim());
        final double[][] minMax = _getPlaneMinMax(uint16Image);

        Cursor<UnsignedShortType> grayCursor = null;
        try {
            grayCursor = ImageToImgLib2Converter.getImg(uint16Image, UINT16.zero()).cursor();
        } catch (ConverterToImgLib2NotFound e) {
            // TODO get rid of this.
        }
        final Cursor<UnsignedByteType> byteCursor = byteImg.cursor();

        final long planeSize = MetadataUtil.getPlaneSize(uint16Image.getMetadata());

        int planeNo = 1;
        int planePixelCounter = 0;
        Converter<UnsignedShortType, UnsignedByteType> converter = new RealUnsignedByteConverter<>(
                minMax[0][planeNo - 1], minMax[1][planeNo - 1]);
        while (grayCursor.hasNext()) {
            if (++planePixelCounter > planeSize) {
                planePixelCounter = 0;
                planeNo++;
                converter = new RealUnsignedByteConverter<>(minMax[0][planeNo - 1], minMax[1][planeNo - 1]);
            }
            converter.convert(grayCursor.next(), byteCursor.next());

        }

        return byteImg;
    }

    private static double[][] _getPlaneMinMax(final Image<UINT16> grayImage) {
        final double[][] minMax = ImageStatistics.getPlaneMinMax(grayImage);
        for (int i = 0; i < minMax[0].length; i++) {
            if (minMax[0][i] == minMax[1][i]) {
                minMax[1][i]++;
            }
        }
        return minMax;
    }

    private static Channel _getImgLib2ColorChannel(Color color) {
        switch (color) {
            case Red:
                return Channel.R;

            case Green:
                return Channel.G;

            case Blue:
                return Channel.B;

            default:
                return null;
        }

    }

}