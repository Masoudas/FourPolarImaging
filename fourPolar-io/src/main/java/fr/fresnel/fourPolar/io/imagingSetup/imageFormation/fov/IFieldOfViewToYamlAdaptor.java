package fr.fresnel.fourPolar.io.imagingSetup.imageFormation.fov;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.IFieldOfView;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.Rectangle;
import fr.fresnel.fourPolar.core.physics.polarization.Polarizations;

/**
 * This class is used as an adaptor of {@link IFieldOfView} adaptor to yaml.
 */
@JsonPropertyOrder({"pol0", "pol45", "pol90", "pol135"})
public class IFieldOfViewToYamlAdaptor {
    private IFieldOfView _fov;
    
    public IFieldOfViewToYamlAdaptor(IFieldOfView fov){
        this._fov = fov;
    }

    @JsonProperty
    private Rectangle getPol0() {
        return this._fov.getFoV(Polarizations.pol0);        
    }

    @JsonProperty
    private Rectangle getPol45() {
        return this._fov.getFoV(Polarizations.pol45);        
    }

    @JsonProperty
    private Rectangle getPol90() {
        return this._fov.getFoV(Polarizations.pol90);        
    }

    @JsonProperty
    private Rectangle getPol135() {
        return this._fov.getFoV(Polarizations.pol135);        
    }
    
}