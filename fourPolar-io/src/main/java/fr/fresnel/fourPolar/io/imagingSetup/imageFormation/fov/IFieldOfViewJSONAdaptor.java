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
@JsonPropertyOrder({ "pol0", "pol45", "pol90", "pol135" })
public class IFieldOfViewJSONAdaptor {
    @JsonProperty("pol0")
    private RectangleJSONAdaptor _pol0;

    @JsonProperty("pol45")
    private RectangleJSONAdaptor _pol45;

    @JsonProperty("pol90")
    private RectangleJSONAdaptor _pol90;

    @JsonProperty("pol135")
    private RectangleJSONAdaptor _pol135;

    /**
     * Allows the interface to be written to JSON as an instance of this class.
     * 
     * @param fov
     */
    public void toJSON(IFieldOfView fov) {
        _setPol0(fov);
        _setPol45(fov);
        _setPol90(fov);
        _setPol135(fov);
    }

    /**
     * Returns the interface from the JSON file.
     * 
     * @return
     */
    public IFieldOfView fromJSON() {
        return new FieldOfView(this._pol0.fromYaml(), this._pol45.fromYaml(), this._pol90.fromYaml(),
                this._pol135.fromYaml());
    }

    private void _setPol0(IFieldOfView fov) {
        _pol0 = new RectangleJSONAdaptor();
        _pol0.toYaml(fov.getFoV(Polarizations.pol0));
    }

    private void _setPol45(IFieldOfView fov) {
        _pol45 = new RectangleJSONAdaptor();
        _pol45.toYaml(fov.getFoV(Polarizations.pol45));
    }

    private void _setPol90(IFieldOfView fov) {
        _pol90 = new RectangleJSONAdaptor();
        _pol90.toYaml(fov.getFoV(Polarizations.pol90));
    }

    private void _setPol135(IFieldOfView fov) {
        _pol135 = new RectangleJSONAdaptor();
        _pol135.toYaml(fov.getFoV(Polarizations.pol135));
    }

}