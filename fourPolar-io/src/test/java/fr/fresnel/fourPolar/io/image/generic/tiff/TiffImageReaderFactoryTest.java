package fr.fresnel.fourPolar.io.image.generic.tiff;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.io.exceptions.image.generic.NoReaderFoundForImage;
import fr.fresnel.fourPolar.io.image.generic.ImageReader;
import fr.fresnel.fourPolar.io.image.generic.tiff.scifio.Float32ImgLib2TiffImageReader;
import fr.fresnel.fourPolar.io.image.generic.tiff.scifio.RGB16ImgLib2TiffImageReader;
import fr.fresnel.fourPolar.io.image.generic.tiff.scifio.UINT16ImgLib2TiffImageReader;

public class TiffImageReaderFactoryTest {

    @Test
    public void getReader_UINT16ImgLib2Implementation_ReturnsCorrectWriter() throws NoReaderFoundForImage {
        ImageReader<UINT16> reader = TiffImageReaderFactory.getReader(new ImgLib2ImageFactory(), UINT16.zero());

        assertTrue(reader instanceof UINT16ImgLib2TiffImageReader);
    }
    
    @Test
    public void getReader_Float32ImgLib2Implementation_ReturnsCorrectWriter() throws NoReaderFoundForImage {
        ImageReader<Float32> reader = TiffImageReaderFactory.getReader(new ImgLib2ImageFactory(), Float32.zero());

        assertTrue(reader instanceof Float32ImgLib2TiffImageReader);
    }

    @Test
    public void getReader_RGB16ImgLib2Implementation_ReturnsCorrectWriter() throws NoReaderFoundForImage {
        ImageReader<RGB16> reader = TiffImageReaderFactory.getReader(new ImgLib2ImageFactory(), RGB16.zero());

        assertTrue(reader instanceof RGB16ImgLib2TiffImageReader);
    }
    
}