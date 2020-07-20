package fr.fresnel.fourPolar.io.image.generic.metadata.json;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.io.exceptions.image.generic.metadata.MetadataIOIssues;

public class IMetadataFromYamlTest {
    @Test
    public void read_IncorrectAxisOrder_ThrowsMetadataIOIssues() throws MetadataIOIssues {
        File root = _createRoot();
        String name = "Metadata_IncorrectAxisOrder.yaml";

        assertThrows(MetadataIOIssues.class, () -> {
            new IMetadataFromYAML().read(new File(root, name));
        });
    }

    @Test
    public void read_IncorrectDimensionVector_ThrowsMetadataIOIssues() throws MetadataIOIssues {
        File root = _createRoot();
        String name = "Metadata_IncorrectDimensionVector.yaml";

        assertThrows(MetadataIOIssues.class, () -> {
            new IMetadataFromYAML().read(new File(root, name));
        });
    }

    @Test
    public void read_UnequalAxisOrderAndDim_ThrowsMetadataIOIssues() throws MetadataIOIssues {
        File root = _createRoot();
        String name = "Metadata_UnequalAxisOrderAndDim.yaml";

        assertThrows(MetadataIOIssues.class, () -> {
            new IMetadataFromYAML().read(new File(root, name));
        });
    }

    @Test
    public void read_CompleteMetadata_ReturnsCorrectMetadata() throws MetadataIOIssues {
        File root = _createRoot();
        String name = "Complete_Metadata.yaml";

        IMetadata diskMetadata = new IMetadataFromYAML().read(new File(root, name));
        assertTrue(_isMetadataCorrect(diskMetadata, AxisOrder.XYZ, new long[] { 1, 2, 3 }));
    }

    @Test
    public void read_MultipleCompleteMetadata_ReturnsCorrectMetadata() throws MetadataIOIssues {
        File root = _createRoot();

        for (int i = 0; i < 2; i++) {
            String name = "Complete_Metadata" + i + ".yaml";

            IMetadata diskMetadata = new IMetadataFromYAML().read(new File(root, name));
            assertTrue(_isMetadataCorrect(diskMetadata, AxisOrder.XY, new long[] { 1 + i, 2 + i }));                
        }
    }

    private File _createRoot() {
        String testResource = IMetadataToYamlTest.class.getResource("").getPath();
        File root = new File(testResource, "IMetadataFromYamlTest");
        root.mkdirs();
        return root;
    }

    private boolean _isMetadataCorrect(IMetadata metadata, AxisOrder axisOrder, long[] dim) {
        return metadata.axisOrder() == axisOrder && Arrays.equals(metadata.getDim(), dim);

    }

}