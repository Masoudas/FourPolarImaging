package fr.fresnel.fourPolar.core.image.orientation;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.image.polarization.CannotFormPolarizationImageSet;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFile;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.image.polarization.PolarizationImageSetBuilder;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;

public class OrientationImageFactoryTest {
    @Test
    public void create_FromPolarizationImage_HasCorrectDimensionForAllImages() throws CannotFormPolarizationImageSet {
        long[] dim = { 2, 2, 1, 2, 1 };
        IMetadata metadata = new Metadata.MetadataBuilder(dim).axisOrder(AxisOrder.XYCZT).build();

        ImageFactory factory = new ImgLib2ImageFactory();

        Image<UINT16> pol0 = new ImgLib2ImageFactory().create(metadata, UINT16.zero());
        Image<UINT16> pol45 = new ImgLib2ImageFactory().create(metadata, UINT16.zero());
        Image<UINT16> pol90 = new ImgLib2ImageFactory().create(metadata, UINT16.zero());
        Image<UINT16> pol135 = new ImgLib2ImageFactory().create(metadata, UINT16.zero());

        IPolarizationImageSet polImage = new PolarizationImageSetBuilder(1).fileSet(new DummyFileSet()).channel(1).pol0(pol0)
                .pol45(pol45).pol90(pol90).pol135(pol135).build();

        IOrientationImage orientationImage = OrientationImageFactory.create(factory, polImage);
        assertTrue(Arrays.equals(orientationImage.getAngleImage(OrientationAngle.rho).getImage().getMetadata().getDim(),
                dim)
                && Arrays.equals(
                        orientationImage.getAngleImage(OrientationAngle.delta).getImage().getMetadata().getDim(), dim)
                && Arrays.equals(orientationImage.getAngleImage(OrientationAngle.eta).getImage().getMetadata().getDim(),
                        dim));

    }

}

class DummyFileSet implements ICapturedImageFileSet {

    @Override
    public ICapturedImageFile[] getFile(String label) {
        return null;
    }

    @Override
    public String getSetName() {
        return null;
    }

    @Override
    public Cameras getnCameras() {
        return null;
    }

    @Override
    public boolean hasLabel(String label) {
        return false;
    }

    @Override
    public boolean deepEquals(ICapturedImageFileSet fileset) {
        return false;
    }

}