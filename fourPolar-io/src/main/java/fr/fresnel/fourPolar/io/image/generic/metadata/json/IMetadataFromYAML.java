package fr.fresnel.fourPolar.io.image.generic.metadata.json;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.io.exceptions.image.generic.metadata.MetadataIOIssues;
import fr.fresnel.fourPolar.io.image.generic.metadata.IMetadataReader;

/**
 * A class for reading metadata, as written by {@link IMetadataToYAML}
 */
public class IMetadataFromYAML implements IMetadataReader {
    private final static String _FILE_EXTENSION = ".yaml";
    private final ObjectMapper _mapper;

    public IMetadataFromYAML() {
        _mapper = new ObjectMapper(new YAMLFactory());
    }

    @Override
    public IMetadata read(File path) throws MetadataIOIssues {
        Objects.requireNonNull(path, "path can't be null.");
        
        try {
            return _mapper.readValue(path, IMetadataJSONAdaptor.class).fromJSON();
        } catch (IOException e) {
            throw new MetadataIOIssues(MetadataIOIssues.READ_ISSUES);
        }
    }

    @Override
    public void close() throws MetadataIOIssues {
    }

    @Override
    public IMetadata read(File root, String fileName) throws MetadataIOIssues {
        return read(_createPathFromRootAndFileName(root, fileName));
    }

    private File _createPathFromRootAndFileName(File root, String fileName) {
        return new File(root, fileName + _FILE_EXTENSION);
    }

}