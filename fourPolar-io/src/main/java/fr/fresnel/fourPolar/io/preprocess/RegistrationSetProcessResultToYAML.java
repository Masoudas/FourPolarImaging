package fr.fresnel.fourPolar.io.preprocess;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator.Feature;

import fr.fresnel.fourPolar.core.imagingSetup.IFourPolarImagingSetup;
import fr.fresnel.fourPolar.core.preprocess.RegistrationSetProcessResult;

/**
 * Writes a {@link RegistrationSetProcessResult} to the a yaml file. The yaml
 * file is specified by {@link RegistrationSetProcessResultToYAMLFile}
 */
public class RegistrationSetProcessResultToYAML {
    private final RegistrationSetProcessResultToJSONAdaptor _jsonAdaptor;

    public RegistrationSetProcessResultToYAML(IFourPolarImagingSetup imagingSetup,
            RegistrationSetProcessResult result) {
        _jsonAdaptor = this._createJSONAdaptor(result, imagingSetup);

    }

    /**
     * Write this RegistrationSetProcessResult to disk as yaml.
     * 
     * @throws IOException in case of low-level IO issues.
     */
    public void write(File root4PProject) throws IOException {
        ObjectMapper yamlWriter = this._createYAMLWriter();
        yamlWriter.writeValue(this._getPathToYaml(root4PProject), this._jsonAdaptor);

    }

    private RegistrationSetProcessResultToJSONAdaptor _createJSONAdaptor(RegistrationSetProcessResult result,
            IFourPolarImagingSetup imagingSetup) {
        return new RegistrationSetProcessResultToJSONAdaptor(imagingSetup, result);
    }

    private File _getPathToYaml(File root4PProject) {
        return RegistrationSetProcessResultToYAMLFile.getPathToYaml(root4PProject);
    }

    private ObjectMapper _createYAMLWriter() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory().disable(Feature.WRITE_DOC_START_MARKER));
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        return mapper;
    }
}