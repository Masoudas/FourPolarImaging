package fr.fresnel.fourPolar.core.image.generic.imgLib2Model;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.ConverterToImgLib2NotFound;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.ARGB8;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.FloatType;

public class ImgLib2ImageFactoryTest {
    @Test
    public void createByDimension_UINT16Image_CreatesImageOfSameDimension() throws ConverterToImgLib2NotFound {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 1, 1, 1, 1, 1 }).axisOrder(AxisOrder.XYZCT)
                .build();
        Image<UINT16> image = new ImgLib2ImageFactory().create(metadata, UINT16.zero());

        long[] dimensions = metadata.getDim().clone();
        ImageToImgLib2Converter.getImg(image, UINT16.zero()).dimensions(dimensions);

        assertArrayEquals(image.getMetadata().getDim(), dimensions);
    }

    @Test
    public void createByDimension_Float32Image_CreatesImageOfSameDimension() throws ConverterToImgLib2NotFound {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 1, 1, 1, 1, 1 }).axisOrder(AxisOrder.XYZCT)
                .build();
        Image<Float32> image = new ImgLib2ImageFactory().create(metadata, Float32.zero());

        long[] dimensions = metadata.getDim().clone();
        ImageToImgLib2Converter.getImg(image, Float32.zero()).dimensions(dimensions);

        assertArrayEquals(image.getMetadata().getDim(), dimensions);
    }

    @Test
    public void createByDimension_RGBImage_CreatesImageOfSameDimension() throws ConverterToImgLib2NotFound {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 1, 1, 1, 1, }).axisOrder(AxisOrder.XYZT).build();
        Image<ARGB8> image = new ImgLib2ImageFactory().create(metadata, ARGB8.zero());

        long[] dimensions = metadata.getDim().clone();
        ImageToImgLib2Converter.getImg(image, ARGB8.zero()).dimensions(dimensions);

        assertArrayEquals(image.getMetadata().getDim(), dimensions);
    }

    @Test
    public void createByDimension_RGBImageWithChannel_ThrowsIllegalArgumentException()
            throws ConverterToImgLib2NotFound {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 1, 1, 1, 1, }).axisOrder(AxisOrder.XYCT).build();

        assertThrows(IllegalArgumentException.class, () -> {
            new ImgLib2ImageFactory().create(metadata, ARGB8.zero());
        });
    }

    @Test
    public void checkImageType_SmallDimension_CreatesAnArrayImage() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 1, 1, 1, 1, }).axisOrder(AxisOrder.XYCT).build();
        Image<UINT16> image = new ImgLib2ImageFactory().create(metadata, UINT16.zero());

        assertTrue(image.toString().contains("ArrayImg"));
    }

    @Test
    public void checkImageType_LargeDimension_CreatesACellImage() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 1024, 1024, 1024, 2 }).axisOrder(AxisOrder.XYCT)
                .build();
        Image<UINT16> image = new ImgLib2ImageFactory().create(metadata, UINT16.zero());

        assertTrue(image.toString().contains("CellImg"));
    }

    @Test
    public void createFromImgInterface_UnsignedShortType_CreatesUINT16ImageWithSameDimension()
            throws ConverterToImgLib2NotFound {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 1, 1, 1, 1 }).axisOrder(AxisOrder.XYCT).build();

        UnsignedShortType type = new UnsignedShortType();
        Img<UnsignedShortType> img = new ArrayImgFactory<UnsignedShortType>(type).create(metadata.getDim());
        Image<UINT16> image = new ImgLib2ImageFactory().create(img, type, metadata);

        long[] dimensions = metadata.getDim().clone();
        ImageToImgLib2Converter.getImg(image, UINT16.zero()).dimensions(dimensions);

        assertArrayEquals(image.getMetadata().getDim(), dimensions);
    }

    @Test
    public void createFromImgInterface_FloatType_CreatesFloat32ImageWithSameDimension()
            throws ConverterToImgLib2NotFound {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 1, 1, 1, 1 }).axisOrder(AxisOrder.XYCT).build();

        FloatType type = new FloatType();
        Img<FloatType> img = new ArrayImgFactory<FloatType>(type).create(metadata.getDim());
        Image<Float32> image = new ImgLib2ImageFactory().create(img, type, metadata);

        long[] dimensions = metadata.getDim().clone();
        ImageToImgLib2Converter.getImg(image, Float32.zero()).dimensions(dimensions);

        assertArrayEquals(image.getMetadata().getDim(), dimensions);
    }

    @Test
    public void createFromImgInterface_ARGBType_CreatesRGB16ImageWithSameDimension() throws ConverterToImgLib2NotFound {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 1, 1, 1, 1 }).axisOrder(AxisOrder.XYZT).build();

        ARGBType type = new ARGBType();
        Img<ARGBType> img = new ArrayImgFactory<ARGBType>(type).create(metadata.getDim());
        Image<ARGB8> image = new ImgLib2ImageFactory().create(img, type, metadata);

        long[] dimensions = metadata.getDim().clone();
        ImageToImgLib2Converter.getImg(image, ARGB8.zero()).dimensions(dimensions);

        assertArrayEquals(image.getMetadata().getDim(), dimensions);
    }

    @Test
    public void createFromImgInterface_ARGBTypeWithChannel_ThrowsIllegalArgumentException()
            throws ConverterToImgLib2NotFound {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 1, 1, 1, 1 }).axisOrder(AxisOrder.XYCT).build();

        ARGBType type = new ARGBType();
        Img<ARGBType> img = new ArrayImgFactory<ARGBType>(type).create(metadata.getDim());
        assertThrows(IllegalArgumentException.class, () -> {
            new ImgLib2ImageFactory().create(img, type, metadata);
        });

    }

}