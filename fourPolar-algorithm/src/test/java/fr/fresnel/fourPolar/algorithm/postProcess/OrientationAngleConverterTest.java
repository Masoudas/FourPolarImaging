package fr.fresnel.fourPolar.algorithm.postProcess;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.EnumSet;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.algorithm.postprocess.orientation.OrientationAngleConverter;
import fr.fresnel.fourPolar.core.exceptions.image.orientation.CannotFormOrientationImage;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFile;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.IPixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.orientation.IOrientationImage;
import fr.fresnel.fourPolar.core.image.orientation.OrientationImageFactory;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;

public class OrientationAngleConverterTest {
    @Test
    public void convertToDegree_OrientationImageWithTwoData_ReturnsCorrectDegrees() throws CannotFormOrientationImage {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 1, 1, 1, 1 }).axisOrder(AxisOrder.XYCZT)
                .build();

        Image<Float32> rho = new ImgLib2ImageFactory().create(metadata, Float32.zero());
        Image<Float32> delta = new ImgLib2ImageFactory().create(metadata, Float32.zero());
        Image<Float32> eta = new ImgLib2ImageFactory().create(metadata, Float32.zero());

        _setRadianAngles(rho.getCursor());
        _setRadianAngles(delta.getCursor());
        _setRadianAngles(eta.getCursor());

        IOrientationImage orientationImage = OrientationImageFactory.create(new DummySet(), 1, rho, delta, eta);

        OrientationAngleConverter.convertToDegree(orientationImage);

        for (OrientationAngle orientationAngle : EnumSet.allOf(OrientationAngle.class)) {
            IPixelCursor<Float32> angleCursor = orientationImage.getAngleImage(orientationAngle).getImage().getCursor();

            assertTrue(Math.abs(angleCursor.next().value().get() - 45) < 0.01);
            assertTrue(Math.abs(angleCursor.next().value().get() - 90) < 0.01);
        }
    }

    @Test
    public void convertToRadian_OrientationImageWithTwoData_ReturnsCorrectDegrees() throws CannotFormOrientationImage {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 1, 1, 1, 1 }).axisOrder(AxisOrder.XYCZT)
                .build();

        Image<Float32> rho = new ImgLib2ImageFactory().create(metadata, Float32.zero());
        Image<Float32> delta = new ImgLib2ImageFactory().create(metadata, Float32.zero());
        Image<Float32> eta = new ImgLib2ImageFactory().create(metadata, Float32.zero());

        _setDegreeAngles(rho.getCursor());
        _setDegreeAngles(delta.getCursor());
        _setDegreeAngles(eta.getCursor());

        IOrientationImage orientationImage = OrientationImageFactory.create(new DummySet(), 1, rho, delta, eta);

        OrientationAngleConverter.convertToRadian(orientationImage);

        for (OrientationAngle orientationAngle : EnumSet.allOf(OrientationAngle.class)) {
            IPixelCursor<Float32> angleCursor = orientationImage.getAngleImage(orientationAngle).getImage().getCursor();

            assertTrue(Math.abs(angleCursor.next().value().get() - (float) Math.PI / 4) < 0.01);
            assertTrue(Math.abs(angleCursor.next().value().get() - (float) Math.PI / 2) < 0.01);
        }
    }

    private void _setDegreeAngles(IPixelCursor<Float32> cursor) {
        IPixel<Float32> angle1 = cursor.next();
        angle1.value().set(45);
        cursor.setPixel(angle1);

        IPixel<Float32> angle2 = cursor.next();
        angle2.value().set(90);
        cursor.setPixel(angle2);
    }

    private void _setRadianAngles(IPixelCursor<Float32> cursor) {
        IPixel<Float32> angle1 = cursor.next();
        angle1.value().set(((float) Math.PI / 4));
        cursor.setPixel(angle1);

        IPixel<Float32> angle2 = cursor.next();
        angle2.value().set(((float) Math.PI / 2));
        cursor.setPixel(angle2);
    }

}

class DummySet implements ICapturedImageFileSet {

    @Override
    public ICapturedImageFile[] getFile(String label) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getSetName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Cameras getnCameras() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasLabel(String label) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean deepEquals(ICapturedImageFileSet fileset) {
        // TODO Auto-generated method stub
        return false;
    }

}