package fr.fresnel.fourPolar.core.image.generic.imgLib2Model;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.types.ConverterNotFound;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.types.TypeConverter;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.types.TypeConverterFactory;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.Pixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Type;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.FloatType;

public class ImgLib2ImageTest {
    @Test
    public void dimensions_UnsignedShortImage_AllMethodsShouldReturnTheSameResult() throws ConverterNotFound {
        long[] dimensions = new long[] { 1, 1, 1, 1 };
        UnsignedShortType type = new UnsignedShortType();
        Img<UnsignedShortType> img = new ArrayImgFactory<UnsignedShortType>(type).create(dimensions);
        TypeConverter<UINT16, UnsignedShortType> converter = TypeConverterFactory.getConverter(UINT16.zero(), type);
        IMetadata metadata = new Metadata.MetadataBuilder().build();
        ImgLib2Image<UINT16, UnsignedShortType> image = new ImgLib2Image<UINT16, UnsignedShortType>(img, converter,
                null, metadata);

        assertArrayEquals(dimensions, image.getDimensions());
    }

    @Test
    public void getPixelType_UnsignedShortImage_ImageTypeIsUINT16() throws ConverterNotFound {
        long[] dimensions = new long[] { 1 };
        UnsignedShortType type = new UnsignedShortType();
        Img<UnsignedShortType> img = new ArrayImgFactory<UnsignedShortType>(type).create(dimensions);
        TypeConverter<UINT16, UnsignedShortType> converter = TypeConverterFactory.getConverter(UINT16.zero(), type);
        IMetadata metadata = new Metadata.MetadataBuilder().build();

        ImgLib2Image<UINT16, UnsignedShortType> image = new ImgLib2Image<UINT16, UnsignedShortType>(img, converter,
                null, metadata);

        assertTrue(image.getPixelType() == Type.UINT_16);
    }

    @Test
    public void getPixelType_FloatTypeImage_ImageTypeIsFloat32() throws ConverterNotFound {
        long[] dimensions = new long[] { 1 };
        FloatType type = new FloatType();
        Img<FloatType> img = new ArrayImgFactory<FloatType>(type).create(dimensions);
        TypeConverter<Float32, FloatType> converter = TypeConverterFactory.getConverter(Float32.zero(), type);
        IMetadata metadata = new Metadata.MetadataBuilder().build();

        ImgLib2Image<Float32, FloatType> image = new ImgLib2Image<Float32, FloatType>(img, converter, null, metadata);

        assertTrue(image.getPixelType() == Type.FLOAT_32);
    }

    @Test
    public void getPixelType_ARGBTypeImage_ImageTypeIsRGB16() throws ConverterNotFound {
        long[] dimensions = new long[] { 1 };
        ARGBType type = new ARGBType();
        Img<ARGBType> img = new ArrayImgFactory<ARGBType>(type).create(dimensions);
        TypeConverter<RGB16, ARGBType> converter = TypeConverterFactory.getConverter(RGB16.zero(), type);
        IMetadata metadata = new Metadata.MetadataBuilder().build();

        ImgLib2Image<RGB16, ARGBType> image = new ImgLib2Image<RGB16, ARGBType>(img, converter, null, metadata);

        assertTrue(image.getPixelType() == Type.RGB_16);
    }

    @Test
    public void getCursor_IntervalCursor_ShouldGetAndSetProperly() throws ConverterNotFound {
        long[] dimensions = new long[] { 5 };
        UnsignedShortType type = new UnsignedShortType();
        Img<UnsignedShortType> img = new ArrayImgFactory<UnsignedShortType>(type).create(dimensions);
        TypeConverter<UINT16, UnsignedShortType> converter = TypeConverterFactory.getConverter(UINT16.zero(), type);
        IMetadata metadata = new Metadata.MetadataBuilder().build();

        int pixelNumber = 2;
        int intervalLen = 2;
        ImgLib2Image<UINT16, UnsignedShortType> image = new ImgLib2Image<UINT16, UnsignedShortType>(img, converter,
                null, metadata);
        IPixelCursor<UINT16> cursor = image.getCursor(new long[] { pixelNumber }, new long[] { intervalLen });

        int counter = 0;
        while (cursor.hasNext()) {
            System.out.println(counter);
            cursor.next();
            cursor.setPixel(new Pixel<UINT16>(new UINT16(3)));
        }

        IPixelCursor<UINT16> wholeImageCursor = image.getCursor();
        boolean equals = true;
        counter = 0;
        while (wholeImageCursor.hasNext()) {
            if (counter++ > 2 && counter < 5)
                equals = wholeImageCursor.next().value().get() == 3;
            else
                equals = wholeImageCursor.next().value().get() == 0;
        }

        assertTrue(equals);

    }

}
