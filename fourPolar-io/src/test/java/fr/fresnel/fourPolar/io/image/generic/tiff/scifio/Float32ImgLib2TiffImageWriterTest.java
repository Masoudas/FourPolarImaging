package fr.fresnel.fourPolar.io.image.generic.tiff.scifio;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;

public class Float32ImgLib2TiffImageWriterTest {
    private static long[] _dim = {10, 10};
    private static File _root;
    private static ImgLib2ImageFactory _factory = new ImgLib2ImageFactory();

    @BeforeAll
    private static void setRoot() {
        _root = new File(Float32ImgLib2TiffImageWriterTest.class.getResource("Writers").getPath());
    }

    @Test
    public void write_Float32Image_DiskImageHasSameDimensions() throws IOException {
        File destination = new File(_root, "Float32Image.tif");

        Image<Float32> image = _factory.create(_dim, Float32.zero());
        Float32ImgLib2TiffImageWriter writer = new Float32ImgLib2TiffImageWriter();
        writer.write(destination, image);
        writer.close();

        Image<Float32> diskImage = new Float32ImgLib2TiffImageReader(_factory).read(destination);

        assertTrue(
            diskImage.getDimensions()[0] == _dim[0] &&
            diskImage.getDimensions()[1] == _dim[1]
        );
    }

    @Test
    public void write_WriteMultipleFiles_DiskImageHasSameDimensions() throws IOException {
        Image<Float32> image = _factory.create(_dim, Float32.zero());
        Float32ImgLib2TiffImageWriter writer = new Float32ImgLib2TiffImageWriter();
        File destination = new File(_root, "Float32Image.tif");

        for (int i = 0; i < 10000; i++) {
            
            writer.write(destination, image);
        }
        writer.close();

        Image<Float32> diskImage = new Float32ImgLib2TiffImageReader(_factory).read(destination);

        assertTrue(
            diskImage.getDimensions()[0] == _dim[0] && 
            diskImage.getDimensions()[1] == _dim[1]
        );

    }

    @Test
    public void write_WriteUShortImageWithMetadata_DiskImageHasSameDimensions() throws IOException {

    }



}