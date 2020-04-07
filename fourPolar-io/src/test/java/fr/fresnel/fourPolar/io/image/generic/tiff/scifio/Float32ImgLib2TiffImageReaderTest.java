package fr.fresnel.fourPolar.io.image.generic.tiff.scifio;

import java.io.File;
import java.io.IOException;
import java.security.KeyException;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.ConverterToImgLib2NotFound;
import fr.fresnel.fourPolar.core.exceptions.imageSet.acquisition.IncompatibleCapturedImage;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImageToImgLib2Converter;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import net.imglib2.img.display.imagej.ImageJFunctions;

public class Float32ImgLib2TiffImageReaderTest {
    private static File _testResource;

    @BeforeAll
    private static void setTestResouce() {
        _testResource = new File(Float32ImgLib2TiffImageReaderTest.class.getResource("Readers").getPath());
    }

    @Test
    public void read_Float32Image_ShouldShowImage()
            throws IOException, InterruptedException, ConverterToImgLib2NotFound {
        File path = new File(_testResource, "Float32Image.tif");
        Float32ImgLib2TiffImageReader reader = new Float32ImgLib2TiffImageReader(new ImgLib2ImageFactory());
        Image<Float32> img = reader.read(path);

        ImageJFunctions.show(ImageToImgLib2Converter.getImg(img, Float32.zero()));
        TimeUnit.SECONDS.sleep(10);
    }

    @Test
    public void read_SameImageTenThousandTimes_ShouldNotRunOutOfResource() throws IllegalArgumentException, IOException,
            InterruptedException, KeyException, IncompatibleCapturedImage {
        File path = new File(_testResource, "Float32Image.tif");
        Float32ImgLib2TiffImageReader reader = new Float32ImgLib2TiffImageReader(new ImgLib2ImageFactory());

        for (int i = 0; i < 10000; i++) {
            reader.read(path);
        }

        reader.close();

    }

}