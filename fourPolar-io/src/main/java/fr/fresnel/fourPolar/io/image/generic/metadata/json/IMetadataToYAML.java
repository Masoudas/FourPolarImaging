package fr.fresnel.fourPolar.io.image.generic.metadata.json;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator.Feature;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.io.exceptions.image.generic.metadata.MetadataIOIssues;
import fr.fresnel.fourPolar.io.image.generic.metadata.IMetadataWriter;

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
        Objects.requireNonNull(metadata, "metadata can't be null");
        Objects.requireNonNull(root, "root can't be null");
        Objects.requireNonNull(name, "name can't be null");

        _jsonAdaptor.toJSON(metadata);
        try {
            _mapper.writeValue(_createPathFile(root, name), _jsonAdaptor);
        } catch (IOException e) {
            throw new MetadataIOIssues(MetadataIOIssues.READ_ISSUES);
        }

    }

    @Override
    public void close() throws MetadataIOIssues {
    }

    private File _createPathFile(File root, String name) {
        return new File(root, name + ".yaml");
    }
    
}