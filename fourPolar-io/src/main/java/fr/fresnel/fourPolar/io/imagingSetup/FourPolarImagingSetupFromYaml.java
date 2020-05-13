package fr.fresnel.fourPolar.io.imagingSetup;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import fr.fresnel.fourPolar.core.imagingSetup.IFourPolarImagingSetup;

/**
 * This class is used for reading the fourPolar imaging setup from disk.
 */
public class FourPolarImagingSetupFromYaml {
    private File _root4PProject;

    public FourPolarImagingSetupFromYaml(File root4PProject) {
        this._root4PProject = root4PProject;
    }

    public void read(IFourPolarImagingSetup imagingSetup) throws IOException{
        File destFile = new File(FourPolarImagingSetupToYaml.getDestinationFolder(this._root4PProject),
            FourPolarImagingSetupToYaml.getFileName());

        FourPolarImagingSetupJSONAdaptor adaptor = new FourPolarImagingSetupJSONAdaptor();
        
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        adaptor = mapper.readValue(destFile, adaptor.getClass());

        adaptor.fromYaml(imagingSetup);
    }

}