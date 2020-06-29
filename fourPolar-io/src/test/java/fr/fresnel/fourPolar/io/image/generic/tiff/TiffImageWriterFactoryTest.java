package fr.fresnel.fourPolar.io.image.generic.tiff;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.ARGB8;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.io.image.generic.ImageWriter;
import fr.fresnel.fourPolar.io.image.generic.tiff.ImageJ1.ImageJ1RGB16TiffReader;
import fr.fresnel.fourPolar.io.image.generic.tiff.scifio.SCIFIOFloat32TiffWriter;
import fr.fresnel.fourPolar.io.image.generic.tiff.scifio.SCIFIOUINT16TiffWriter;

public class TiffImageWriterFactoryTest {
    final private IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 1 }).build();

    @Test
    public void getWriter_UINT16ImgLib2Implementation_ReturnsCorrectWriter() {
        Image<UINT16> image = new ImgLib2ImageFactory().create(metadata, UINT16.zero());
        ImageWriter<UINT16> writer = TiffImageWriterFactory.getWriter(image, UINT16.zero());

        assertTrue(writer instanceof SCIFIOUINT16TiffWriter);
    }

    @Test
    public void getWriter_Float32ImgLib2Implementation_ReturnsCorrectWriter() {
        Image<Float32> image = new ImgLib2ImageFactory().create(metadata, Float32.zero());
        ImageWriter<Float32> writer = TiffImageWriterFactory.getWriter(image, Float32.zero());

        assertTrue(writer instanceof SCIFIOFloat32TiffWriter);
    }

    @Test
    public void getWriter_RGB16ImgLib2Implementation_ReturnsCorrectWriter() {
        Image<ARGB8> image = new ImgLib2ImageFactory().create(metadata, ARGB8.zero());
        ImageWriter<ARGB8> writer = TiffImageWriterFactory.getWriter(image, ARGB8.zero());

        assertTrue(writer instanceof ImageJ1RGB16TiffReader);
    }

}