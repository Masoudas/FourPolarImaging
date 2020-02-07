package fr.fresnel.fourPolar.io.physics.na;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fr.fresnel.fourPolar.core.physics.na.INumericalAperture;
import fr.fresnel.fourPolar.core.physics.na.NumericalAperture;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

/**
 * This class is used as an adaptor of {@link INumericalAperture} adaptor to
 * yaml.
 */
@JsonPropertyOrder({ "pol0", "pol45", "pol90", "pol135" })
public class INumericalApertureJSONAdaptor {
    @JsonProperty("pol0")
    private Double _pol0;

    @JsonProperty("pol45")
    private Double _pol45;

    @JsonProperty("pol90")
    private Double _pol90;

    @JsonProperty("pol135")
    private Double _pol135;

    /**
     * Allows the interface to be written to JSON as an instance of this class.
     * 
     * @param na
     */
    public void toJSON(INumericalAperture na) {
        _setPol0(na);
        _setPol45(na);
        _setPol90(na);
        _setPol135(na);
    }

    /**
     * Returns the interface from the JSON.
     * 
     * @return
     * @throws IOException
     */
    public INumericalAperture fromJSON() throws IOException {
        return new NumericalAperture(_getPol0(), _getPol45(), _getPol90(), _getPol135());
    }

    private void _setPol0(INumericalAperture na) {
        _pol0 = na.getNA(Polarization.pol0);
    }

    private void _setPol45(INumericalAperture na) {
        _pol45 = na.getNA(Polarization.pol45);
    }

    private void _setPol90(INumericalAperture na) {
        _pol90 = na.getNA(Polarization.pol90);
    }

    private void _setPol135(INumericalAperture na) {
        _pol135 = na.getNA(Polarization.pol135);
    }

    private double _getPol0() throws IOException {
        if (_pol0 == null) {
            throw new IOException(_getNotExistsMessage(Polarization.pol0));
        }
        return _pol0;
    }

    private double _getPol45() throws IOException {
        if (_pol45 == null) {
            throw new IOException(_getNotExistsMessage(Polarization.pol45));
        }

        return _pol45;
    }

    private double _getPol90() throws IOException {
        if (_pol90 == null) {
            throw new IOException(_getNotExistsMessage(Polarization.pol90));
        }

        return _pol90;
    }

    private double _getPol135() throws IOException {
        if (_pol135 == null) {
            throw new IOException(_getNotExistsMessage(Polarization.pol90));
        }
        return _pol135;
    }

    private String _getNotExistsMessage(Polarization pol) {
        return "Numerical aperture for " + pol + " is not found on the disk";
    }

}