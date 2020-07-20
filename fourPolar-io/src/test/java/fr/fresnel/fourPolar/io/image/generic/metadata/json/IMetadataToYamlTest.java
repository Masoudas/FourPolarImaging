package fr.fresnel.fourPolar.io.image.generic.metadata.json;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.io.exceptions.image.generic.metadata.MetadataIOIssues;

public class IMetadataToYamlTest {
    @Test
    public void write_IncompleteMetadataWithNoAxisOrder_ThrowsMetadataIOIssuesException() {
        DummyMetadata metadata = new DummyMetadata();
        metadata.setDim(new long[] { 1, 2 });

        assertThrows(MetadataIOIssues.class, () -> {
            new IMetadataToYAML().write(metadata, new File(""), "");
        });

    }

    @Test
    public void write_CompleteMetadata_WritesMetadata()
            throws MetadataIOIssues, IOException {
        DummyMetadata metadata = new DummyMetadata();
        metadata.setDim(new long[] { 1, 2 });
        metadata.setAxisOrder(AxisOrder.XY);

        File root = _createRoot();
        String name = "Complete_Metadata";

        new IMetadataToYAML().write(metadata, root, name);
        assertTrue(_isMetadataWrittenProperly(root, name, metadata));

    }

    @Test
    public void write_MultipleMetadataWithSameWriter_WritesAllMetadataInstancesInSeparateFiles()
            throws MetadataIOIssues, IOException {
        DummyMetadata metadata = new DummyMetadata();

        for (int index = 0; index < 2; index++) {
            metadata.setDim(new long[] { 1 + index, 2 + index });
            metadata.setAxisOrder(AxisOrder.XY);

            File root = _createRoot();
            String name = "Complete_Metadata" + String.valueOf(index);

            new IMetadataToYAML().write(metadata, root, name);
            assertTrue(_isMetadataWrittenProperly(root, name, metadata));

        }

    }

    private File _createRoot() {
        String testResource = IMetadataToYamlTest.class.getResource("").getPath();
        File root = new File(testResource, "IMetadataTOYAMLTest");
        root.mkdirs();
        return root;
    }

    private boolean _isMetadataWrittenProperly(File root, String name, IMetadata metadata) throws IOException {
        DummyIMetadataJSONAdaptor adaptor = new DummyIMetadataJSONAdaptor();
        adaptor = new ObjectMapper(new YAMLFactory()).readValue(new File(root.getAbsolutePath() + "/" + name + ".yaml"),
                adaptor.getClass());

        return (adaptor.axisOrder.equals(metadata.axisOrder().toString()))
                && adaptor.dimension.equals(Arrays.toString(metadata.getDim()));
    }

}

class DummyMetadata implements IMetadata {
    AxisOrder axisOrder;
    long[] dim;

    public void setAxisOrder(AxisOrder axisOrder) {
        this.axisOrder = axisOrder;
    }

    public void setDim(long[] dim) {
        this.dim = dim;
    }

    @Override
    public AxisOrder axisOrder() {
        return axisOrder;
    }

    @Override
    public int numChannels() {
        return 0;
    }

    @Override
    public int bitPerPixel() {
        return 0;
    }

    @Override
    public long[] getDim() {
        return this.dim;
    }

}

@JsonPropertyOrder({ "Axis-Order", "Dimension" })
class DummyIMetadataJSONAdaptor {
    @JsonProperty("Axis-Order")
    public String axisOrder = null;

    @JsonProperty("Dimension")
    public String dimension = null;

}