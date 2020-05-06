package fr.fresnel.fourPolar.io.image.generic.tiff.scifio;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;

public class RGB16ImgLib2TiffImageReaderTest {

    @Test
    public void read_PreDefinedDiskRGBImg_ReadPixelsAreEqual() throws IOException {
        File path = new File(RGB16ImgLib2TiffImageReaderTest.class.getResource("Readers").getFile(), "RGB16Image.tif");

        ImgLib2ImageFactory factory = new ImgLib2ImageFactory();

        Image<RGB16> img = new RGB16SCIFIOTiffImageReader(factory).read(path);

        IPixelCursor<RGB16> cursor = img.getCursor();

        boolean equals = true;
        while (cursor.hasNext()) {
            RGB16 rgb16 = cursor.next().value();
            
            equals &= rgb16.getR() == 255 && rgb16.getG() == 0 && rgb16.getB() == 255;
        }

        assertTrue(equals);
        
    }

}