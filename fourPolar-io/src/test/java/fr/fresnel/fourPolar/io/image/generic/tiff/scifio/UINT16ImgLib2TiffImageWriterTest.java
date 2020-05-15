package fr.fresnel.fourPolar.io.image.generic.tiff.scifio;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelTypes;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.io.exceptions.image.generic.metadata.MetadataParseError;
import io.scif.FormatException;

public class UINT16ImgLib2TiffImageWriterTest {
    private static long[] _dim = { 10, 10, 2 };
    private static File _root;
    private static ImgLib2ImageFactory _factory = new ImgLib2ImageFactory();

    @BeforeAll
    private static void setRoot() {
        _root = new File(UINT16ImgLib2TiffImageWriterTest.class.getResource("Writers").getPath());
    }

    @Test
    public void write_UINT16Image_DiskImageHasSameDimensions() throws IOException, MetadataParseError {
        IMetadata metadata = new Metadata.MetadataBuilder(_dim).bitPerPixel(PixelTypes.UINT_16).build();
        File destination = new File(_root, "UnknownAxis.tif");

        Image<UINT16> image = _factory.create(metadata, UINT16.zero());
        UINT16SCIFIOTiffImageWriter writer = new UINT16SCIFIOTiffImageWriter();
        writer.write(destination, image);
        writer.close();

        
        // Image<UINT16> diskImage = new UINT16SCIFIOTiffImageReader(_factory).read(destination);

        // assertTrue(
        //     diskImage.getMetadata().getDim()[0] == _dim[0] && 
        //     diskImage.getMetadata().getDim()[1] == _dim[1] && 
        //     diskImage.getMetadata().getDim()[2] == _dim[2]
        // );
    }

    @Test
    public void write_WriteMultipleFiles_DiskImageHasSameDimensions() throws IOException {
        Image<UINT16> image = _factory.create(_dim, UINT16.zero());
        UINT16SCIFIOTiffImageWriter writer = new UINT16SCIFIOTiffImageWriter();
        File destination = new File(_root, "UINT16Image.tif");

        for (int i = 0; i < 10000; i++) {
            writer.write(destination, image);
        }
        writer.close();

        Image<UINT16> diskImage = new UINT16SCIFIOTiffImageReader(_factory).read(destination);

        assertTrue(
            diskImage.getMetadata().getDim()[0] == _dim[0] && 
            diskImage.getMetadata().getDim()[1] == _dim[1] && 
            diskImage.getMetadata().getDim()[2] == _dim[2]
        );

    }

    @Test
    public void write_WriteUShortImageWithMetadata_DiskImageHasSameDimensions() throws IOException, FormatException {

    }



}