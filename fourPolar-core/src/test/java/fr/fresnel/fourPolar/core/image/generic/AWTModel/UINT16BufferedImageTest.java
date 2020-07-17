package fr.fresnel.fourPolar.core.image.generic.AWTModel;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.image.BufferedImage;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelTypes;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;

public class UINT16BufferedImageTest {
    @Test
    public void init_1DMetadata_ThrowsIllegalArgumentException() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 1 }).axisOrder(AxisOrder.NoOrder)
                .bitPerPixel(PixelTypes.UINT_16).build();

        assertThrows(IllegalArgumentException.class,
                () -> new UINT16BufferedImage(metadata, new U16BIDummyImageFactory(), UINT16.zero()));

    }

    @Test
    public void init_UINT16ImageType_Creates_TYPE_USHORT_GRAY_BufferedImageForEachPlane() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 1, 1 }).axisOrder(AxisOrder.XY)
                .bitPerPixel(PixelTypes.UINT_16).build();

        UINT16BufferedImage image = new UINT16BufferedImage(metadata, new U16BIDummyImageFactory(), UINT16.zero());
        assertTrue(image.getImagePlane(1).getPlane().getType() == BufferedImage.TYPE_USHORT_GRAY);
    }

}

class U16BIDummyImageFactory implements ImageFactory {

    @Override
    public <T extends PixelType> Image<T> create(IMetadata metadata, T pixelType) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Not defined");
    }

}