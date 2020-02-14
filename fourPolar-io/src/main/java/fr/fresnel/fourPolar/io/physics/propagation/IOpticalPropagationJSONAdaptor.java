package fr.fresnel.fourPolar.io.physics.propagation;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fr.fresnel.fourPolar.core.exceptions.physics.propagation.PropagationFactorNotFound;
import fr.fresnel.fourPolar.core.physics.channel.IChannel;
import fr.fresnel.fourPolar.core.physics.dipole.DipoleSquaredComponent;
import fr.fresnel.fourPolar.core.physics.na.INumericalAperture;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.physics.propagation.IOpticalPropagation;
import fr.fresnel.fourPolar.core.physics.propagation.OpticalPropagation;
import fr.fresnel.fourPolar.io.physics.channel.IChannelJSONAdaptor;
import fr.fresnel.fourPolar.io.physics.na.INumericalApertureJSONAdaptor;

/**
 * This class adapts the {@link IOpticalPropagation} interface to JSON.
 */
@JsonPropertyOrder({IOpticalPropagationJSONLabels.channel, IOpticalPropagationJSONLabels.na})
public class IOpticalPropagationJSONAdaptor {
    @JsonProperty(IOpticalPropagationJSONLabels.xx_0)
    private Double _xx_0;

    @JsonProperty(IOpticalPropagationJSONLabels.yy_0)
    private Double _yy_0;

    @JsonProperty(IOpticalPropagationJSONLabels.zz_0)
    private Double _zz_0;

    @JsonProperty(IOpticalPropagationJSONLabels.xy_0)
    private Double _xy_0;

    @JsonProperty(IOpticalPropagationJSONLabels.xx_90)
    private Double _xx_90;

    @JsonProperty(IOpticalPropagationJSONLabels.yy_90)
    private Double _yy_90;

    @JsonProperty(IOpticalPropagationJSONLabels.zz_90)
    private Double _zz_90;

    @JsonProperty(IOpticalPropagationJSONLabels.xy_90)
    private Double _xy_90;

    @JsonProperty(IOpticalPropagationJSONLabels.xx_45)
    private Double _xx_45;

    @JsonProperty(IOpticalPropagationJSONLabels.yy_45)
    private Double _yy_45;

    @JsonProperty(IOpticalPropagationJSONLabels.zz_45)
    private Double _zz_45;

    @JsonProperty(IOpticalPropagationJSONLabels.xy_45)
    private Double _xy_45;

    @JsonProperty(IOpticalPropagationJSONLabels.xx_135)
    private Double _xx_135;

    @JsonProperty(IOpticalPropagationJSONLabels.yy_135)
    private Double _yy_135;

    @JsonProperty(IOpticalPropagationJSONLabels.zz_135)
    private Double _zz_135;

    @JsonProperty(IOpticalPropagationJSONLabels.xy_135)
    private Double _xy_135;

    @JsonProperty(IOpticalPropagationJSONLabels.channel)
    private IChannelJSONAdaptor _channelAdaptor;

    @JsonProperty(IOpticalPropagationJSONLabels.na)
    private INumericalApertureJSONAdaptor _naAdaptor;


    public IOpticalPropagationJSONAdaptor() {
        _channelAdaptor = new IChannelJSONAdaptor();
        _naAdaptor = new INumericalApertureJSONAdaptor();
    }

    /**
     * Returns the content of the JSON that holds the instance of
     * {@link IOpticalPropagation}.
     * 
     * @return
     * @throws IOException
     */
    public IOpticalPropagation fromJSON() throws IOException {
        IChannel channel = _getChannel();
        INumericalAperture na = _getNumericalAperture();

        OpticalPropagation opticalPropagation = new OpticalPropagation(channel, na);

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

        _setChannel(opticalPropagation);
        _setNumericalAperture(opticalPropagation);
    }

