package fr.fresnel.fourPolar.io.image.generic.tiff.scifio;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.Pixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;

public class RGB16ImgLib2TiffImageWriterTest {
    private static long[] _dim = {2, 2};
    private static File _root;
    private static ImgLib2ImageFactory _factory = new ImgLib2ImageFactory();

    private RGB16 _pixel = new RGB16(255, 0, 255);

    @BeforeAll
    private static void setRoot() {
        _root = new File(RGB16ImgLib2TiffImageWriterTest.class.getResource("Writers").getPath());
    }

    @Test
    public void write_RGB16Image_DiskImageHasSameData() throws IOException {
        File destination = new File(_root, "RGB16Image.tif");

        Image<RGB16> image = _factory.create(_dim, RGB16.zero());

        IPixelCursor<RGB16> cursor = image.getCursor();
        while (cursor.hasNext()) {
            cursor.next();
            cursor.setPixel(new Pixel<RGB16>(_pixel));
        }

        RGB16ImgLib2TiffImageWriter writer = new RGB16ImgLib2TiffImageWriter();
        
        writer.write(destination, image);
        writer.close();

        
        Image<RGB16> diskImage = new RGB16ImgLib2TiffImageReader(_factory).read(destination);

        boolean equals = true;
        IPixelCursor<RGB16> diskCursor = diskImage.getCursor();
        while (diskCursor.hasNext()) {
            RGB16 pixel = diskCursor.next().value();

            equals &= pixel.getR() == _pixel.getR() && pixel.getG() == _pixel.getG() &&
                pixel.getB() == _pixel.getB();
        }

        assertTrue(equals);
    }

}