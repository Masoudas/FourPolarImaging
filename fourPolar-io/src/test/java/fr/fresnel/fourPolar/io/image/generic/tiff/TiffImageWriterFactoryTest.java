package fr.fresnel.fourPolar.io.image.generic.tiff;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.io.exceptions.image.generic.NoWriterFoundForImage;
import fr.fresnel.fourPolar.io.image.generic.ImageWriter;
import fr.fresnel.fourPolar.io.image.generic.tiff.scifio.Float32SCIFIOTiffImageWriter;
import fr.fresnel.fourPolar.io.image.generic.tiff.scifio.RGB16SCIFIOTiffImageWriter;
import fr.fresnel.fourPolar.io.image.generic.tiff.scifio.UINT16SCIFIOTiffImageWriter;

public class TiffImageWriterFactoryTest {
    final private long[] _dim = { 1, 1 };

    @Test
    public void getWriter_UINT16ImgLib2Implementation_ReturnsCorrectWriter()
            throws NoWriterFoundForImage {
        Image<UINT16> image = new ImgLib2ImageFactory().create(_dim, UINT16.zero());
        ImageWriter<UINT16> reader = TiffImageWriterFactory.getWriter(image, UINT16.zero());

        assertTrue(reader instanceof UINT16SCIFIOTiffImageWriter);
    }
    
    @Test
    public void getWriter_Float32ImgLib2Implementation_ReturnsCorrectWriter()
            throws NoWriterFoundForImage {
        Image<Float32> image = new ImgLib2ImageFactory().create(_dim, Float32.zero());
        ImageWriter<Float32> reader = TiffImageWriterFactory.getWriter(image, Float32.zero());

        assertTrue(reader instanceof Float32SCIFIOTiffImageWriter);
    }

    @Test
    public void getWriter_RGB16ImgLib2Implementation_ReturnsCorrectWriter()
            throws NoWriterFoundForImage {
        Image<RGB16> image = new ImgLib2ImageFactory().create(_dim, RGB16.zero());
        ImageWriter<RGB16> reader = TiffImageWriterFactory.getWriter(image, RGB16.zero());

        assertTrue(reader instanceof RGB16SCIFIOTiffImageWriter);
    }

    
}