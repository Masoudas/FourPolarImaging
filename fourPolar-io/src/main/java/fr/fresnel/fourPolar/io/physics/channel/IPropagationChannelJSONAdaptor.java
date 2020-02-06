package fr.fresnel.fourPolar.io.physics.channel;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fr.fresnel.fourPolar.core.exceptions.physics.channel.CalibrationFactorOutOfRange;
import fr.fresnel.fourPolar.core.exceptions.physics.channel.WavelengthOutOfRange;
import fr.fresnel.fourPolar.core.physics.channel.IPropagationChannel;
import fr.fresnel.fourPolar.core.physics.channel.PropagationChannel;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

/**
 * This class is used as an adaptor of {@link IPropagationChannel} to JSON.
 */
@JsonPropertyOrder({ "Wavelength-nanometer", "CalibFactor-Pol0", "CalibFactor-Pol45", "CalibFactor-Pol90",
        "CalibFactor-Pol135" })
public class IPropagationChannelJSONAdaptor {
    /**
     * This is the scale used for writing the wavelength. The json description must
     * change accordingly.
     */
    private double _wavelengthScale = 1e-9;
    @JsonProperty("Wavelength-nanometer")
    private Double _wavelength;

    @JsonProperty("CalibFactor-Pol0")
    private Double _pol0;

    @JsonProperty("CalibFactor-Pol45")
    private Double _pol45;

    @JsonProperty("CalibFactor-Pol90")
    private Double _pol90;

    @JsonProperty("CalibFactor-Pol135")
    private Double _pol135;

    /**
     * Allows this class to be written as a JSon object.
     * 
     * @param channel
     */
    public void toJSON(IPropagationChannel channel) {
        setPol0(channel);
        setPol45(channel);
        setPol90(channel);
        setPol135(channel);
        setWaveLength(channel);
    }

    /**
     * Returns the content of the JSON as a proper interface Note that when
     * wavelength is read, it is scaled to go back to meter.
     * 
     * @return
     * @throws IOException
     * @throws CalibrationFactorOutOfRange
     * @throws WavelengthOutOfRange
     */
    public IPropagationChannel fromJSON() throws IOException {
        return new PropagationChannel(_getWaveLength(), _getPol0(), _getPol45(), _getPol90(),
                _getPol135());
    }

    /**
     * When wave length is read from the original propagation channel, it's devided
     * by 1e-9 to be written in nano meter unit.
     * 
     * @param channel
     */
    private void setWaveLength(IPropagationChannel channel) {
        _wavelength = channel.getWavelength() / _wavelengthScale;
    }

    private void setPol0(IPropagationChannel channel) {
        _pol0 = channel.getCalibrationFactor(Polarization.pol0);
    }

    private void setPol45(IPropagationChannel channel) {
        _pol45 = channel.getCalibrationFactor(Polarization.pol45);
    }

    private void setPol90(IPropagationChannel channel) {
        _pol90 = channel.getCalibrationFactor(Polarization.pol90);
    }

    private void setPol135(IPropagationChannel channel) {
        _pol135 = channel.getCalibrationFactor(Polarization.pol135);
    }

    private double _getWaveLength() throws IOException {
        if (_wavelength == null) {
            throw new IOException("Wavelength was not found for the channel");
        }
        return _wavelength * _wavelengthScale;
    }

    private double _getPol0() throws IOException {
        if (_wavelength == null) {
            throw new IOException(_CalibFactNotExistMessage(Polarization.pol0));
        }
        return _pol0;
    }

    private double _getPol45() throws IOException {
        if (_wavelength == null) {
            throw new IOException(_CalibFactNotExistMessage(Polarization.pol45));
        }
        return _pol45;
    }

    private double _getPol90() throws IOException {
        if (_wavelength == null) {
            throw new IOException(_CalibFactNotExistMessage(Polarization.pol90));
        }
        return _pol90;
    }

    private double _getPol135() throws IOException {
        if (_wavelength == null) {
            throw new IOException(_CalibFactNotExistMessage(Polarization.pol135));
        }
        return _pol135;
    }

    private String _CalibFactNotExistMessage(Polarization polarization) {
        return "Calibration factor was not found for " + polarization;
    }

}