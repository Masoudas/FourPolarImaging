package fr.fresnel.fourPolar.io.image.captured;

import java.io.File;
import java.io.IOException;
import java.security.KeyException;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.ConverterToImgLib2NotFound;
import fr.fresnel.fourPolar.core.exceptions.imageSet.acquisition.IncompatibleCapturedImage;
import fr.fresnel.fourPolar.core.image.captured.ICapturedImage;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImageToImgLib2Converter;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.imageSet.acquisition.sample.SampleImageSet;
import fr.fresnel.fourPolar.core.imagingSetup.FourPolarImagingSetup;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.io.exceptions.image.generic.NoReaderFoundForImage;
import fr.fresnel.fourPolar.io.image.captured.tiff.TiffCapturedImageChecker;
import fr.fresnel.fourPolar.io.image.captured.tiff.TiffCapturedImageReader;
import net.imglib2.img.display.imagej.ImageJFunctions;

public class TiffCapturedImageReaderTest {
    private static File _testResource;

    @BeforeAll
    private static void setTestResouce() {
        _testResource = new File(TiffCapturedImageReaderTest.class.getResource("TiffCapturedImageReader").getPath());
    }

    @Test
    public void read_16bitTiff_ShouldShowImage() throws IllegalArgumentException, IOException, InterruptedException,
            KeyException, IncompatibleCapturedImage, NoReaderFoundForImage, ConverterToImgLib2NotFound {
        final File pol0_45_90_135 = new File(_testResource, "16bit.tif");

        FourPolarImagingSetup setup = new FourPolarImagingSetup(1, Cameras.One);
        final SampleImageSet sImageSet = new SampleImageSet(setup, new TiffCapturedImageChecker());
        sImageSet.addImage(1, pol0_45_90_135);

        ImageFactory factory = new ImgLib2ImageFactory();

        final TiffCapturedImageReader imgReader = new TiffCapturedImageReader(factory);

        final ICapturedImage capturedImage = imgReader.read(sImageSet.getChannelImages(1).get(0),
                Cameras.getLabels(Cameras.One)[0]);

        ImageJFunctions.show(ImageToImgLib2Converter.getImg(capturedImage.getImage(), UINT16.zero()));
        TimeUnit.SECONDS.sleep(10);

    }
}