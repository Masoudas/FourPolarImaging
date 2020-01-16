package fr.fresnel.fourPolar.io.physics.channel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fr.fresnel.fourPolar.core.physics.channel.IPropagationChannel;
import fr.fresnel.fourPolar.core.physics.polarization.Polarizations;

/**
 * This class is used as an adaptor of {@link IPropagationChannel} adaptor to
 * yaml.
 */
@JsonPropertyOrder({"wavelength", "pol0", "pol45", "pol90", "pol135"})
public class IPropagationChannelToYamlAdaptor {
    IPropagationChannel _channel;

    public IPropagationChannelToYamlAdaptor (IPropagationChannel channel){
        this._channel = channel;
    }

    @JsonProperty
    private double getWaveLength() {
        return this._channel.getWavelength();
    }

    @JsonProperty
    private double getPol0() {
        return this._channel.getCalibrationFactor(Polarizations.pol0);    
    }

    @JsonProperty
    private double getPol45() {
        return this._channel.getCalibrationFactor(Polarizations.pol45);        
    }

    @JsonProperty
    private double getPol90() {
        return this._channel.getCalibrationFactor(Polarizations.pol90);        
    }

    @JsonProperty
    private double getPol135() {
        return this._channel.getCalibrationFactor(Polarizations.pol135);        
    }
    
}