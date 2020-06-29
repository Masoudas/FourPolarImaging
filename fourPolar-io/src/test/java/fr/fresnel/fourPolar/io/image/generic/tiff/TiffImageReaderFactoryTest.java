package fr.fresnel.fourPolar.io.image.generic.tiff;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.ARGB8;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.io.image.generic.ImageReader;
import fr.fresnel.fourPolar.io.image.generic.tiff.scifio.SCIFIOFloat32TiffReader;
import fr.fresnel.fourPolar.io.image.generic.tiff.scifio.SCIFIORGB16TiffReader;
import fr.fresnel.fourPolar.io.image.generic.tiff.scifio.SCIFIOUINT16TiffReader;

public class TiffImageReaderFactoryTest {

    @Test
    public void getReader_UINT16ImgLib2Implementation_ReturnsCorrectWriter(){
        ImageReader<UINT16> reader = TiffImageReaderFactory.getReader(new ImgLib2ImageFactory(), UINT16.zero());

        assertTrue(reader instanceof SCIFIOUINT16TiffReader);
    }
    
    @Test
    public void getReader_Float32ImgLib2Implementation_ReturnsCorrectWriter() {
        ImageReader<Float32> reader = TiffImageReaderFactory.getReader(new ImgLib2ImageFactory(), Float32.zero());

        assertTrue(reader instanceof SCIFIOFloat32TiffReader);
    }

    @Test
    public void getReader_RGB16ImgLib2Implementation_ReturnsCorrectWriter() {
        ImageReader<ARGB8> reader = TiffImageReaderFactory.getReader(new ImgLib2ImageFactory(), ARGB8.zero());

        assertTrue(reader instanceof SCIFIORGB16TiffReader);
    }
    
}