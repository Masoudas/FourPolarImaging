package fr.fresnel.fourPolar.core.image.vector.batikModel;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;
import fr.fresnel.fourPolar.core.image.vector.VectorImage;
import fr.fresnel.fourPolar.core.image.vector.VectorImageFactory;

public class BatikVectorImageTest {
    @Test
    public void getImagePlane_1DImage_ImageHasOnlyOnePlane() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2 }).axisOrder(AxisOrder.NoOrder).build();
        BatikVectorImage vectorImage = new BatikVectorImage(metadata, new DummyBatikFactory());

        assertTrue(vectorImage.numPlanes() == 1);

    }

    @Test
    public void getImagePlane_XYImage_ImageHasOnlyOnePlane() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2 }).axisOrder(AxisOrder.XY).build();
        BatikVectorImage vectorImage = new BatikVectorImage(metadata, new DummyBatikFactory());

        assertTrue(vectorImage.numPlanes() == 1);

    }

    @Test
    public void getImagePlane_XYZImage223_ImageHasThreePlanes() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2, 3 }).axisOrder(AxisOrder.XYZ).build();
        BatikVectorImage vectorImage = new BatikVectorImage(metadata, new DummyBatikFactory());

        assertTrue(vectorImage.numPlanes() == 3);
    }

    @Test
    public void getImagePlane_XYZTImage2213_ImageHasThreePlanes() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2, 1, 3 }).axisOrder(AxisOrder.XYZT).build();
        BatikVectorImage vectorImage = new BatikVectorImage(metadata, new DummyBatikFactory());

        assertTrue(vectorImage.numPlanes() == 3);
    }

}

class DummyBatikFactory implements VectorImageFactory {

    @Override
    public VectorImage create(IMetadata metadata) {
        return null;
    }

    @Override
    public <T extends PixelType> VectorImage create(Image<T> image, T pixelType) {
        return null;
    }

}