    private void _getPropFactorXXtoPol0(IOpticalPropagation opticalPropagation) throws IOException{
        if (_xx_0 == null){
            throw new IOException(_getFactorNotExistsMessage(DipoleSquaredComponent.XX, Polarization.pol0));
        }
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol0, _xx_0);
    }

    private void _getPropFactorYYtoPol0(IOpticalPropagation opticalPropagation) throws IOException{
        if (_yy_0 == null){
            throw new IOException(_getFactorNotExistsMessage(DipoleSquaredComponent.YY, Polarization.pol0));
        }
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol0, _yy_0);
    }

    private void _getPropFactorZZtoPol0(IOpticalPropagation opticalPropagation) throws IOException{
        if (_zz_0 == null){
            throw new IOException(_getFactorNotExistsMessage(DipoleSquaredComponent.ZZ, Polarization.pol0));
        }
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol0, _zz_0);
    }

    private void _getPropFactorXYtoPol0(IOpticalPropagation opticalPropagation) throws IOException{
        if (_xy_0 == null){
            throw new IOException(_getFactorNotExistsMessage(DipoleSquaredComponent.XY, Polarization.pol0));
        }
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol0, _xy_0);
    }

    private void _getPropFactorXXtoPol90(IOpticalPropagation opticalPropagation) throws IOException{
        if (_xx_90 == null){
            throw new IOException(_getFactorNotExistsMessage(DipoleSquaredComponent.XX, Polarization.pol90));
        }
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol90, _xx_90);
    }

    private void _getPropFactorYYtoPol90(IOpticalPropagation opticalPropagation) throws IOException{
        if (_yy_90 == null){
            throw new IOException(_getFactorNotExistsMessage(DipoleSquaredComponent.YY, Polarization.pol90));
        }
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol90, _yy_90);
    }

    private void _getPropFactorZZtoPol90(IOpticalPropagation opticalPropagation) throws IOException{
        if (_zz_90 == null){
            throw new IOException(_getFactorNotExistsMessage(DipoleSquaredComponent.ZZ, Polarization.pol90));
        }
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol90, _zz_90);
    }

    private void _getPropFactorXYtoPol90(IOpticalPropagation opticalPropagation) throws IOException{
        if (_xy_90 == null){
            throw new IOException(_getFactorNotExistsMessage(DipoleSquaredComponent.XY, Polarization.pol90));
        }
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol90, _xy_90);
    }

    private void _getPropFactorXXtoPol45(IOpticalPropagation opticalPropagation) throws IOException{
        if (_xx_45 == null){
            throw new IOException(_getFactorNotExistsMessage(DipoleSquaredComponent.XX, Polarization.pol45));
        }
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol45, _xx_45);
    }

    private void _getPropFactorYYtoPol45(IOpticalPropagation opticalPropagation) throws IOException{
        if (_yy_45 == null){
            throw new IOException(_getFactorNotExistsMessage(DipoleSquaredComponent.YY, Polarization.pol45));
        }
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol45, _yy_45);
    }

    private void _getPropFactorZZtoPol45(IOpticalPropagation opticalPropagation) throws IOException{
        if (_zz_45 == null){
            throw new IOException(_getFactorNotExistsMessage(DipoleSquaredComponent.ZZ, Polarization.pol45));
        }
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol45, _zz_45);
    }

    private void _getPropFactorXYtoPol45(IOpticalPropagation opticalPropagation) throws IOException{
        if (_xy_45 == null){
            throw new IOException(_getFactorNotExistsMessage(DipoleSquaredComponent.XY, Polarization.pol45));
        }
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol45, _xy_45);
    }

    private void _getPropFactorXXtoPol135(IOpticalPropagation opticalPropagation) throws IOException{
        if (_xx_135 == null){
            throw new IOException(_getFactorNotExistsMessage(DipoleSquaredComponent.XX, Polarization.pol135));
        }
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol135, _xx_135);
    }

    private void _getPropFactorYYtoPol135(IOpticalPropagation opticalPropagation) throws IOException{
        if (_yy_135 == null){
            throw new IOException(_getFactorNotExistsMessage(DipoleSquaredComponent.YY, Polarization.pol135));
        }
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol135, _yy_135);
    }

    private void _getPropFactorZZtoPol135(IOpticalPropagation opticalPropagation) throws IOException{
        if (_zz_135 == null){
            throw new IOException(_getFactorNotExistsMessage(DipoleSquaredComponent.ZZ, Polarization.pol135));
        }
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol135, _zz_135);
    }

    private void _getPropFactorXYtoPol135(IOpticalPropagation opticalPropagation) throws IOException{
        if (_xy_135 == null){
            throw new IOException(_getFactorNotExistsMessage(DipoleSquaredComponent.XY, Polarization.pol135));
        }
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol135, _xy_135);
    }

    private IChannel _getChannel() throws IOException{
        return _channelAdaptor.fromJSON();
    }

    private void _setChannel(IOpticalPropagation opticalPropagation) {
        this._channelAdaptor.toJSON(opticalPropagation.getChannel());
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

    private INumericalAperture _getNumericalAperture() throws IOException {
        return _naAdaptor.fromJSON();
    }


    private void _setNumericalAperture(IOpticalPropagation opticalPropagation) {
        _naAdaptor.toJSON(opticalPropagation.getNumericalAperture());
    }

    private String _getFactorNotExistsMessage(DipoleSquaredComponent component, Polarization pol){
        return "Propagation factor from Dipole squared " + component + " to polarization " + pol + " does not exist!";
    }


}
