package fr.fresnel.fourPolar.io.imagingSetup;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator.Feature;

import fr.fresnel.fourPolar.core.imagingSetup.IFourPolarImagingSetup;
import fr.fresnel.fourPolar.core.PathFactoryOfProject;

/**
 * This class is used for writing the fourPolar imaging setup to disk.
 */
public class FourPolarImagingSetupToYaml {
    private IFourPolarImagingSetup _imagingSetup;
    private File _root4PProject;

    public FourPolarImagingSetupToYaml(IFourPolarImagingSetup imagingSetup, File root4PProject) {
        this._imagingSetup = imagingSetup;
        this._root4PProject = root4PProject;
    }

    public void write() throws IOException {
        File destFile = new File(getDestinationFolder(this._root4PProject), getFileName());

        FourPolarImagingSetupJSONAdaptor adaptor = new FourPolarImagingSetupJSONAdaptor();
        adaptor.toYaml(this._imagingSetup);

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory().disable(Feature.WRITE_DOC_START_MARKER));
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        mapper.writeValue(destFile, adaptor);
    }

    public static File getDestinationFolder(File root4PProject) {
        return PathFactoryOfProject.getFolder_Params(root4PProject);
    }

    public static String getFileName() {
        return "ImagingSetup.yaml";
    }
}