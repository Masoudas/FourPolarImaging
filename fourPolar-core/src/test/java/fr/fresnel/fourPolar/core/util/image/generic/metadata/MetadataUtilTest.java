package fr.fresnel.fourPolar.core.util.image.generic.metadata;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;

public class MetadataUtilTest {

    @Test
    public void numPlanesPerDimension_1DImage_throwsIllegalArgumentException() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2 }).build();
        assertThrows(IllegalArgumentException.class, () -> MetadataUtil.numPlanesPerDimension(metadata));
    }

    @Test
    public void numPlanesPerDimension_22Image_returns00() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2 }).build();

        long[] nPlanes = MetadataUtil.numPlanesPerDimension(metadata);
        assertArrayEquals(new long[] { 0, 0 }, nPlanes);
    }

    @Test
    public void numPlanesPerDimension_223Image_returns003() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2, 3 }).build();

        long[] nPlanes = MetadataUtil.numPlanesPerDimension(metadata);
        assertArrayEquals(new long[] { 0, 0, 3 }, nPlanes);
    }

    @Test
    public void numPlanesPerDimension_2234Image_returns00312() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2, 3, 4 }).build();

        long[] nPlanes = MetadataUtil.numPlanesPerDimension(metadata);
        assertArrayEquals(new long[] { 0, 0, 3, 12 }, nPlanes);
    }

    @Test
    public void numPlanesPerDimension_22314Image_returns003312() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2, 3, 1, 4 }).build();

        long[] nPlanes = MetadataUtil.numPlanesPerDimension(metadata);
        assertArrayEquals(new long[] { 0, 0, 3, 3, 12 }, nPlanes);
    }

    @Test
    public void getPlaneCoordinates_1DImage__throwsIllegalArgumentException() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2 }).build();
        assertThrows(IllegalArgumentException.class, () -> MetadataUtil.getPlaneCoordinates(metadata, 1));

    }

    @Test
    public void getPlaneCoordinates_22Image_returns00to11FirstPlane() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2 }).build();

        long[][] coords = MetadataUtil.getPlaneCoordinates(metadata, 1);

        assertArrayEquals(coords[0], new long[] { 0, 0 });
        assertArrayEquals(coords[1], new long[] { 1, 1 });
    }

    @Test
    public void getPlaneCoordinates_223Image_returns000to110FirstPlane002to112ThirdPlane() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2, 3 }).build();

        long[][] coords_firstPlane = MetadataUtil.getPlaneCoordinates(metadata, 1);
        assertArrayEquals(coords_firstPlane[0], new long[] { 0, 0, 0 });
        assertArrayEquals(coords_firstPlane[1], new long[] { 1, 1, 0 });

        long[][] coords_thirdPlane = MetadataUtil.getPlaneCoordinates(metadata, 3);
        assertArrayEquals(coords_thirdPlane[0], new long[] { 0, 0, 2 });
        assertArrayEquals(coords_thirdPlane[1], new long[] { 1, 1, 2 });

    }

    @Test
    public void getPlaneCoordinates_2234Image_returns0000to1100FirstPlane() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2, 3, 4 }).build();
       
        long[][] coords_firstPlane = MetadataUtil.getPlaneCoordinates(metadata, 1);
        assertArrayEquals(coords_firstPlane[0], new long[] { 0, 0, 0, 0 });
        assertArrayEquals(coords_firstPlane[1], new long[] { 1, 1, 0, 0 });

    }

    @Test
    public void getPlaneCoordinates_2234Image_returns0011to1111FifthPlane() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2, 3, 4 }).build();
       
        long[][] coords_firstPlane = MetadataUtil.getPlaneCoordinates(metadata, 5);
        assertArrayEquals(coords_firstPlane[0], new long[] { 0, 0, 1, 1 });
        assertArrayEquals(coords_firstPlane[1], new long[] { 1, 1, 1, 1 });

    }

    @Test
    public void getPlaneCoordinates_2234Image_returns0023to1123TwelfthPlane() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2, 3, 4 }).build();

        long[][] coords_thirdPlane = MetadataUtil.getPlaneCoordinates(metadata, 12);
        assertArrayEquals(coords_thirdPlane[0], new long[] { 0, 0, 2, 3 });
        assertArrayEquals(coords_thirdPlane[1], new long[] { 1, 1, 2, 3 });

    }

    @Test
    public void getPlaneCoordinates_2214Image_returns0002to1102ThirdPlane() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2, 1, 4 }).build();
       
        long[][] coords_firstPlane = MetadataUtil.getPlaneCoordinates(metadata, 3);
        assertArrayEquals(coords_firstPlane[0], new long[] { 0, 0, 0, 2 });
        assertArrayEquals(coords_firstPlane[1], new long[] { 1, 1, 0, 2 });

    }

}