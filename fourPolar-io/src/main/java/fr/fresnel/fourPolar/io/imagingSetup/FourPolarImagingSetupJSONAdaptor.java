package fr.fresnel.fourPolar.io.imagingSetup;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fr.fresnel.fourPolar.core.imagingSetup.FourPolarImagingSetup;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.io.imagingSetup.imageFormation.fov.IFieldOfViewJSONAdaptor;
import fr.fresnel.fourPolar.io.physics.channel.IPropagationChannelJSONAdaptor;
import fr.fresnel.fourPolar.io.physics.na.INumericalApertureJSONAdaptor;

/**
 * This class is used for writing the fourPolar imaging setup to disk.
 */
@JsonPropertyOrder({ "Number Of Cameras", "Field Of View", "Numerical Aperture", "Channels" })
class FourPolarImagingSetupJSONAdaptor {
    @JsonProperty("Field Of View")
    private IFieldOfViewJSONAdaptor _fovAdaptor;

    @JsonProperty("Numerical Aperture")
    private INumericalApertureJSONAdaptor _naAdaptor;

    @JsonProperty("Channels")
    private IPropagationChannelJSONAdaptor[] _channelAdaptor;

    @JsonProperty("Number Of Cameras")
    private Cameras _cameras;

    public void toYaml(FourPolarImagingSetup imagingSetup) {
        _setFieldOfViewAdaptor(imagingSetup);
        _setNumericalApertureAdaptor(imagingSetup);
        _setChannels(imagingSetup);
        _setNCameras(imagingSetup);
    }

    public FourPolarImagingSetup fromYaml() {
        FourPolarImagingSetup imagingSetup = new FourPolarImagingSetup(
            this._channelAdaptor.length, this._cameras);

        imagingSetup.setFieldOfView(this._fovAdaptor.fromJSON());    
        imagingSetup.setNumericalAperture(this._naAdaptor.fromJSON());
        
        for (int channel = 1; channel <= this._channelAdaptor.length; channel++) {            
            imagingSetup.setPropagationChannel(channel, this._channelAdaptor[channel-1].fromJSON());    
        }
 
        return imagingSetup;       
    }

    private void _setFieldOfViewAdaptor(FourPolarImagingSetup imagingSetup) {
        _fovAdaptor = new IFieldOfViewJSONAdaptor();
        _fovAdaptor.toJSON(imagingSetup.getFieldOfView());
    }

    private void _setNumericalApertureAdaptor(FourPolarImagingSetup imagingSetup) {
        _naAdaptor = new INumericalApertureJSONAdaptor();
        _naAdaptor.toJSON(imagingSetup.getNumericalAperture());
    }

    private void _setChannels(FourPolarImagingSetup imagingSetup) {
        int nchannel = imagingSetup.getnChannel();
        this._channelAdaptor = new IPropagationChannelJSONAdaptor[nchannel];

        for (int channel = 1; channel <= nchannel; channel++) {
            this._channelAdaptor[channel - 1] = new IPropagationChannelJSONAdaptor();
            this._channelAdaptor[channel - 1].toJSON(channel, imagingSetup.getPropagationChannel(channel));
        }

    }

    private void _setNCameras(FourPolarImagingSetup imagingSetup) {
        _cameras = imagingSetup.getCameras();
    }
}