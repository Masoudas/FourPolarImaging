package fr.fresnel.fourPolar.io.physics.na;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fr.fresnel.fourPolar.core.physics.na.INumericalAperture;
import fr.fresnel.fourPolar.core.physics.polarization.Polarizations;

/**
 * This class is used as an adaptor of {@link INumericalAperture} adaptor to yaml.
 */
@JsonPropertyOrder({"pol0", "pol45", "pol90", "pol135"})
public class INumericalApertureToYamlAdaptor {
    private INumericalAperture _na;

    public INumericalApertureToYamlAdaptor(INumericalAperture na) {
        this._na = na;
    }

    @JsonProperty
    private double getPol0() {
        return this._na.getNA(Polarizations.pol0);
    }

    @JsonProperty
    private double getPol45() {
        return this._na.getNA(Polarizations.pol45);
    }

    @JsonProperty
    private double getPol90() {
        return this._na.getNA(Polarizations.pol90);
    }
    
    @JsonProperty
    private double getPol135() {
        return this._na.getNA(Polarizations.pol135);
    }

}