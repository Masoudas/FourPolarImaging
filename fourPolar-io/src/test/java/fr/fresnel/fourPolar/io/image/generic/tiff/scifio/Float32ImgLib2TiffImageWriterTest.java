package fr.fresnel.fourPolar.io.image.generic.tiff.scifio;

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

public class Float32ImgLib2TiffImageWriterTest {
    private static long[] _dim = { 10, 10, 1, 3, 3 };
    private static File _root;
    private static ImgLib2ImageFactory _factory = new ImgLib2ImageFactory();

    @BeforeAll
    private static void setRoot() {
        _root = new File(Float32ImgLib2TiffImageWriterTest.class.getResource("Writers").getPath());
    }

    @Test
    public void write_Float32Image_DiskImageHasSameDimensions() throws IOException, MetadataParseError {
        File destination = new File(_root, "Float32Image.tif");

        IMetadata metadata = new Metadata.MetadataBuilder(_dim).axisOrder(AxisOrder.XYCZT)
                .bitPerPixel(PixelTypes.FLOAT_32).build();
        Image<Float32> image = _factory.create(metadata, Float32.zero());
        
        Float32SCIFIOTiffImageWriter writer = new Float32SCIFIOTiffImageWriter();
        writer.write(destination, image);
        writer.close();

        Image<Float32> diskImage = new Float32SCIFIOTiffImageReader(_factory).read(destination);

        assertTrue(diskImage.getMetadata().getDim()[0] == _dim[0] && diskImage.getMetadata().getDim()[1] == _dim[1]);
    }

    @Test
    public void write_WriteMultipleFiles_DiskImageHasSameDimensions() throws IOException {
        Image<Float32> image = _factory.create(_dim, Float32.zero());
        Float32SCIFIOTiffImageWriter writer = new Float32SCIFIOTiffImageWriter();
        File destination = new File(_root, "Float32Image.tif");

        for (int i = 0; i < 10000; i++) {

            writer.write(destination, image);
        }
        writer.close();

        Image<Float32> diskImage = new Float32SCIFIOTiffImageReader(_factory).read(destination);

        assertTrue(diskImage.getMetadata().getDim()[0] == _dim[0] && diskImage.getMetadata().getDim()[1] == _dim[1]);

    }

}