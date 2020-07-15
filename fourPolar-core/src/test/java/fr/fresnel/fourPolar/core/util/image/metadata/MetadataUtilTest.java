package fr.fresnel.fourPolar.core.util.image.metadata;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;

public class MetadataUtilTest {

    @Test
    public void numPlanesPerDimension_1DImage_returns0() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2 }).build();

        assertThrows(IllegalArgumentException.class,
                () -> MetadataUtil.numPlanesPerDimension(metadata));
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
    public void numPlanesPerDimension_2234Image_returns003() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2, 3, 4 }).build();

        long[] nPlanes = MetadataUtil.numPlanesPerDimension(metadata);
        assertArrayEquals(new long[] { 0, 0, 3, 12 }, nPlanes);
    }

}