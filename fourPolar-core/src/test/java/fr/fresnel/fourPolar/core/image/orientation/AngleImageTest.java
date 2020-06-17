package fr.fresnel.fourPolar.core.image.orientation;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;

public class AngleImageTest {
    @Test
    public void init_NotXYCZTImage_ThrowsCannotFormPolarizationImageSet() {
        long[] dim = { 1, 1, 1, 1, 1 };
        IMetadata metadata = new Metadata.MetadataBuilder(dim).axisOrder(AxisOrder.XYZCT).build();

        Image<Float32> rho = new ImgLib2ImageFactory().create(metadata, Float32.zero());

        assertThrows(IllegalArgumentException.class, () -> {
            new AngleImage(OrientationAngle.rho, rho);
        });
    }

    @Test
    public void createClass_NotOneChannelImage_ThrowsCannotFormPolarizationImageSet() {
        long[] dim = { 1, 1, 2, 1, 1 };
        IMetadata metadata = new Metadata.MetadataBuilder(dim).axisOrder(AxisOrder.XYZCT).build();

        Image<Float32> rho = new ImgLib2ImageFactory().create(metadata, Float32.zero());

        assertThrows(IllegalArgumentException.class, () -> {
            new AngleImage(OrientationAngle.rho, rho);
        });
    }

}