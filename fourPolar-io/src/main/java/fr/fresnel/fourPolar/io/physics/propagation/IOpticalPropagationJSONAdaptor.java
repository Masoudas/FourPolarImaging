package fr.fresnel.fourPolar.io.physics.propagation;

import com.fasterxml.jackson.annotation.JsonProperty;

import fr.fresnel.fourPolar.core.physics.dipole.DipoleSquaredComponent;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.physics.propagation.IOpticalPropagation;

/**
 * This class adapts the {@link IOpticalPropagation} interface to JSON.
 */
class IOpticalPropagationJSONAdaptor {
    final private IOpticalPropagation _iPropagation;

    /**
     * This class adapts the {@link IOpticalPropagation} interface to JSON.
     * @param iOpticalPropagation
     */
    public IOpticalPropagationJSONAdaptor(IOpticalPropagation iPropagation) {
        this._iPropagation = iPropagation;
    }

    @JsonProperty("Dipole-squared XX to pol0")
    public void getDipoleSquaredXXtoPol0() {
        this._iPropagation.getPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol0);
    }

    @JsonProperty("Dipole-squared YY to pol0")
    public void getDipoleSquaredYYtoPol0() {
        this._iPropagation.getPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol0);
    }

    @JsonProperty("Dipole-squared ZZ to pol0")
    public void getDipoleSquaredZZtoPol0() {
        this._iPropagation.getPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol0);
    }

    @JsonProperty("Dipole-squared XY to pol0")
    public void getDipoleSquaredXYtoPol0() {
        this._iPropagation.getPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol0);
    }

    @JsonProperty("Dipole-squared XX to pol90")
    public void getDipoleSquaredXXtoPol90() {
        this._iPropagation.getPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol90);
    }

    @JsonProperty("Dipole-squared YY to pol90")
    public void getDipoleSquaredYYtoPol90() {
        this._iPropagation.getPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol90);
    }

    @JsonProperty("Dipole-squared ZZ to pol90")
    public void getDipoleSquaredZZtoPol90() {
        this._iPropagation.getPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol90);
    }

    @JsonProperty("Dipole-squared XY to pol90")
    public void getDipoleSquaredXYtoPol90() {
        this._iPropagation.getPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol90);
    }

    @JsonProperty("Dipole-squared XX to pol45")
    public void getDipoleSquaredXXtoPol45() {
        this._iPropagation.getPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol45);
    }

    @JsonProperty("Dipole-squared YY to pol45")
    public void getDipoleSquaredYYtoPol45() {
        this._iPropagation.getPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol45);
    }

    @JsonProperty("Dipole-squared ZZ to pol45")
    public void getDipoleSquaredZZtoPol45() {
        this._iPropagation.getPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol45);
    }

    @JsonProperty("Dipole-squared XY to pol45")
    public void getDipoleSquaredXYtoPol45() {
        this._iPropagation.getPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol45);
    }

    @JsonProperty("Dipole-squared XX to pol135")
    public void getDipoleSquaredXXtoPol135() {
        this._iPropagation.getPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol135);
    }

    @JsonProperty("Dipole-squared YY to pol135")
    public void getDipoleSquaredYYtoPol135() {
        this._iPropagation.getPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol135);
    }

    @JsonProperty("Dipole-squared ZZ to pol135")
    public void getDipoleSquaredZZtoPol135() {
        this._iPropagation.getPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol135);
    }

    @JsonProperty("Dipole-squared XY to pol135")
    public void getDipoleSquaredXYtoPol135() {
        this._iPropagation.getPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol135);
    }

    
}