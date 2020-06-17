package fr.fresnel.fourPolar.core.image.polarization;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

public class PolarizationImageTest {
    @Test
    public void init_NotXYCZTImage_ThrowsIllegalArgumentException() {
        long[] dim1 = { 1, 1, 1, 1 };

        IMetadata metadata1 = new Metadata.MetadataBuilder(dim1).axisOrder(AxisOrder.XYZT).build();

        Image<UINT16> pol0 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());

        assertThrows(IllegalArgumentException.class, () -> {
            new PolarizationImage(Polarization.pol0, pol0);
        });
    }

    @Test
    public void init_MultiChannelImage_ThrowsIllegalArgumentException() {
        long[] dim1 = { 1, 1, 2, 1, 1 };

        IMetadata metadata1 = new Metadata.MetadataBuilder(dim1).axisOrder(AxisOrder.XYCZT).build();

        Image<UINT16> pol0 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());

        assertThrows(IllegalArgumentException.class, () -> {
            new PolarizationImage(Polarization.pol0, pol0);
        });
    }

}