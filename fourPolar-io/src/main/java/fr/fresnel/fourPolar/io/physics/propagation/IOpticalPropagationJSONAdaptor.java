package fr.fresnel.fourPolar.io.physics.propagation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fr.fresnel.fourPolar.core.exceptions.physics.propagation.PropagationFactorNotFound;
import fr.fresnel.fourPolar.core.physics.dipole.DipoleSquaredComponent;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.physics.propagation.IOpticalPropagation;
import fr.fresnel.fourPolar.core.physics.propagation.OpticalPropagation;
import fr.fresnel.fourPolar.io.physics.channel.IPropagationChannelJSONAdaptor;

/**
 * This class adapts the {@link IOpticalPropagation} interface to JSON.
 */
@JsonPropertyOrder(IOpticalPropagationJSONLabels.channel)
public class IOpticalPropagationJSONAdaptor {
    @JsonProperty(IOpticalPropagationJSONLabels.xx_0)
    private double _xx_0;

    @JsonProperty(IOpticalPropagationJSONLabels.yy_0)
    private double _yy_0;

    @JsonProperty(IOpticalPropagationJSONLabels.zz_0)
    private double _zz_0;

    @JsonProperty(IOpticalPropagationJSONLabels.xy_0)
    private double _xy_0;

    @JsonProperty(IOpticalPropagationJSONLabels.xx_90)
    private double _xx_90;

    @JsonProperty(IOpticalPropagationJSONLabels.yy_90)
    private double _yy_90;

    @JsonProperty(IOpticalPropagationJSONLabels.zz_90)
    private double _zz_90;

    @JsonProperty(IOpticalPropagationJSONLabels.xy_90)
    private double _xy_90;

    @JsonProperty(IOpticalPropagationJSONLabels.xx_45)
    private double _xx_45;

    @JsonProperty(IOpticalPropagationJSONLabels.yy_45)
    private double _yy_45;

    @JsonProperty(IOpticalPropagationJSONLabels.zz_45)
    private double _zz_45;

    @JsonProperty(IOpticalPropagationJSONLabels.xy_45)
    private double _xy_45;

    @JsonProperty(IOpticalPropagationJSONLabels.xx_135)
    private double _xx_135;

    @JsonProperty(IOpticalPropagationJSONLabels.yy_135)
    private double _yy_135;

    @JsonProperty(IOpticalPropagationJSONLabels.zz_135)
    private double _zz_135;

    @JsonProperty(IOpticalPropagationJSONLabels.xy_135)
    private double _xy_135;

    @JsonProperty(IOpticalPropagationJSONLabels.channel)
    private IPropagationChannelJSONAdaptor _channelAdaptor;

    public IOpticalPropagationJSONAdaptor() {
        _channelAdaptor = new IPropagationChannelJSONAdaptor();
    }

    /**
     * Returns the content of the JSON that holds the instance of
     * {@link IOpticalPropagation}.
     * 
     * @return
     */
    public IOpticalPropagation fromJSON() {
        OpticalPropagation opticalPropagation = new OpticalPropagation();

        _getPropFactorXXtoPol0(opticalPropagation);
        _getPropFactorYYtoPol0(opticalPropagation);
        _getPropFactorZZtoPol0(opticalPropagation);
        _getPropFactorXYtoPol0(opticalPropagation);

        _getPropFactorXXtoPol90(opticalPropagation);
        _getPropFactorYYtoPol90(opticalPropagation);
        _getPropFactorZZtoPol90(opticalPropagation);
        _getPropFactorXYtoPol90(opticalPropagation);

        _getPropFactorXXtoPol45(opticalPropagation);
        _getPropFactorYYtoPol45(opticalPropagation);
        _getPropFactorZZtoPol45(opticalPropagation);
        _getPropFactorXYtoPol45(opticalPropagation);

        _getPropFactorXXtoPol135(opticalPropagation);
        _getPropFactorYYtoPol135(opticalPropagation);
        _getPropFactorZZtoPol135(opticalPropagation);
        _getPropFactorXYtoPol135(opticalPropagation);

        _getPropagationChannel(opticalPropagation);

        return opticalPropagation;
    }

