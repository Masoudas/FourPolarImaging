package fr.fresnel.fourPolar.io.image.generic.metadata.json;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator.Feature;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.metadata.IMetadataWriter;
import fr.fresnel.fourPolar.io.exceptions.image.generic.metadata.MetadataIOIssues;

/**
 * A class for writing {@link IMetadata} as a YAML file.
 */
public class IMetadataToYAML implements IMetadataWriter {
    private final IMetadataJSONAdaptor _jsonAdaptor = new IMetadataJSONAdaptor();
    private final ObjectMapper _mapper;

    public IMetadataToYAML() {
        _mapper = new ObjectMapper(new YAMLFactory().disable(Feature.WRITE_DOC_START_MARKER));
        _mapper.enable(SerializationFeature.INDENT_OUTPUT);

    }

    @Override
    public void write(IMetadata metadata, File root, String name) throws MetadataIOIssues {
        _jsonAdaptor.toJSON(metadata);
        try {
            _mapper.writeValue(_createPathFile(root, name), _jsonAdaptor);
        } catch (IOException e) {
            throw new MetadataIOIssues(MetadataIOIssues.READ_ISSUES);
        }

    }

    @Override
    public void close() throws IOException {
        throw new NoSuchMethodError("No need to call the method for this writer");
    }

    private File _createPathFile(File root, String name) {
        return new File(root, name + ".yaml");
    }
    
}