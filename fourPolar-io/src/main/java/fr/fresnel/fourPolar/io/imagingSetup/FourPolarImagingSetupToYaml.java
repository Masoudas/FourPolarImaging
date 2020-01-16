package fr.fresnel.fourPolar.io.imagingSetup;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator.Feature;

import fr.fresnel.fourPolar.core.imagingSetup.FourPolarImagingSetup;
import fr.fresnel.fourPolar.io.PathFactory;

/**
 * This class is used for writing the fourPolar imaging setup to disk.
 */
public class FourPolarImagingSetupToYaml {
    private FourPolarImagingSetup _imagingSetup;
    private File _rootFolder;

    public FourPolarImagingSetupToYaml(FourPolarImagingSetup imagingSetup, File rootFolder) {
        this._imagingSetup = imagingSetup;
        this._rootFolder = rootFolder;
    }

    public void write() throws JsonGenerationException, JsonMappingException, IOException {
        File destFile = new File(getDestinationFolder(this._rootFolder), this._getFileName());

        FourPolarImagingSetupJSONAdaptor adaptor = new FourPolarImagingSetupJSONAdaptor();
        adaptor.toYaml(this._imagingSetup);

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory().disable(Feature.WRITE_DOC_START_MARKER));
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        mapper.writeValue(destFile, adaptor);
    }

    public static File getDestinationFolder(File rootFolder) {
        return PathFactory.getFolder_0_Params(rootFolder);
    }

    private String _getFileName() {
        return "ImagingSetup.yaml";
    }
}