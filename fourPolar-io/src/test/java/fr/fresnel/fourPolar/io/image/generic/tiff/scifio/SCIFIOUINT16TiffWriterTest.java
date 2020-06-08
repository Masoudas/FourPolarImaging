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
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelTypes;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.io.exceptions.image.generic.metadata.MetadataParseError;
import fr.fresnel.fourPolar.io.image.generic.tiff.scifio.metadata.SCIFIOMetadataReader;

public class SCIFIOUINT16TiffWriterTest {
    private static File _root;
    private static ImgLib2ImageFactory _factory = new ImgLib2ImageFactory();

    @BeforeAll
    private static void setRoot() {
        _root = new File(SCIFIOUINT16TiffWriterTest.class.getResource("").getPath(), "Writers");
        _root.mkdir();
    }

    @Test
    public void write_UINT16XYImage_DiskImageHasSameMetadata() throws IOException, MetadataParseError {
        long[] dim = { 2, 2 };

        File destination = new File(_root, "UINT16XYImage.tif");

        IMetadata metadata = new Metadata.MetadataBuilder(dim).axisOrder(AxisOrder.XY).bitPerPixel(PixelTypes.FLOAT_32)
                .build();
        Image<UINT16> image = _factory.create(metadata, UINT16.zero());

        SCIFIOUINT16TiffWriter writer = new SCIFIOUINT16TiffWriter();
        writer.write(destination, image);
        writer.close();

        SCIFIOMetadataReader metadataReader = new SCIFIOMetadataReader();
        IMetadata diskMetadata = metadataReader.read(destination);

        assertArrayEquals(diskMetadata.getDim(), metadata.getDim());
        assertTrue(diskMetadata.axisOrder() == metadata.axisOrder());
    }

    @Test
    public void write_UINT16XYCZTImage_DiskImageHasSameMetadata() throws IOException, MetadataParseError {
        long[] dim = { 10, 10, 1, 2, 2 };

        File destination = new File(_root, "UINT16XYCZTImage.tif");

        IMetadata metadata = new Metadata.MetadataBuilder(dim).axisOrder(AxisOrder.XYCZT)
                .bitPerPixel(PixelTypes.FLOAT_32).build();
        Image<UINT16> image = _factory.create(metadata, UINT16.zero());

        SCIFIOUINT16TiffWriter writer = new SCIFIOUINT16TiffWriter();
        writer.write(destination, image);
        writer.close();

        SCIFIOMetadataReader metadataReader = new SCIFIOMetadataReader();
        IMetadata diskMetadata = metadataReader.read(destination);

        assertArrayEquals(diskMetadata.getDim(), metadata.getDim());
        assertTrue(diskMetadata.axisOrder() == metadata.axisOrder());
    }

    // @Test
    // public void write_UINT16XYCZTImageSingleZAndT_DiskImageHasSameMetadata()
    // throws IOException, MetadataParseError {
    // long[] dim = { 10, 10, 1, 1, 1 };

    // File destination = new File(_root, "UINT16Image.tif");

    // IMetadata metadata = new
    // Metadata.MetadataBuilder(dim).axisOrder(AxisOrder.XYCZT)
    // .bitPerPixel(PixelTypes.FLOAT_32).build();
    // Image<UINT16> image = _factory.create(metadata, UINT16.zero());

    // UINT16SCIFIOTiffImageWriter writer = new UINT16SCIFIOTiffImageWriter();
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
        Image<UINT16> image = _factory.create(metadata, UINT16.zero());

        SCIFIOUINT16TiffWriter writer = new SCIFIOUINT16TiffWriter();
        File destination = new File(_root, "UINT16Image.tif");

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