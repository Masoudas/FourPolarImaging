package fr.fresnel.fourPolar.io.image.generic.tiff.scifio;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.io.exceptions.image.generic.metadata.MetadataParseError;

public class SCIFIORGB16TiffReaderTest {

    @Test
    public void read_PreDefinedDiskRGBImg_ReadPixelsAreEqual() throws IOException, MetadataParseError {
        // File path = new File(SCIFIORGB16TiffReaderTest.class.getResource("Readers").getFile(), "RGB16Image.tif");

        // ImgLib2ImageFactory factory = new ImgLib2ImageFactory();

        // Image<RGB16> img = new SCIFIORGB16TiffReader(factory).read(path);

        // IPixelCursor<RGB16> cursor = img.getCursor();

        // boolean equals = true;
        // while (cursor.hasNext()) {
        //     RGB16 rgb16 = cursor.next().value();

        //     equals &= rgb16.getR() == 255 && rgb16.getG() == 0 && rgb16.getB() == 255;
        // }

        // assertTrue(equals);

        // assertTrue(img.getMetadata().axisOrder() == AxisOrder.XY
        //         && Arrays.equals(img.getMetadata().getDim(), new long[] { 2, 2 }));

    }

}