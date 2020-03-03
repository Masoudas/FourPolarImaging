package fr.fresnel.fourPolar.algorithm.fourPolar.converters;

import fr.fresnel.fourPolar.core.exceptions.physics.propagation.PropagationFactorNotFound;
import fr.fresnel.fourPolar.core.physics.dipole.DipoleSquaredComponent;
import fr.fresnel.fourPolar.core.physics.dipole.IOrientationVector;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;
import fr.fresnel.fourPolar.core.physics.polarization.IPolarizationsIntensity;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.physics.polarization.PolarizationsIntensity;
import fr.fresnel.fourPolar.core.physics.propagation.IOpticalPropagation;

/**
 * A concreter implementation of the {@link IOrientationToIntensityConverter}.
 */
public class OrientationToIntensityConverter implements IOrientationToIntensityConverter {
    final private IOpticalPropagation _opticalPropagation;

    private double _cosHalfDelta;

    private double _cosEta;
    private double _cosSquaredEta;

    private double _sinEta;
    private double _sinSquaredEta;

    private double _sinRho;
    private double _sin2Rho;
    private double _sinSquaredRho;
    private double _sinFourRho;

    private double _cosSquaredRho;
    private double _cosFourRho;

    private double _dipoleAmplitude_a;
    private double _dipoleAmplitude_b;

    private double _dipoleSquared_XX;
    private double _dipoleSquared_YY;
    private double _dipoleSquared_ZZ;
    private double _dipoleSquared_XY;

    final private double _propFactor_xx_0;
    final private double _propFactor_yy_0;
    final private double _propFactor_zz_0;
    final private double _propFactor_xy_0;
    final private double _propFactor_xx_90;
    final private double _propFactor_yy_90;
    final private double _propFactor_zz_90;
    final private double _propFactor_xy_90;
    final private double _propFactor_xx_45;
    final private double _propFactor_yy_45;
    final private double _propFactor_zz_45;
    final private double _propFactor_xy_45;
    final private double _propFactor_xx_135;
    final private double _propFactor_yy_135;
    final private double _propFactor_zz_135;
    final private double _propFactor_xy_135;

    /**
     * This implementation uses an instance of the {@link IOpticalPropagation} to
     * convert an orientation vector to a polarizations intensity.
     * 
     * @param inverseProp contains the inverse optical propagation factors.
     */
    public OrientationToIntensityConverter(IOpticalPropagation opticalPropagation) throws PropagationFactorNotFound {
        this._opticalPropagation = opticalPropagation;

        _propFactor_xx_0 = this._opticalPropagation.getPropagationFactor(
            DipoleSquaredComponent.XX, Polarization.pol0);
        _propFactor_yy_0 = this._opticalPropagation.getPropagationFactor(
            DipoleSquaredComponent.YY, Polarization.pol0);
        _propFactor_zz_0 = this._opticalPropagation.getPropagationFactor(
            DipoleSquaredComponent.ZZ, Polarization.pol0);
        _propFactor_xy_0 = this._opticalPropagation.getPropagationFactor(
            DipoleSquaredComponent.XY, Polarization.pol0);
        _propFactor_xx_90 = this._opticalPropagation.getPropagationFactor(
            DipoleSquaredComponent.XX, Polarization.pol90);
        _propFactor_yy_90 = this._opticalPropagation.getPropagationFactor(
            DipoleSquaredComponent.YY, Polarization.pol90);
        _propFactor_zz_90 = this._opticalPropagation.getPropagationFactor(
            DipoleSquaredComponent.ZZ, Polarization.pol90);
        _propFactor_xy_90 = this._opticalPropagation.getPropagationFactor(
            DipoleSquaredComponent.XY, Polarization.pol90);
        _propFactor_xx_45 = this._opticalPropagation.getPropagationFactor(
            DipoleSquaredComponent.XX, Polarization.pol45);
        _propFactor_yy_45 = this._opticalPropagation.getPropagationFactor(
            DipoleSquaredComponent.YY, Polarization.pol45);
        _propFactor_zz_45 = this._opticalPropagation.getPropagationFactor(
            DipoleSquaredComponent.ZZ, Polarization.pol45);
        _propFactor_xy_45 = this._opticalPropagation.getPropagationFactor(
            DipoleSquaredComponent.XY, Polarization.pol45);
        _propFactor_xx_135 = this._opticalPropagation.getPropagationFactor(
            DipoleSquaredComponent.XX, Polarization.pol135);
        _propFactor_yy_135 = this._opticalPropagation.getPropagationFactor(
            DipoleSquaredComponent.YY, Polarization.pol135);
        _propFactor_zz_135 = this._opticalPropagation.getPropagationFactor(
            DipoleSquaredComponent.ZZ, Polarization.pol135);
        _propFactor_xy_135 = this._opticalPropagation.getPropagationFactor(
            DipoleSquaredComponent.XY, Polarization.pol135);
    }

