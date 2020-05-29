package fr.fresnel.fourPolar.io.image.generic.tiff.scifio;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.security.KeyException;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.ConverterToImgLib2NotFound;
import fr.fresnel.fourPolar.core.exceptions.imageSet.acquisition.IncompatibleCapturedImage;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.io.exceptions.image.generic.metadata.MetadataParseError;

public class SCIFIOFloat32TiffReaderTest {
    private static File _testResource;

    @BeforeAll
    private static void setTestResouce() {
        _testResource = new File(SCIFIOFloat32TiffReaderTest.class.getResource("Readers").getPath());
    }

    @Test
    public void read_Float32XYImage_ShouldShowImage()
            throws IOException, InterruptedException, ConverterToImgLib2NotFound, MetadataParseError {
        File path = new File(_testResource, "Float32XYImage.tif");
        SCIFIOFloat32TiffReader reader = new SCIFIOFloat32TiffReader(new ImgLib2ImageFactory());
        Image<Float32> img = reader.read(path);

        assertTrue(img.getMetadata().axisOrder() == AxisOrder.XY
                && Arrays.equals(img.getMetadata().getDim(), new long[] { 10, 10 }));
    }

    @Test
    public void read_Float32XYCZTImage_ShouldShowImage()
            throws IOException, InterruptedException, ConverterToImgLib2NotFound, MetadataParseError {
        File path = new File(_testResource, "Float32XYCZTImage.tif");
        SCIFIOFloat32TiffReader reader = new SCIFIOFloat32TiffReader(new ImgLib2ImageFactory());
        Image<Float32> img = reader.read(path);

        assertTrue(img.getMetadata().axisOrder() == AxisOrder.XYCZT
                && Arrays.equals(img.getMetadata().getDim(), new long[] { 10, 10, 1, 2, 2 }));
    }

    @Test
    public void read_SameImageTenThousandTimes_ShouldNotRunOutOfResource() throws IllegalArgumentException, IOException,
            InterruptedException, KeyException, IncompatibleCapturedImage, MetadataParseError {
        File path = new File(_testResource, "Float32XYImage.tif");
        SCIFIOFloat32TiffReader reader = new SCIFIOFloat32TiffReader(new ImgLib2ImageFactory());

        Image<Float32> img = null;
        for (int i = 0; i < 10000; i++) {
            img = reader.read(path);
        }
        reader.close();

        assertTrue(img.getMetadata().axisOrder() == AxisOrder.XY
                && Arrays.equals(img.getMetadata().getDim(), new long[] { 10, 10 }));

    }

}