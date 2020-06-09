package fr.fresnel.fourPolar.io.imagingSetup.imageFormation.fov;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.FieldOfView;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.IFieldOfView;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.util.shape.IBoxShape;
import fr.fresnel.fourPolar.core.util.shape.ShapeFactory;
import fr.fresnel.fourPolar.io.util.shape.IBoxShape2DJSONAdaptor;

/**
 * This class is used as an adaptor of {@link IFieldOfView} to JSON. Note that
 * when writing the fov, we shift all pixels by one pixel to implicitly include
 * the fact that pixels start from one. Hence when reading, we shift all values
 * back by one as well.
 */
@JsonPropertyOrder({ "pol0", "pol45", "pol90", "pol135" })
public class IFieldOfViewJSONAdaptor {
    private static final long _FOV_SHIFT = 1;

    @JsonProperty("pol0")
    private IBoxShape2DJSONAdaptor _pol0;

    @JsonProperty("pol45")
    private IBoxShape2DJSONAdaptor _pol45;

    @JsonProperty("pol90")
    private IBoxShape2DJSONAdaptor _pol90;

    @JsonProperty("pol135")
    private IBoxShape2DJSONAdaptor _pol135;

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
        IBoxShape pol0 = this._pol0.fromYaml();
        IBoxShape shiftedPol0 = _shiftFoV(pol0, -_FOV_SHIFT);

        IBoxShape pol45 = this._pol45.fromYaml();
        IBoxShape shiftedPol45 = _shiftFoV(pol45, -_FOV_SHIFT);

        IBoxShape pol90 = this._pol90.fromYaml();
        IBoxShape shiftedPol90 = _shiftFoV(pol90, -_FOV_SHIFT);

        IBoxShape pol135 = this._pol135.fromYaml();
        IBoxShape shiftedPol134 = _shiftFoV(pol135, -_FOV_SHIFT);

        return new FieldOfView(shiftedPol0, shiftedPol45, shiftedPol90, shiftedPol134);
    }

    private void _setPol0(IFieldOfView fov) {
        IBoxShape pol0AsBox = fov.getFoV(Polarization.pol0);
        IBoxShape shiftedPol0AsBox = this._shiftFoV(pol0AsBox, _FOV_SHIFT);

        _pol0 = new IBoxShape2DJSONAdaptor();
        _pol0.toYaml(shiftedPol0AsBox);
    }

    private void _setPol45(IFieldOfView fov) {
        IBoxShape pol45AsBox = fov.getFoV(Polarization.pol45);
        IBoxShape shiftedPol45AsBox = this._shiftFoV(pol45AsBox, _FOV_SHIFT);

        _pol45 = new IBoxShape2DJSONAdaptor();
        _pol45.toYaml(shiftedPol45AsBox);
    }

    private void _setPol90(IFieldOfView fov) {
        IBoxShape pol90AsBox = fov.getFoV(Polarization.pol90);
        IBoxShape shiftedPol90AsBox = this._shiftFoV(pol90AsBox, _FOV_SHIFT);

        _pol90 = new IBoxShape2DJSONAdaptor();
        _pol90.toYaml(shiftedPol90AsBox);
    }

    private void _setPol135(IFieldOfView fov) {
        IBoxShape pol135AsBox = fov.getFoV(Polarization.pol135);
        IBoxShape shiftedPol135AsBox = this._shiftFoV(pol135AsBox, _FOV_SHIFT);

        _pol135 = new IBoxShape2DJSONAdaptor();
        _pol135.toYaml(shiftedPol135AsBox);
    }

    private IBoxShape _shiftFoV(IBoxShape fov, long shift) {
        long[] shiftedMin = Arrays.stream(fov.min()).map((t) -> t + shift).toArray();
        long[] shiftedMax = Arrays.stream(fov.max()).map((t) -> t + shift).toArray();

        return new ShapeFactory().closedBox(shiftedMin, shiftedMax, fov.axisOrder());
    }


}