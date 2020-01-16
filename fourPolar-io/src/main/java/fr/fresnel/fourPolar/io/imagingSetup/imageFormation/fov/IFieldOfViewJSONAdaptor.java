package fr.fresnel.fourPolar.io.imagingSetup.imageFormation.fov;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.FieldOfView;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.IFieldOfView;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.Rectangle;
import fr.fresnel.fourPolar.core.physics.polarization.Polarizations;

/**
 * This class is used as an adaptor of {@link IFieldOfView} to JSON.
 */
@JsonPropertyOrder({"pol0", "pol45", "pol90", "pol135"})
public class IFieldOfViewJSONAdaptor {
    @JsonProperty("pol0")
    private Rectangle _pol0;

    @JsonProperty("pol45")
    private Rectangle _pol45;

    @JsonProperty("pol90")
    private Rectangle _pol90;

    @JsonProperty("pol135")
    private Rectangle _pol135;

    /**
     * Allows the interface to be written to JSON as an instance of this class.
     * @param fov
     */
    public void toJSON(IFieldOfView fov){
        _setPol0(fov);
        _setPol45(fov);
        _setPol90(fov);
        _setPol135(fov);
    }

    /**
     * Returns the interface from the JSON file.
     * @return
     */
    public IFieldOfView fromJSON() {
        return new FieldOfView(this._pol0, this._pol45, this._pol90, this._pol135);
    }

    private void _setPol0(IFieldOfView fov) {
        _pol0 = fov.getFoV(Polarizations.pol0);        
    }

    private void _setPol45(IFieldOfView fov) {
        _pol45 = fov.getFoV(Polarizations.pol45);        
    }

    private void _setPol90(IFieldOfView fov) {
        _pol90 = fov.getFoV(Polarizations.pol90);        
    }

    private void _setPol135(IFieldOfView fov) {
        _pol135 = fov.getFoV(Polarizations.pol135);        
    }
    
}