package fr.fresnel.fourPolar.core.image;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelTypes;

/**
 * Dedicated to testing the non-abstract methods of the class. Note that all
 * methods of this are independent of {@link PixelType} and AWT buffered image
 * types.
 */
public class PlanarImageModelTest {
    @Test
    public void init_PlanesDontHaveSameDimensionAsMetadata_ThrowsIllegalArgumentException() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 1, 1 }).axisOrder(AxisOrder.XY)
                .bitPerPixel(PixelTypes.UINT_16).build();

        DummyPlaneModel dummyPlaneModel = new DummyPlaneModel(new long[] { 2, 2 });

        assertThrows(IllegalArgumentException.class, () -> {
            new DummyPlanarWithPlanesConstructor(metadata, new DummyPlaneModel[] { dummyPlaneModel });
        });
    }

    @Test
    public void init_NumPlanesNotEqualToMetadataNumPlanes_ThrowsIllegalArgumentException() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 1, 1, 2 }).axisOrder(AxisOrder.XYZ)
                .bitPerPixel(PixelTypes.UINT_16).build();

        DummyPlaneModel dummyPlaneModel = new DummyPlaneModel(new long[] { 1, 1 });

        assertThrows(IllegalArgumentException.class, () -> {
            new DummyPlanarWithPlanesConstructor(metadata, new DummyPlaneModel[] { dummyPlaneModel });
        });
    }

    @Test
    public void init_TwoPlaneImage_CreatesCorrectPlanarImage() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 1, 1, 2 }).axisOrder(AxisOrder.XYZ)
                .bitPerPixel(PixelTypes.UINT_16).build();

        DummyPlaneModel plane_1 = new DummyPlaneModel(new long[] { 1, 1 });
        DummyPlaneModel plane_2 = new DummyPlaneModel(new long[] { 1, 1 });

        PlanarImageModel<DummyPlaneModel> pModel = new DummyPlanarWithPlanesConstructor(metadata,
                new DummyPlaneModel[] { plane_1, plane_2 });

        for (int planeIndex = 1; planeIndex <= 2; planeIndex++) {
            assertTrue(pModel.getImagePlane(planeIndex).planeIndex() == planeIndex);
        }

    }

    @Test
    public void init_1DMetadata_ThrowsIllegalArgumentException() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 1 }).axisOrder(AxisOrder.NoOrder)
                .bitPerPixel(PixelTypes.UINT_16).build();

        assertThrows(IllegalArgumentException.class, () -> new DummyPlanarImage(metadata));
    }

    @Test
    public void getPlaneIndex_11XYImage_Return1ForPosition00() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 1, 1 }).axisOrder(AxisOrder.XY)
                .bitPerPixel(PixelTypes.UINT_16).build();

        DummyPlanarImage image = new DummyPlanarImage(metadata);

        int planeNo = image.getPlaneIndex(new long[] { 0, 0 });

        assertTrue(planeNo == 1);
    }

    @Test
    public void getPlaneIndex_113XYZImage_Return3ForPosition002() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 1, 1, 3 }).axisOrder(AxisOrder.XYZ)
                .bitPerPixel(PixelTypes.UINT_16).build();

        DummyPlanarImage image = new DummyPlanarImage(metadata);

        int planeNo = image.getPlaneIndex(new long[] { 0, 0, 2 });

        assertTrue(planeNo == 3);
    }

    @Test
    public void getPlaneIndex_2233XYZTImage_Return3ForPosition0020() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2, 3, 3 }).axisOrder(AxisOrder.XYZT)
                .bitPerPixel(PixelTypes.UINT_16).build();

        DummyPlanarImage image = new DummyPlanarImage(metadata);

        int planeNo = image.getPlaneIndex(new long[] { 0, 0, 2, 0 });

        assertTrue(planeNo == 3);
    }

    @Test
    public void getPlaneIndex_2233XYZTImage_Return0ForPosition0022() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2, 3, 3 }).axisOrder(AxisOrder.XYZT)
                .bitPerPixel(PixelTypes.UINT_16).build();

        DummyPlanarImage image = new DummyPlanarImage(metadata);

        int planeNo = image.getPlaneIndex(new long[] { 0, 0, 2, 2 });

        assertTrue(planeNo == 9);
    }

    @Test
    public void getPlaneIndex_11113XYZCTImage_Return2ForPosition00001() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 1, 1, 1, 1, 3 }).axisOrder(AxisOrder.XYCZT)
                .bitPerPixel(PixelTypes.UINT_16).build();

        DummyPlanarImage image = new DummyPlanarImage(metadata);

        int planeNo = image.getPlaneIndex(new long[] { 0, 0, 0, 0, 2 });

        assertTrue(planeNo == 3);
    }

    @Test
    public void getImagePlane_11XYImage_ReturnSinglePlane1() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 1, 1 }).axisOrder(AxisOrder.XY)
                .bitPerPixel(PixelTypes.UINT_16).build();

        DummyPlanarImage image = new DummyPlanarImage(metadata);

        assertTrue(image.getImagePlane(1).planeIndex() == 1);
        assertThrows(IllegalArgumentException.class, () -> image.getImagePlane(2));

    }

    @Test
    public void getImagePlane_113XYZImage_ReturnAllThreePlanes() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 1, 1, 3 }).axisOrder(AxisOrder.XYZ)
                .bitPerPixel(PixelTypes.UINT_16).build();

        DummyPlanarImage image = new DummyPlanarImage(metadata);

        for (int planeIndex = 1; planeIndex <= 3; planeIndex++) {
            assertTrue(image.getImagePlane(planeIndex).planeIndex() == planeIndex);
        }
    }

    @Test
    public void getImagePlane_113XYZImage_ThrowsIllegalArgumentExceptionForPlane10() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 1, 1, 3 }).axisOrder(AxisOrder.XYZ)
                .bitPerPixel(PixelTypes.UINT_16).build();

        DummyPlanarImage image = new DummyPlanarImage(metadata);

        assertThrows(IllegalArgumentException.class, () -> image.getImagePlane(10).planeIndex());

    }

    @Test
    public void getImagePlane_1133XYZTImage_ReturnAllNinePlanes() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 1, 1, 3, 3 }).axisOrder(AxisOrder.XYZT)
                .bitPerPixel(PixelTypes.UINT_16).build();

        DummyPlanarImage image = new DummyPlanarImage(metadata);

        for (int planeIndex = 1; planeIndex <= 9; planeIndex++) {
            assertTrue(image.getImagePlane(planeIndex).planeIndex() == planeIndex);
        }

    }

}

