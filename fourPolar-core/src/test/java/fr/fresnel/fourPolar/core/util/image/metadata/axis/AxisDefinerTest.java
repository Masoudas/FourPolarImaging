package fr.fresnel.fourPolar.core.util.image.metadata.axis;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.ConverterToImgLib2NotFound;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImageToImgLib2Converter;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;

public class AxisDefinerTest {
    @Test
    public void defineAxis_ImageAlreadyHasAxis_ThrowsIllegalArgumentExeption() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 1, 1 }).axisOrder(AxisOrder.XY).build();

        Image<UINT16> image = new ImgLib2ImageFactory().create(metadata, UINT16.zero());

        assertThrows(IllegalArgumentException.class, () -> {
            AxisDefiner.defineAxis(image, AxisOrder.XY);
        });

    }

    @Test
    public void defineAxis_newAxisOrderDoesNotSameNumAxisAsImageDim_ThrowsIllegalArgumentExeption() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 1, 1 }).axisOrder(AxisOrder.NoOrder).build();

        Image<UINT16> image = new ImgLib2ImageFactory().create(metadata, UINT16.zero());

        assertThrows(IllegalArgumentException.class, () -> {
            AxisDefiner.defineAxis(image, AxisOrder.XYZ);
        });

    }

    @Test
    public void defineAxis_DefineXYZOrderFor3DImage_ReturnsSameImageWithNewAxisOrder()
            throws ConverterToImgLib2NotFound {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 1, 1 }).axisOrder(AxisOrder.NoOrder).build();

        Image<UINT16> image = new ImgLib2ImageFactory().create(metadata, UINT16.zero());
        Image<UINT16> imageWithNewAxisOrder = AxisDefiner.defineAxis(image, AxisOrder.XY);

        assertTrue(imageWithNewAxisOrder.getMetadata().axisOrder() == AxisOrder.XY);
        assertTrue(ImageToImgLib2Converter.getImg(image, UINT16.zero()) == ImageToImgLib2Converter
                .getImg(imageWithNewAxisOrder, UINT16.zero()));

    }
}