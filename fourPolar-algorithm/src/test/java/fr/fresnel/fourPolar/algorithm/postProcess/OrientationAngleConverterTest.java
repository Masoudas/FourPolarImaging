package fr.fresnel.fourPolar.algorithm.postProcess;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.algorithm.util.image.orientation.OrientationAngleConverter;
import fr.fresnel.fourPolar.core.exceptions.image.orientation.CannotFormOrientationImage;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFile;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.IPixelRandomAccess;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.IPixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.orientation.IOrientationImage;
import fr.fresnel.fourPolar.core.image.orientation.OrientationImageFactory;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.physics.dipole.IOrientationVector;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;

public class OrientationAngleConverterTest {
    @Test
    public void convertToDegree_OrientationImageWithCriticalPointData_ReturnsCorrectDegrees()
            throws CannotFormOrientationImage {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 1, 1, 1, 1 }).axisOrder(AxisOrder.XYCZT)
                .build();

        Image<Float32> rho = new ImgLib2ImageFactory().create(metadata, Float32.zero());
        Image<Float32> delta = new ImgLib2ImageFactory().create(metadata, Float32.zero());
        Image<Float32> eta = new ImgLib2ImageFactory().create(metadata, Float32.zero());

        long[] pos0 = new long[]{0,0,0,0,0};
        long[] pos1 = new long[]{1,0,0,0,0};
        _setPixel(rho, pos0, 0);
        _setPixel(rho, pos1, (float)IOrientationVector.MAX_Rho);

        _setPixel(delta, pos0, 0);
        _setPixel(delta, pos1, (float)IOrientationVector.MAX_Delta);
        
        _setPixel(eta, pos0, 0);
        _setPixel(eta, pos1, (float)IOrientationVector.MAX_Eta);

        IOrientationImage orientationImage = OrientationImageFactory.create(new DummySet(), 1, rho, delta, eta);

        Image<Float32> degreeImage_rho = OrientationAngleConverter.convertToDegree(orientationImage, OrientationAngle.rho);
        assertTrue(_isPixelEqualToValue(degreeImage_rho, pos0, (float)0));
        assertTrue(_isPixelEqualToValue(degreeImage_rho, pos1, (float)Math.toDegrees(IOrientationVector.MAX_Rho)));

        Image<Float32> degreeImage_delta = OrientationAngleConverter.convertToDegree(orientationImage, OrientationAngle.delta);
        assertTrue(_isPixelEqualToValue(degreeImage_delta, pos0, (float)0));
        assertTrue(_isPixelEqualToValue(degreeImage_delta, pos1, (float)Math.toDegrees(IOrientationVector.MAX_Delta)));

        Image<Float32> degreeImage_eta = OrientationAngleConverter.convertToDegree(orientationImage, OrientationAngle.eta);
        assertTrue(_isPixelEqualToValue(degreeImage_eta, pos0, (float)0));
        assertTrue(_isPixelEqualToValue(degreeImage_eta, pos1, (float)Math.toDegrees(IOrientationVector.MAX_Eta)));
        
    }

    @Test
    public void convertToRadian_RhoImageWithCritialPointData_ReturnsCorrectDegrees() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 1, 1, 1, 1 }).axisOrder(AxisOrder.XYCZT)
                .build();

        Image<Float32> rho = new ImgLib2ImageFactory().create(metadata, Float32.zero());

        long[] pos0 = new long[]{0,0,0,0,0};
        long[] pos1 = new long[]{1,0,0,0,0};
        _setPixel(rho, pos0, 0);
        _setPixel(rho, pos1, (float)180);

        OrientationAngleConverter.convertToRadian(rho);

        assertTrue(_isPixelEqualToValue(rho, pos0, (float)0));
        assertTrue(_isPixelEqualToValue(rho, pos1, (float)IOrientationVector.MAX_Rho));

    }

    @Test
    public void convertToRadian_DeltaImageWithCritialPointData_ReturnsCorrectDegrees() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 1, 1, 1, 1 }).axisOrder(AxisOrder.XYCZT)
                .build();

        Image<Float32> rho = new ImgLib2ImageFactory().create(metadata, Float32.zero());

        long[] pos0 = new long[]{0,0,0,0,0};
        long[] pos1 = new long[]{1,0,0,0,0};
        _setPixel(rho, pos0, 0);
        _setPixel(rho, pos1, (float)180);

        OrientationAngleConverter.convertToRadian(rho);

        assertTrue(_isPixelEqualToValue(rho, pos0, (float)0));
        assertTrue(_isPixelEqualToValue(rho, pos1, (float)IOrientationVector.MAX_Delta));

    }

    @Test
    public void convertToRadian_EtaImageWithCritialPointData_ReturnsCorrectDegrees() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 1, 1, 1, 1 }).axisOrder(AxisOrder.XYCZT)
                .build();

        Image<Float32> rho = new ImgLib2ImageFactory().create(metadata, Float32.zero());

        long[] pos0 = new long[]{0,0,0,0,0};
        long[] pos1 = new long[]{1,0,0,0,0};
        _setPixel(rho, pos0, 0);
        _setPixel(rho, pos1, (float)90);

        OrientationAngleConverter.convertToRadian(rho);

        assertTrue(_isPixelEqualToValue(rho, pos0, (float)0));
        assertTrue(_isPixelEqualToValue(rho, pos1, (float)IOrientationVector.MAX_Eta));

    }

    private void _setPixel(Image<Float32> image, long[] position, float value) {
        IPixelRandomAccess<Float32> ra = image.getRandomAccess();

        ra.setPosition(position);
        IPixel<Float32> pixel = ra.getPixel();

        pixel.value().set(value);
        ra.setPixel(pixel);        
    }

    private boolean _isPixelEqualToValue(Image<Float32> image, long[] position, float value) {
        IPixelRandomAccess<Float32> ra = image.getRandomAccess();
        ra.setPosition(position);

        // Check the value is close enough
        return Math.abs(ra.getPixel().value().get() - value) < 1e-10;
    }


}

class DummySet implements ICapturedImageFileSet {

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