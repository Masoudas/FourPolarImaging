package fr.fresnel.fourPolar.io.imagingSetup;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator.Feature;

import fr.fresnel.fourPolar.core.imagingSetup.FourPolarImagingSetup;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.FieldOfView;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.Rectangle;
import fr.fresnel.fourPolar.core.physics.channel.PropagationChannel;
import fr.fresnel.fourPolar.core.physics.na.INumericalAperture;
import fr.fresnel.fourPolar.core.physics.na.NumericalAperture;
import fr.fresnel.fourPolar.io.PathFactory;
import fr.fresnel.fourPolar.io.imagingSetup.imageFormation.fov.IFieldOfViewToYamlAdaptor;
import fr.fresnel.fourPolar.io.physics.channel.IPropagationChannelToYamlAdaptor;
import fr.fresnel.fourPolar.io.physics.na.INumericalApertureToYamlAdaptor;

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
        File destFile = new File(this._getDestinationFolder(), this._getFileName());

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory().disable(Feature.WRITE_DOC_START_MARKER));
        mapper.findAndRegisterModules();

        mapper.writeValue(destFile, this);
    }

    @JsonProperty
    private IFieldOfViewToYamlAdaptor getFieldOfView() {
        return new IFieldOfViewToYamlAdaptor(this._imagingSetup.getFieldOfView());
    }

    @JsonProperty
    private INumericalApertureToYamlAdaptor getNumericalAperture() {
        return new INumericalApertureToYamlAdaptor(this._imagingSetup.getNumericalAperture());
    }

    @JsonProperty
    private IPropagationChannelToYamlAdaptor[] getChannels() {
        int nchannel = this._imagingSetup.getnChannel();
        IPropagationChannelToYamlAdaptor[] cAdaptors = new IPropagationChannelToYamlAdaptor[nchannel];

        for (int channel = 1; channel <= nchannel; channel++) {
            cAdaptors[channel] = new IPropagationChannelToYamlAdaptor(
                    this._imagingSetup.getPropagationChannel(channel));
        }
        return cAdaptors;
    }

    @JsonProperty
    private Cameras getNCameras(){
        return this.getNCameras();
    }

    private File _getDestinationFolder() {
        return PathFactory.getFolder_0_Params(this._rootFolder);        
    }

    private String _getFileName() {
        return "ImagingSetup.yaml";
    }

    public static void main(String[] args) {
        FourPolarImagingSetup imagingSetup = new FourPolarImagingSetup(4, Cameras.One);

        Rectangle rect0 = new Rectangle(1, 1, 1, 1);
        FieldOfView fov = new FieldOfView(rect0, rect0, rect0, rect0);
        imagingSetup.setFieldOfView(fov);

        INumericalAperture na = new NumericalAperture(3.45, 5.65, 43.4342, 1.3434);
        imagingSetup.setNumericalAperture(na);

        PropagationChannel prop = new PropagationChannel(1, 1.45, 1.54, 1.34, 3.11);
        imagingSetup.setPropagationChannel(1, prop);
        imagingSetup.setPropagationChannel(2, prop);
        imagingSetup.setPropagationChannel(3, prop);
        imagingSetup.setPropagationChannel(4, prop);


        File file = new File("BigClass.yaml");


    }
}