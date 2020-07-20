package fr.fresnel.fourPolar.io.image.generic.metadata.json;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.io.exceptions.image.generic.metadata.MetadataIOIssues;
import fr.fresnel.fourPolar.io.image.generic.metadata.IMetadataReader;

/**
 * A class for reading metadata, as written by {@link IMetadataToYAML}
 */
public class IMetadataFromYAML implements IMetadataReader {
    private final ObjectMapper _mapper;

    public IMetadataFromYAML() {
        _mapper = new ObjectMapper(new YAMLFactory());
    }

    @Override
    public IMetadata read(File path) throws MetadataIOIssues {
        try {
            return _mapper.readValue(path, IMetadataJSONAdaptor.class).fromJSON();
        } catch (IOException e) {
            throw new MetadataIOIssues(MetadataIOIssues.Write_ISSUES);
        }
    }

    @Override
    public void close() throws MetadataIOIssues {
    }

}