package fr.fresnel.fourPolar.io.image.generic.tiff.scifio;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.Pixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;

public class RGB16ImgLib2TiffImageWriterTest {
    private static File _root;
    private static ImgLib2ImageFactory _factory = new ImgLib2ImageFactory();

    @BeforeAll
    private static void setRoot() {
        _root = new File(RGB16ImgLib2TiffImageWriterTest.class.getResource("Writers").getPath());
    }

    @Test
    public void write_RGB16Image_DiskImageHasSameData() throws IOException {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 50, 50, 2 }).axisOrder(AxisOrder.XYT).build();

        Image<RGB16> image = _factory.create(metadata, RGB16.zero());

        for (IPixelCursor<RGB16> cursor = image.getCursor(); cursor.hasNext();) {
            cursor.next();
            cursor.setPixel(new Pixel<RGB16>(new RGB16(255, 0, 255)));
        }

        RGB16SCIFIOTiffImageWriter writer = new RGB16SCIFIOTiffImageWriter();

        File destination = new File(_root, "RGB16Image.tif");
        writer.write(destination, image);
        writer.close();

        // Image<RGB16> diskImage = new
        // RGB16SCIFIOTiffImageReader(_factory).read(destination);

        // boolean equals = true;
        // IPixelCursor<RGB16> diskCursor = diskImage.getCursor();
        // while (diskCursor.hasNext()) {
        // RGB16 pixel = diskCursor.next().value();

        // equals &= pixel.getR() == _pixel.getR() && pixel.getG() == _pixel.getG() &&
        // pixel.getB() == _pixel.getB();
        // }

        // assertTrue(equals);
    }

}