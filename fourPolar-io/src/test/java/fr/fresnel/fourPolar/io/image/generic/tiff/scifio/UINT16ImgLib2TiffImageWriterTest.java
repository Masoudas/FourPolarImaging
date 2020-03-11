package fr.fresnel.fourPolar.io.image.generic.tiff.scifio;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import io.scif.FormatException;

public class UINT16ImgLib2TiffImageWriterTest {
    private static long[] _dim = {10, 10, 2};
    private static File _root;
    private static ImgLib2ImageFactory _factory = new ImgLib2ImageFactory();

    @BeforeAll
    private static void setRoot() {
        _root = new File(UINT16ImgLib2TiffImageWriterTest.class.getResource("Writers").getPath());
    }

    @Test
    public void write_UINT16Image_DiskImageHasSameDimensions() throws IOException {
        File destination = new File(_root, "UINT16Image.tif");

        Image<UINT16> image = _factory.create(_dim, new UINT16());
        UINT16ImgLib2TiffImageWriter writer = new UINT16ImgLib2TiffImageWriter();
        writer.write(destination, image);
        writer.close();

        
        Image<UINT16> diskImage = new UINT16ImgLib2TiffImageReader(_factory).read(destination);

        assertTrue(
            diskImage.getDimensions()[0] == _dim[0] && 
            diskImage.getDimensions()[1] == _dim[1] && 
            diskImage.getDimensions()[2] == _dim[2]
        );
    }

    @Test
    public void write_WriteMultipleFiles_DiskImageHasSameDimensions() throws IOException {
        Image<UINT16> image = _factory.create(_dim, new UINT16());
        UINT16ImgLib2TiffImageWriter writer = new UINT16ImgLib2TiffImageWriter();
        File destination = new File(_root, "UINT16Image.tif");

        for (int i = 0; i < 10000; i++) {
            writer.write(destination, image);
        }
        writer.close();

        Image<UINT16> diskImage = new UINT16ImgLib2TiffImageReader(_factory).read(destination);

        assertTrue(
            diskImage.getDimensions()[0] == _dim[0] && 
            diskImage.getDimensions()[1] == _dim[1] && 
            diskImage.getDimensions()[2] == _dim[2]
        );

    }

    @Test
    public void write_WriteUShortImageWithMetadata_DiskImageHasSameDimensions() throws IOException, FormatException {

    }



}