class DummyPlaneModel {
    private long[] dim;

    public DummyPlaneModel(long[] dim) {
        this.dim = dim;
    }

    public long[] getDim() {
        return dim;
    }
}

class DummyPlaneSupplier implements ImagePlaneSupplier<DummyPlaneModel> {
    @Override
    public DummyPlaneModel get() {
        return new DummyPlaneModel(new long[] { 1, 1 });
    }

}

class DummyPlanarImage extends PlanarImageModel<DummyPlaneModel> {
    public DummyPlanarImage(IMetadata metadata) {
        super(metadata, new DummyPlaneSupplier());
    }

    @Override
    protected void _checkPlanesHaveSameDimensionAsMetadata(IMetadata metadata, DummyPlaneModel[] planes)
            throws IllegalArgumentException {
    }
}

class DummyPlanarWithPlanesConstructor extends PlanarImageModel<DummyPlaneModel> {

    protected DummyPlanarWithPlanesConstructor(IMetadata metadata, DummyPlaneModel[] planes) {
        super(metadata, planes);
    }

    @Override
    protected void _checkPlanesHaveSameDimensionAsMetadata(IMetadata metadata, DummyPlaneModel[] planes)
            throws IllegalArgumentException {
        for (DummyPlaneModel dummyPlaneModel : planes) {
            long[] planeSize = metadata.getDim();
            if (!Arrays.equals(new long[] { planeSize[0], planeSize[1] }, dummyPlaneModel.getDim())) {
                throw new IllegalArgumentException();
            }
        }
    }

}