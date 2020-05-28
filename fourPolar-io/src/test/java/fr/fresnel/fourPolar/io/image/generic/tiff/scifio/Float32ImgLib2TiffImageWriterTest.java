package fr.fresnel.fourPolar.io.image.generic.tiff.scifio;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelTypes;
import fr.fresnel.fourPolar.io.exceptions.image.generic.metadata.MetadataParseError;
import fr.fresnel.fourPolar.io.image.generic.tiff.scifio.metadata.SCIFIOMetadataReader;

public class Float32ImgLib2TiffImageWriterTest {
    private static File _root;
    private static ImgLib2ImageFactory _factory = new ImgLib2ImageFactory();

    @BeforeAll
    private static void setRoot() {
        _root = new File(Float32ImgLib2TiffImageWriterTest.class.getResource("Writers").getPath());
    }

    @Test
    public void write_Float32XYImage_DiskImageHasSameMetadata() throws IOException, MetadataParseError {
        long[] dim = { 10, 10 };

        File destination = new File(_root, "Float32Image.tif");

        IMetadata metadata = new Metadata.MetadataBuilder(dim).axisOrder(AxisOrder.XY).bitPerPixel(PixelTypes.FLOAT_32)
                .build();
        Image<Float32> image = _factory.create(metadata, Float32.zero());

        Float32SCIFIOTiffImageWriter writer = new Float32SCIFIOTiffImageWriter();
        writer.write(destination, image);
        writer.close();

        SCIFIOMetadataReader metadataReader = new SCIFIOMetadataReader();
        IMetadata diskMetadata = metadataReader.read(destination);

        assertArrayEquals(diskMetadata.getDim(), metadata.getDim());
        assertTrue(diskMetadata.axisOrder() == metadata.axisOrder());
    }

    @Test
    public void write_Float32XYCZTImage_DiskImageHasSameMetadata() throws IOException, MetadataParseError {
        long[] dim = { 10, 10, 1, 2, 2 };

        File destination = new File(_root, "Float32Image.tif");

        IMetadata metadata = new Metadata.MetadataBuilder(dim).axisOrder(AxisOrder.XYCZT)
                .bitPerPixel(PixelTypes.FLOAT_32).build();
        Image<Float32> image = _factory.create(metadata, Float32.zero());

        Float32SCIFIOTiffImageWriter writer = new Float32SCIFIOTiffImageWriter();
        writer.write(destination, image);
        writer.close();

        SCIFIOMetadataReader metadataReader = new SCIFIOMetadataReader();
        IMetadata diskMetadata = metadataReader.read(destination);

        assertArrayEquals(diskMetadata.getDim(), metadata.getDim());
        assertTrue(diskMetadata.axisOrder() == metadata.axisOrder());
    }

    // @Test
    // public void write_Float32XYCZTImageSingleZAndT_DiskImageHasSameMetadata()
    // throws IOException, MetadataParseError {
    // long[] dim = { 10, 10, 1, 1, 1 };

    // File destination = new File(_root, "Float32Image.tif");

    // IMetadata metadata = new
    // Metadata.MetadataBuilder(dim).axisOrder(AxisOrder.XYCZT)
    // .bitPerPixel(PixelTypes.FLOAT_32).build();
    // Image<Float32> image = _factory.create(metadata, Float32.zero());

    // Float32SCIFIOTiffImageWriter writer = new Float32SCIFIOTiffImageWriter();
    // writer.write(destination, image);
    // writer.close();

    // SCIFIOMetadataReader metadataReader = new SCIFIOMetadataReader();
    // IMetadata diskMetadata = metadataReader.read(destination);

    // assertArrayEquals(diskMetadata.getDim(), metadata.getDim());
    // assertTrue(diskMetadata.axisOrder() == metadata.axisOrder());
    // }

    @Test
    public void write_WriteMultipleFiles_DiskImageHasSameMetadata() throws IOException, MetadataParseError {
        long[] dim = { 10, 10, 1, 2, 2 };

        IMetadata metadata = new Metadata.MetadataBuilder(dim).axisOrder(AxisOrder.XYCZT)
                .bitPerPixel(PixelTypes.FLOAT_32).build();
        Image<Float32> image = _factory.create(metadata, Float32.zero());

        Float32SCIFIOTiffImageWriter writer = new Float32SCIFIOTiffImageWriter();
        File destination = new File(_root, "Float32Image.tif");

        for (int i = 0; i < 10000; i++) {

            writer.write(destination, image);
        }
        writer.close();

        SCIFIOMetadataReader metadataReader = new SCIFIOMetadataReader();
        IMetadata diskMetadata = metadataReader.read(destination);

        assertArrayEquals(diskMetadata.getDim(), metadata.getDim());
        assertTrue(diskMetadata.axisOrder() == metadata.axisOrder());

    }

}