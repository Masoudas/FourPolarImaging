package fr.fresnel.fourPolar.io.physics.channel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fr.fresnel.fourPolar.core.physics.channel.IPropagationChannel;
import fr.fresnel.fourPolar.core.physics.channel.PropagationChannel;
import fr.fresnel.fourPolar.core.physics.polarization.Polarizations;

/**
 * This class is used as an adaptor of {@link IPropagationChannel} to
 * JSON.
 */
@JsonPropertyOrder({ "Channel Number", "Wavelength", "Calib-Factor Pol0", "Calib-Factor Pol45", "Calib-Factor Pol90",
        "Calib-Factor Pol135" })
public class IPropagationChannelJSONAdaptor {
    @JsonProperty("Channel Number")
    private int _channalNumber;

    @JsonProperty("Wavelength")
    private double _wavelength;

    @JsonProperty("Calib-Factor Pol0")
    private double _pol0;

    @JsonProperty("Calib-Factor Pol45")
    private double _pol45;

    @JsonProperty("Calib-Factor Pol90")
    private double _pol90;

    @JsonProperty("Calib-Factor Pol135")
    private double _pol135;

    /**
     * Allows this class to be written as a JSon object.
     * 
     * @param channelNumber
     * @param channel
     */
    public void toJSON(int channelNumber, IPropagationChannel channel) {
        setPol0(channel);
        setPol45(channel);
        setPol90(channel);
        setPol135(channel);
        setnChannel(channelNumber);
        setWaveLength(channel);
    }

    /**
     * Returns the content of the JSON as a proper interface.
     * @return
     */
    public IPropagationChannel fromJSON() {
        return new PropagationChannel(
            this._wavelength, this._pol0, this._pol45, this._pol90, this._pol135);
    }

    private void setWaveLength(IPropagationChannel channel) {
        _wavelength = channel.getWavelength();
    }

    private void setPol0(IPropagationChannel channel) {
        _pol0 = channel.getCalibrationFactor(Polarizations.pol0);
    }

    private void setPol45(IPropagationChannel channel) {
        _pol45 = channel.getCalibrationFactor(Polarizations.pol45);
    }

    private void setPol90(IPropagationChannel channel) {
        _pol90 = channel.getCalibrationFactor(Polarizations.pol90);
    }

    private void setPol135(IPropagationChannel channel) {
        _pol135 = channel.getCalibrationFactor(Polarizations.pol135);
    }

    private void setnChannel(int channelNumber) {
        _channalNumber = channelNumber;
    }

}