    /**
     * Prepares the content of this class to be serialized as a JSON based on
     * {@link IOpticalPropagation}.
     * 
     * @param opticalPropagation
     */
    public void toJSON(IOpticalPropagation opticalPropagation) {
        _setPropFactorXXtoPol0(opticalPropagation);
        _setPropFactorYYtoPol0(opticalPropagation);
        _setPropFactorZZtoPol0(opticalPropagation);
        _setPropFactorXYtoPol0(opticalPropagation);

        _setPropFactorXXtoPol90(opticalPropagation);
        _setPropFactorYYtoPol90(opticalPropagation);
        _setPropFactorZZtoPol90(opticalPropagation);
        _setPropFactorXYtoPol90(opticalPropagation);

        _setPropFactorXXtoPol45(opticalPropagation);
        _setPropFactorYYtoPol45(opticalPropagation);
        _setPropFactorZZtoPol45(opticalPropagation);
        _setPropFactorXYtoPol45(opticalPropagation);

        _setPropFactorXXtoPol135(opticalPropagation);
        _setPropFactorYYtoPol135(opticalPropagation);
        _setPropFactorZZtoPol135(opticalPropagation);
        _setPropFactorXYtoPol135(opticalPropagation);

        _setPropagationChannel(opticalPropagation);
    }

