package fr.fresnel.fourPolar.core.image.generic.imgLib2Model;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.types.ConverterNotFound;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.types.TypeConverter;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.types.TypeConverterFactory;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.Pixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelTypes;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.integer.UnsignedShortType;

public class ImgLib2ImageTest {
    @Test
    public void dimensions_UnsignedShortImage_ReturnsCorrectSize() throws ConverterNotFound {
        long[] dimensions = new long[] { 1, 1, 1, 1 };
        UnsignedShortType type = new UnsignedShortType();
        Img<UnsignedShortType> img = new ArrayImgFactory<UnsignedShortType>(type).create(dimensions);
        TypeConverter<UINT16, UnsignedShortType> converter = TypeConverterFactory.getConverter(UINT16.zero(), type);
        IMetadata metadata = new Metadata.MetadataBuilder(dimensions).bitPerPixel(PixelTypes.UINT_16)
                .axisOrder(AxisOrder.XYCT).build();
        ImgLib2Image<UINT16, UnsignedShortType> image = new ImgLib2Image<UINT16, UnsignedShortType>(img, converter,
                null, metadata);

        assertArrayEquals(dimensions, image.getMetadata().getDim());
    }

    @Test
    public void getCursor_UnsignedShortImage_HasOneElement() throws ConverterNotFound {
        long[] dimensions = new long[] { 1, 1, 1, 1 };
        UnsignedShortType type = new UnsignedShortType();
        Img<UnsignedShortType> img = new ArrayImgFactory<UnsignedShortType>(type).create(dimensions);
        TypeConverter<UINT16, UnsignedShortType> converter = TypeConverterFactory.getConverter(UINT16.zero(), type);
        IMetadata metadata = new Metadata.MetadataBuilder(dimensions).bitPerPixel(PixelTypes.UINT_16)
                .axisOrder(AxisOrder.XYCT).build();
        ImgLib2Image<UINT16, UnsignedShortType> image = new ImgLib2Image<UINT16, UnsignedShortType>(img, converter,
                null, metadata);

        int counter = 0;
        for (IPixelCursor<UINT16> i = image.getCursor(); i.hasNext(); i.next()) {
            counter++;
        }
        assertTrue(counter == 1);
    }

    @Test
    public void getCursor_IntervalCursor_ShouldGetAndSetProperly() throws ConverterNotFound {
        long[] dimensions = new long[] { 5, 1 };
        UnsignedShortType type = new UnsignedShortType();
        Img<UnsignedShortType> img = new ArrayImgFactory<UnsignedShortType>(type).create(dimensions);
        TypeConverter<UINT16, UnsignedShortType> converter = TypeConverterFactory.getConverter(UINT16.zero(), type);
        IMetadata metadata = new Metadata.MetadataBuilder(dimensions).bitPerPixel(PixelTypes.UINT_16)
                .axisOrder(AxisOrder.XY).build();

        int pixelNumber = 2;
        int intervalLen = 2;
        ImgLib2Image<UINT16, UnsignedShortType> image = new ImgLib2Image<UINT16, UnsignedShortType>(img, converter,
                null, metadata);
        IPixelCursor<UINT16> cursor = image.getCursor(new long[] { pixelNumber, 0 }, new long[] { intervalLen, 0 });

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
