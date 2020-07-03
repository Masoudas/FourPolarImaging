package fr.fresnel.fourPolar.core.image.generic.imgLib2Model;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.types.ConverterNotFound;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.types.TypeConverter;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.types.TypeConverterFactory;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.Pixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelTypes;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.ARGB8;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.integer.UnsignedShortType;

public class ImgLib2PixelCursorTest {
    @Test
    public void localize_UINT16Image_ReturnsAllPositionsInTheImage() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2 }).axisOrder(AxisOrder.XY).build();

        Image<UINT16> image = new ImgLib2ImageFactory().create(metadata, UINT16.zero());

        IPixelCursor<UINT16> cursor = image.getCursor();

        List<long[]> list = new ArrayList<>();

        while (cursor.hasNext()) {
            cursor.next();
            list.add(cursor.localize());
        }

        assertArrayEquals(new long[] { 0, 0 }, list.get(0));
        assertArrayEquals(new long[] { 1, 0 }, list.get(1));
        assertArrayEquals(new long[] { 0, 1 }, list.get(2));
        assertArrayEquals(new long[] { 1, 1 }, list.get(3));

    }

    @Test
    public void setPixel_UINT16Image_SetsPixelsToDefinedValues() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2 }).axisOrder(AxisOrder.XY).build();
        Image<UINT16> image = new ImgLib2ImageFactory().create(metadata, UINT16.zero());
        IPixelCursor<UINT16> cursor = image.getCursor();

        int value = 1;
        while (cursor.hasNext()) {
            cursor.next();
            cursor.setPixel(new Pixel<UINT16>(new UINT16(value)));
            value++;
        }

        cursor.reset();
        value = 1;
        boolean equals = true;
        while (cursor.hasNext()) {
            equals = cursor.next().value().get() == value++;
        }

        assertTrue(equals);
    }

    @Test
    public void setPixel_Float32Image_SetsPixelsToDefinedValues() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2 }).axisOrder(AxisOrder.XY).build();
        Image<Float32> image = new ImgLib2ImageFactory().create(metadata, Float32.zero());
        IPixelCursor<Float32> cursor = image.getCursor();

        float value = 1.1f;
        while (cursor.hasNext()) {
            cursor.next();
            cursor.setPixel(new Pixel<Float32>(new Float32(value)));
            value++;
        }

        cursor.reset();
        value = 1.1f;
        boolean equals = true;
        while (cursor.hasNext()) {
            equals = cursor.next().value().get() == value++;
        }

        assertTrue(equals);
    }

    @Test
    public void setPixel_RGB16Image_SetsPixelsToDefinedValues() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2 }).axisOrder(AxisOrder.XY).build();
        Image<ARGB8> image = new ImgLib2ImageFactory().create(metadata, ARGB8.zero());
        IPixelCursor<ARGB8> cursor = image.getCursor();

        int value = 0;
        while (cursor.hasNext()) {
            cursor.next();
            cursor.setPixel(new Pixel<ARGB8>(new ARGB8(++value, 0, 0)));
        }

        boolean equals = true;
        cursor.reset();
        value = 0;
        while (cursor.hasNext()) {
            equals = cursor.next().value().getR() == ++value;
        }

        assertTrue(equals);
    }

    @Test
    public void getCursor_IntervalCursor_ShouldGetAndSetProperly() throws ConverterNotFound {
        long[] dimensions = new long[] { 5, 1 };
        UnsignedShortType type = new UnsignedShortType();
        Img<UnsignedShortType> img = new ArrayImgFactory<UnsignedShortType>(type).create(dimensions);
        TypeConverter<UINT16, UnsignedShortType> converter = TypeConverterFactory.getConverter(type);

        IMetadata metadata = new Metadata.MetadataBuilder(dimensions).bitPerPixel(PixelTypes.UINT_16)
                .axisOrder(AxisOrder.XY).build();

        int pixelNumber = 3;
        int intervalLen = 2;
        ImgLib2Image<UINT16, UnsignedShortType> image = new ImgLib2Image<UINT16, UnsignedShortType>(img, converter,
                null, metadata);
        IPixelCursor<UINT16> cursor = image.getCursor(new long[] { pixelNumber, 0 }, new long[] { intervalLen, 1 });

        while (cursor.hasNext()) {
            cursor.next();
            System.out.println(cursor.localize()[0]);
            cursor.setPixel(new Pixel<UINT16>(new UINT16(3)));
        }

        IPixelCursor<UINT16> wholeImageCursor = image.getCursor();
        boolean equals = true;
        int counter = 0;
        while (wholeImageCursor.hasNext() && counter++ < 5) {
            if (counter > 3)
                equals &= wholeImageCursor.next().value().get() == 3;
            else
                equals &= wholeImageCursor.next().value().get() == 0;
        }

        assertTrue(equals && counter == 5);

    }

}