    @Override
    public IPolarizationsIntensity convert(IOrientationVector orientationVector) {
        _cosHalfDelta = Math.cos(orientationVector.getAngle(OrientationAngle.delta) / 2);

        _cosEta = Math.cos(orientationVector.getAngle(OrientationAngle.eta));
        _cosSquaredEta = _cosEta * _cosEta;

        _sinEta = Math.sin(orientationVector.getAngle(OrientationAngle.eta));
        _sinSquaredEta = _sinEta * _sinEta;

        _sinRho = Math.sin(orientationVector.getAngle(OrientationAngle.eta));
        _sin2Rho = Math.sin(2 * orientationVector.getAngle(OrientationAngle.eta));
        _sinSquaredRho = _sinRho * _sinRho;
        _sinFourRho = _sinSquaredRho * _sinSquaredRho;

        _cosSquaredRho = 1 - _sinSquaredRho;
        _cosFourRho = _cosSquaredRho * _cosSquaredRho;

        _dipoleAmplitude_a = (1 - _cosHalfDelta) * (2 + _cosHalfDelta) / 6;
        _dipoleAmplitude_b = (_cosHalfDelta * _cosHalfDelta * _cosHalfDelta - 1) / (3 * (_cosHalfDelta - 1));

        this._getDipoleSquared_XX();
        this._getDipoleSquared_YY();
        this._getDipoleSquared_ZZ();
        this._getDipoleSquared_XY();

        IPolarizationsIntensity intensity = null;
        try {
            intensity = new PolarizationsIntensity(_getPol0Intensity(), _getPol45Intensity(), _getPol90Intensity(),
                    _getPol135Intensity());
        } catch (PropagationFactorNotFound e) {
            // This exception is never caught, because the constructor checks for it.
        }

        return intensity;
    }

    private double _getPol0Intensity() throws PropagationFactorNotFound {
        return _dipoleSquared_XX * _propFactor_xx_0 + _dipoleSquared_YY * _propFactor_yy_0
            + _dipoleSquared_ZZ * _propFactor_zz_0 + _dipoleSquared_XY * _propFactor_xy_0;
    }

    private double _getPol45Intensity() throws PropagationFactorNotFound {
        return _dipoleSquared_XX * _propFactor_xx_45 + _dipoleSquared_YY * _propFactor_yy_45
            + _dipoleSquared_ZZ * _propFactor_zz_45 + _dipoleSquared_XY * _propFactor_xy_45;
    }

    private double _getPol90Intensity() throws PropagationFactorNotFound {
        return _dipoleSquared_XX * _propFactor_xx_90 + _dipoleSquared_YY * _propFactor_yy_90
            + _dipoleSquared_ZZ * _propFactor_zz_90 + _dipoleSquared_XY * _propFactor_xy_90;
    }

    private double _getPol135Intensity() throws PropagationFactorNotFound {
        return _dipoleSquared_XX * _propFactor_xx_135 + _dipoleSquared_YY * _propFactor_yy_135
            + _dipoleSquared_ZZ * _propFactor_zz_135 + _dipoleSquared_XY * _propFactor_xy_135;
    }

    private double _getDipoleSquared_XX() {
        return _dipoleAmplitude_b * ((2 * (_cosSquaredEta + 1) * _sin2Rho + _sinFourRho + _cosSquaredEta * _cosFourRho)
            + _dipoleAmplitude_a * _sinSquaredEta * _cosSquaredRho);
    }

    private double _getDipoleSquared_YY() {
        return _dipoleAmplitude_b * ((2 * (_cosSquaredEta + 1) * _sin2Rho + _cosFourRho + _cosSquaredEta * _sinFourRho)
                + _dipoleAmplitude_a * _sinSquaredEta * _sinSquaredRho);
    }

    private double _getDipoleSquared_ZZ() {
        return _dipoleAmplitude_b * _sinSquaredEta + _dipoleAmplitude_a * _cosSquaredEta;
    }

    private double _getDipoleSquared_XY() {
        return 0.5 * _sin2Rho * _sinSquaredEta * (_dipoleAmplitude_a - _dipoleAmplitude_b);
    }
}