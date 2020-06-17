package fr.fresnel.fourPolar.core.image.orientation;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.image.orientation.CannotFormOrientationImage;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFile;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;

public class OrientationImageTest {
    @Test
    public void createClass_DifferentImageDimension_ThrowsCannotFormPolarizationImageSet() {
        long[] dim1 = { 1, 1, 1, 1, 1 };
        long[] dim2 = { 2, 1, 1, 1, 1 };

        IMetadata metadata1 = new Metadata.MetadataBuilder(dim1).axisOrder(AxisOrder.XYCZT).build();
        IMetadata metadata2 = new Metadata.MetadataBuilder(dim2).axisOrder(AxisOrder.XYCZT).build();

        Image<Float32> rho = new ImgLib2ImageFactory().create(metadata1, Float32.zero());
        Image<Float32> eta = new ImgLib2ImageFactory().create(metadata1, Float32.zero());
        Image<Float32> delta = new ImgLib2ImageFactory().create(metadata2, Float32.zero());

        assertThrows(CannotFormOrientationImage.class, () -> {
            new OrientationImage(new DummyPolFileSet(), 1, new AngleImage(OrientationAngle.rho, rho),
                    new AngleImage(OrientationAngle.delta, delta), new AngleImage(OrientationAngle.eta, eta));
        });
    }

    @Test
    public void createClass_DuplicateImage_ThrowsCannotFormPolarizationImageSet() {
        long[] dim = { 1, 1, 1, 1, 1 };
        IMetadata metadata = new Metadata.MetadataBuilder(dim).axisOrder(AxisOrder.XYCZT).build();

        Image<Float32> rho = new ImgLib2ImageFactory().create(metadata, Float32.zero());
        Image<Float32> delta = new ImgLib2ImageFactory().create(metadata, Float32.zero());
        Image<Float32> eta = new ImgLib2ImageFactory().create(metadata, Float32.zero());

        assertThrows(CannotFormOrientationImage.class, () -> {
            new OrientationImage(new DummyPolFileSet(), 1, new AngleImage(OrientationAngle.rho, rho),
                    new AngleImage(OrientationAngle.rho, rho), new AngleImage(OrientationAngle.delta, delta));
        });

        assertThrows(CannotFormOrientationImage.class, () -> {
            new OrientationImage(new DummyPolFileSet(), 1, new AngleImage(OrientationAngle.rho, rho),
                    new AngleImage(OrientationAngle.delta, delta), new AngleImage(OrientationAngle.eta, rho));
        });

        assertThrows(CannotFormOrientationImage.class, () -> {
            new OrientationImage(new DummyPolFileSet(), 1, new AngleImage(OrientationAngle.rho, rho),
                    new AngleImage(OrientationAngle.delta, delta), new AngleImage(OrientationAngle.eta, delta));
        });

        assertThrows(CannotFormOrientationImage.class, () -> {
            new OrientationImage(new DummyPolFileSet(), 1, new AngleImage(OrientationAngle.eta, rho),
                    new AngleImage(OrientationAngle.delta, delta), new AngleImage(OrientationAngle.eta, delta));
        });
    }

    @Test
    public void getPolarizationImage_ImgLib2PolarizationImage_ReturnsCorrectImage() throws CannotFormOrientationImage {
        long[] dim = { 2, 2, 1, 2, 2 };
        IMetadata metadata = new Metadata.MetadataBuilder(dim).axisOrder(AxisOrder.XYCZT).build();

        Image<Float32> rho = new ImgLib2ImageFactory().create(metadata, Float32.zero());
        Image<Float32> delta = new ImgLib2ImageFactory().create(metadata, Float32.zero());
        Image<Float32> eta = new ImgLib2ImageFactory().create(metadata, Float32.zero());

        OrientationImage orientationImage = new OrientationImage(new DummyPolFileSet(), 1,
                new AngleImage(OrientationAngle.rho, rho), new AngleImage(OrientationAngle.delta, delta),
                new AngleImage(OrientationAngle.eta, eta));

        assertTrue(orientationImage.getAngleImage(OrientationAngle.rho).getImage() == rho
                && orientationImage.getAngleImage(OrientationAngle.delta).getImage() == delta
                && orientationImage.getAngleImage(OrientationAngle.eta).getImage() == eta);
    }
}

class DummyPolFileSet implements ICapturedImageFileSet {

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

    @Override
    public Iterator<ICapturedImageFile> getIterator() {
        return null;
    }

    @Override
    public int[] getChannels() {
        return null;
    }

}