package fr.fresnel.fourPolar.io.image.generic.tiff.scifio;

import java.io.File;
import java.io.IOException;
import java.security.KeyException;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.imageSet.acquisition.IncompatibleCapturedImage;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import net.imglib2.img.Img;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.integer.UnsignedShortType;

public class Float32ImgLib2TiffImageReaderTest {
    private static File _testResource;

    @BeforeAll
    private static void setTestResouce() {
        _testResource = new File(Float32ImgLib2TiffImageReaderTest.class.getResource("Readers").getPath());
    }

    @Test
    public void read_UINT16Image_ShouldShowImage()
            throws IOException, InterruptedException {
        File path = new File(_testResource, "UINT16Image.tif");   
        UINT16ImgLib2TiffImageReader reader = new UINT16ImgLib2TiffImageReader(new ImgLib2ImageFactory());
        Image<UINT16> img = reader.read(path);
        
        ImageJFunctions.show((Img<UnsignedShortType>)img);
        TimeUnit.SECONDS.sleep(10);
                
    }

    @Test
    public void read_SameImageTenThousandTimes_ShouldNotRunOutOfResource()
            throws IllegalArgumentException, IOException, InterruptedException, KeyException, IncompatibleCapturedImage {
        File path = new File(_testResource, "UINT16Image.tif");   
        UINT16ImgLib2TiffImageReader reader = new UINT16ImgLib2TiffImageReader(new ImgLib2ImageFactory());
        
        for (int i = 0; i < 10000; i++) {
            reader.read(path);
        }
        
        reader.close();


    }


}