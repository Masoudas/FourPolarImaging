package fr.fresnel.fourPolar.algorithm.util.image.color;

import java.util.Objects;

import fr.fresnel.fourPolar.algorithm.util.image.color.GrayScaleToColorConverter.Color;
import fr.fresnel.fourPolar.algorithm.util.image.stats.ImageStatistics;
import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.ConverterToImgLib2NotFound;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImageToImgLib2Converter;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.metadata.MetadataUtil;
import fr.fresnel.fourPolar.core.image.generic.pixel.IPixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelTypes;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
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
 * Merge two single color images
 */
class GrayImagesToMonoColorMerger {
    public GrayImagesToMonoColorMerger() {
        new AssertionError();
    }

    public static Image<RGB16> convert(Image<UINT16> image1, Color color1, Image<UINT16> image2, Color color2) {
        Objects.requireNonNull(image1);
        Objects.requireNonNull(image2);
        Objects.requireNonNull(color1);
        Objects.requireNonNull(color2);

        MetadataUtil.isAxisOrderEqual(image1.getMetadata(), image2.getMetadata());
        MetadataUtil.isDimensionEqual(image1.getMetadata(), image2.getMetadata());

        RandomAccess<ARGBType> image1AsARGB = _createMonoColorCopy(image1, color1);
        RandomAccess<ARGBType> image2AsARGB = _createMonoColorCopy(image2, color2);

        Image<RGB16> monochromeImage = _createOutputRGBImage(image1);
        _fillMonochromImage(monochromeImage, image1AsARGB, image2AsARGB);

        return monochromeImage;
    }

    private static RandomAccess<ARGBType> _createMonoColorCopy(Image<UINT16> image, Color color) {
        Channel channelColor = _getImgLib2ColorChannel(color);

        RandomAccessible<UnsignedByteType> unsignedByteImgRA = _getUnsignedByteImage(image);

        Converter<UnsignedByteType, ARGBType> toColorConverter = new ChannelARGBConverter(channelColor);
        return Converters.convert(unsignedByteImgRA, toColorConverter, new ARGBType()).randomAccess();
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

    private static Image<RGB16> _createOutputRGBImage(Image<UINT16> image) {
        IMetadata metadata = new Metadata.MetadataBuilder(image.getMetadata()).bitPerPixel(PixelTypes.RGB_16).build();

        return image.getFactory().create(metadata, RGB16.zero());
    }

    private static void _fillMonochromImage(Image<RGB16> monochromImage, RandomAccess<ARGBType> raImage1RGB,
            RandomAccess<ARGBType> raImage2RGB) {
        for (IPixelCursor<RGB16> monochromeCursor = monochromImage.getCursor(); monochromeCursor.hasNext();) {
            IPixel<RGB16> pixel = monochromeCursor.next();
            final long[] position = monochromeCursor.localize();

            raImage1RGB.setPosition(position);
            raImage2RGB.setPosition(position);

            ARGBType sumColors = raImage1RGB.get();
            sumColors.add(raImage2RGB.get());

            pixel.value().set(ARGBType.red(sumColors.get()), ARGBType.green(sumColors.get()),
                    ARGBType.blue(sumColors.get()));

            monochromeCursor.setPixel(pixel);

        }
    }
}