    private void _getPropFactorXXtoPol0(IOpticalPropagation opticalPropagation) {
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol0, _xx_0);
    }

    private void _getPropFactorYYtoPol0(IOpticalPropagation opticalPropagation) {
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol0, _yy_0);
    }

    private void _getPropFactorZZtoPol0(IOpticalPropagation opticalPropagation) {
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol0, _zz_0);
    }

    private void _getPropFactorXYtoPol0(IOpticalPropagation opticalPropagation) {
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol0, _xy_0);
    }

    private void _getPropFactorXXtoPol90(IOpticalPropagation opticalPropagation) {
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol90, _xx_90);
    }

    private void _getPropFactorYYtoPol90(IOpticalPropagation opticalPropagation) {
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol90, _yy_90);
    }

    private void _getPropFactorZZtoPol90(IOpticalPropagation opticalPropagation) {
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol90, _zz_90);
    }

    private void _getPropFactorXYtoPol90(IOpticalPropagation opticalPropagation) {
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol90, _xy_90);
    }

    private void _getPropFactorXXtoPol45(IOpticalPropagation opticalPropagation) {
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol45, _xx_45);
    }

    private void _getPropFactorYYtoPol45(IOpticalPropagation opticalPropagation) {
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol45, _yy_45);
    }

    private void _getPropFactorZZtoPol45(IOpticalPropagation opticalPropagation) {
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol45, _zz_45);
    }

    private void _getPropFactorXYtoPol45(IOpticalPropagation opticalPropagation) {
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol45, _xy_45);
    }

    private void _getPropFactorXXtoPol135(IOpticalPropagation opticalPropagation) {
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol135, _xx_135);
    }

    private void _getPropFactorYYtoPol135(IOpticalPropagation opticalPropagation) {
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol135, _yy_135);
    }

    private void _getPropFactorZZtoPol135(IOpticalPropagation opticalPropagation) {
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol135, _zz_135);
    }

    private void _getPropFactorXYtoPol135(IOpticalPropagation opticalPropagation) {
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol135, _xy_135);
    }

    private void _getPropagationChannel(IOpticalPropagation opticalPropagation) {
        opticalPropagation.setPropagationChannel(_channelAdaptor.fromJSON());
    }

    private void _setPropagationChannel(IOpticalPropagation opticalPropagation) {
        this._channelAdaptor.toJSON(opticalPropagation.getPropagationChannel());
    }

    private void _setPropFactorXXtoPol0(IOpticalPropagation opticalPropagation) {
        try {
            _xx_0 = opticalPropagation.getPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol0);
        } catch (PropagationFactorNotFound e) {
            // Exception never caught
        }
    }

    private void _setPropFactorYYtoPol0(IOpticalPropagation opticalPropagation) {
        try {
            _yy_0 = opticalPropagation.getPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol0);
        } catch (PropagationFactorNotFound e) {
            // Exception never caught
        }
    }

    private void _setPropFactorZZtoPol0(IOpticalPropagation opticalPropagation) {
        try {
            _zz_0 = opticalPropagation.getPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol0);
        } catch (PropagationFactorNotFound e) {
            // Exception never caught
        }
    }

    private void _setPropFactorXYtoPol0(IOpticalPropagation opticalPropagation) {
        try {
            _xy_0 = opticalPropagation.getPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol0);
        } catch (PropagationFactorNotFound e) {
            // Exception never caught
        }
    }

    private void _setPropFactorXXtoPol90(IOpticalPropagation opticalPropagation) {
        try {
            _xx_90 = opticalPropagation.getPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol90);
        } catch (PropagationFactorNotFound e) {
            // Exception never caught
        }
    }

    private void _setPropFactorYYtoPol90(IOpticalPropagation opticalPropagation) {
        try {
            _yy_90 = opticalPropagation.getPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol90);
        } catch (PropagationFactorNotFound e) {
            // Exception never caught
        }
    }

    private void _setPropFactorZZtoPol90(IOpticalPropagation opticalPropagation) {
        try {
            _zz_90 = opticalPropagation.getPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol90);
        } catch (PropagationFactorNotFound e) {
            // Exception never caught
        }
    }

    private void _setPropFactorXYtoPol90(IOpticalPropagation opticalPropagation) {
        try {
            _xy_90 = opticalPropagation.getPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol90);
        } catch (PropagationFactorNotFound e) {
            // Exception never caught
        }
    }

    private void _setPropFactorXXtoPol45(IOpticalPropagation opticalPropagation) {
        try {
            _xx_45 = opticalPropagation.getPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol45);
        } catch (PropagationFactorNotFound e) {
            // Exception never caught
        }
    }

    private void _setPropFactorYYtoPol45(IOpticalPropagation opticalPropagation) {
        try {
            _yy_45 = opticalPropagation.getPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol45);
        } catch (PropagationFactorNotFound e) {
            // Exception never caught
        }
    }

    private void _setPropFactorZZtoPol45(IOpticalPropagation opticalPropagation) {
        try {
            _zz_45 = opticalPropagation.getPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol45);
        } catch (PropagationFactorNotFound e) {
            // Exception never caught
        }
    }

    private void _setPropFactorXYtoPol45(IOpticalPropagation opticalPropagation) {
        try {
            _xy_45 = opticalPropagation.getPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol45);
        } catch (PropagationFactorNotFound e) {
            // Exception never caught
        }
    }

    private void _setPropFactorXXtoPol135(IOpticalPropagation opticalPropagation) {
        try {
            _xx_135 = opticalPropagation.getPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol135);
        } catch (PropagationFactorNotFound e) {
            // Exception never caught
        }
    }

    private void _setPropFactorYYtoPol135(IOpticalPropagation opticalPropagation) {
        try {
            _yy_135 = opticalPropagation.getPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol135);
        } catch (PropagationFactorNotFound e) {
            // Exception never caught
        }
    }

    private void _setPropFactorZZtoPol135(IOpticalPropagation opticalPropagation) {
        try {
            _zz_135 = opticalPropagation.getPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol135);
        } catch (PropagationFactorNotFound e) {
            // Exception never caught
        }
    }

    private void _setPropFactorXYtoPol135(IOpticalPropagation opticalPropagation) {
        try {
            _xy_135 = opticalPropagation.getPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol135);
        } catch (PropagationFactorNotFound e) {
            // Exception never caught
        }
    }

}
