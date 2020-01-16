package fr.fresnel.fourPolar.io.imagingSetup;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import fr.fresnel.fourPolar.core.imagingSetup.FourPolarImagingSetup;

/**
 * This class is used for writing the fourPolar imaging setup to disk.
 */
public class FourPolarImagingSetupFromYaml {
    private File _rootFolder;

    public FourPolarImagingSetupFromYaml(File rootFolder) {
        this._rootFolder = rootFolder;
    }

    public FourPolarImagingSetup read() throws JsonParseException, JsonMappingException, IOException{
        File destFile = new File(FourPolarImagingSetupToYaml.getDestinationFolder(this._rootFolder),
            FourPolarImagingSetupToYaml.getFileName());

        FourPolarImagingSetupJSONAdaptor adaptor = new FourPolarImagingSetupJSONAdaptor();
        
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        adaptor = mapper.readValue(destFile, adaptor.getClass());

        return adaptor.fromYaml();
    }

}