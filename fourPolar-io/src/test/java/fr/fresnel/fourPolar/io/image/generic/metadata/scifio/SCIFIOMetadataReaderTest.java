package fr.fresnel.fourPolar.io.image.generic.metadata.scifio;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.io.exceptions.image.generic.metadata.MetadataIOIssues;
import fr.fresnel.fourPolar.io.image.generic.metadata.IMetadataReader;

public class SCIFIOMetadataReaderTest {
    @Test
    public void reader_XYZT16BitImageJ1Image_ReturnsCorrectMetadata() throws IOException, MetadataIOIssues {
        File image = new File(SCIFIOMetadataReaderTest.class.getResource("").getPath(), "XYZT.tif");

        IMetadataReader reader = new SCIFIOMetadataReader();
        IMetadata metadata = reader.read(image);

        assertTrue(
                metadata.axisOrder() == AxisOrder.XYZT && metadata.bitPerPixel() == 8 && metadata.numChannels() == 0);
    }

    @Test
    public void reader_UnknownAxis_ReturnsNoOrderAxis() throws IOException, MetadataIOIssues {
        File image = new File(SCIFIOMetadataReaderTest.class.getResource("").getPath(), "UnknownAxis.tif");

        IMetadataReader reader = new SCIFIOMetadataReader();
        IMetadata metadata = reader.read(image);

        assertTrue(metadata.axisOrder() == AxisOrder.NoOrder);

    }

    @Test
    public void reader_XYZT16BitImageJ1ImageRepeatedUse_ReturnsCorrectMetadata()
            throws IOException, MetadataIOIssues {
        File image = new File(SCIFIOMetadataReaderTest.class.getResource("").getPath(), "XYZT.tif");

        IMetadataReader reader = new SCIFIOMetadataReader();

        reader.read(image);
        IMetadata metadata = reader.read(image);

        assertTrue(
                metadata.axisOrder() == AxisOrder.XYZT && metadata.bitPerPixel() == 8 && metadata.numChannels() == 0);
    }

}