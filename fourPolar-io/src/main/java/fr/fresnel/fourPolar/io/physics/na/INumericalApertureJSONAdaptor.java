package fr.fresnel.fourPolar.io.physics.na;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fr.fresnel.fourPolar.core.physics.na.INumericalAperture;
import fr.fresnel.fourPolar.core.physics.na.NumericalAperture;
import fr.fresnel.fourPolar.core.physics.polarization.Polarizations;

/**
 * This class is used as an adaptor of {@link INumericalAperture} adaptor to yaml.
 */
@JsonPropertyOrder({"pol0", "pol45", "pol90", "pol135"})
public class INumericalApertureJSONAdaptor {
    @JsonProperty("pol0")
    private double _pol0;

    @JsonProperty("pol45")
    private double _pol45;

    @JsonProperty("pol90")
    private double _pol90;

    @JsonProperty("pol135")
    private double _pol135;

    /**
     * Allows the interface to be written to JSON as an instance of this class.
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
     * @return
     */
    public INumericalAperture fromJSON() {
        return new NumericalAperture(this._pol0, this._pol45, this._pol90, this._pol135);
    }

    private void _setPol0(INumericalAperture na) {
        _pol0 = na.getNA(Polarizations.pol0);
    }

    private void _setPol45(INumericalAperture na) {
        _pol45 = na.getNA(Polarizations.pol45);
    }

    private void _setPol90(INumericalAperture na) {
        _pol90 = na.getNA(Polarizations.pol90);
    }
    
    private void _setPol135(INumericalAperture na) {
        _pol135 = na.getNA(Polarizations.pol135);
    }

}