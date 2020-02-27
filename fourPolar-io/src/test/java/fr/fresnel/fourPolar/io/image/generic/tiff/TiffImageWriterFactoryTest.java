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
import fr.fresnel.fourPolar.io.image.generic.tiff.scifio.Float32ImgLib2TiffImageWriter;
import fr.fresnel.fourPolar.io.image.generic.tiff.scifio.RGB16ImgLib2TiffImageWriter;
import fr.fresnel.fourPolar.io.image.generic.tiff.scifio.UINT16ImgLib2TiffImageWriter;

public class TiffImageWriterFactoryTest {
    final private long[] _dim = { 1, 1 };

    @Test
    public void getWriter_UINT16ImgLib2Implementation_ReturnsCorrectWriter()
            throws NoWriterFoundForImage {
        Image<UINT16> image = new ImgLib2ImageFactory().create(_dim, new UINT16());
        ImageWriter<UINT16> reader = TiffImageWriterFactory.getWriter(image, new UINT16());

        assertTrue(reader instanceof UINT16ImgLib2TiffImageWriter);
    }
    
    @Test
    public void getWriter_Float32ImgLib2Implementation_ReturnsCorrectWriter()
            throws NoWriterFoundForImage {
        Image<Float32> image = new ImgLib2ImageFactory().create(_dim, new Float32());
        ImageWriter<Float32> reader = TiffImageWriterFactory.getWriter(image, new Float32());

        assertTrue(reader instanceof Float32ImgLib2TiffImageWriter);
    }

    @Test
    public void getWriter_RGB16ImgLib2Implementation_ReturnsCorrectWriter()
            throws NoWriterFoundForImage {
        Image<RGB16> image = new ImgLib2ImageFactory().create(_dim, new RGB16());
        ImageWriter<RGB16> reader = TiffImageWriterFactory.getWriter(image, new RGB16());

        assertTrue(reader instanceof RGB16ImgLib2TiffImageWriter);
    }

    
}