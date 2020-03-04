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
        _propFactor_xx_0 = opticalPropagation.getPropagationFactor(
            DipoleSquaredComponent.XX, Polarization.pol0);
        _propFactor_yy_0 = opticalPropagation.getPropagationFactor(
            DipoleSquaredComponent.YY, Polarization.pol0);
        _propFactor_zz_0 = opticalPropagation.getPropagationFactor(
            DipoleSquaredComponent.ZZ, Polarization.pol0);
        _propFactor_xy_0 = opticalPropagation.getPropagationFactor(
            DipoleSquaredComponent.XY, Polarization.pol0);
        _propFactor_xx_90 = opticalPropagation.getPropagationFactor(
            DipoleSquaredComponent.XX, Polarization.pol90);
        _propFactor_yy_90 = opticalPropagation.getPropagationFactor(
            DipoleSquaredComponent.YY, Polarization.pol90);
        _propFactor_zz_90 = opticalPropagation.getPropagationFactor(
            DipoleSquaredComponent.ZZ, Polarization.pol90);
        _propFactor_xy_90 = opticalPropagation.getPropagationFactor(
            DipoleSquaredComponent.XY, Polarization.pol90);
        _propFactor_xx_45 = opticalPropagation.getPropagationFactor(
            DipoleSquaredComponent.XX, Polarization.pol45);
        _propFactor_yy_45 = opticalPropagation.getPropagationFactor(
            DipoleSquaredComponent.YY, Polarization.pol45);
        _propFactor_zz_45 = opticalPropagation.getPropagationFactor(
            DipoleSquaredComponent.ZZ, Polarization.pol45);
        _propFactor_xy_45 = opticalPropagation.getPropagationFactor(
            DipoleSquaredComponent.XY, Polarization.pol45);
        _propFactor_xx_135 = opticalPropagation.getPropagationFactor(
            DipoleSquaredComponent.XX, Polarization.pol135);
        _propFactor_yy_135 = opticalPropagation.getPropagationFactor(
            DipoleSquaredComponent.YY, Polarization.pol135);
        _propFactor_zz_135 = opticalPropagation.getPropagationFactor(
            DipoleSquaredComponent.ZZ, Polarization.pol135);
        _propFactor_xy_135 = opticalPropagation.getPropagationFactor(
            DipoleSquaredComponent.XY, Polarization.pol135);    }

    @Override
    public IPolarizationsIntensity convert(IOrientationVector orientationVector) {
        double cosHalfDelta = Math.cos(orientationVector.getAngle(OrientationAngle.delta) / 2);

        double cosEta = Math.cos(orientationVector.getAngle(OrientationAngle.eta));
        double cosSquaredEta = cosEta * cosEta;

        double sinEta = Math.sin(orientationVector.getAngle(OrientationAngle.eta));
        double sinSquaredEta = sinEta * sinEta;

        double sinRho = Math.sin(orientationVector.getAngle(OrientationAngle.rho));
        double sin2Rho = Math.sin(2 * orientationVector.getAngle(OrientationAngle.rho));
        double sinSquaredRho = sinRho * sinRho;
        double sinFourRho = sinSquaredRho * sinSquaredRho;

        double cosSquaredRho = 1 - sinSquaredRho;
        double cosFourRho = cosSquaredRho * cosSquaredRho;

        double dipoleAmplitude_a = this._getDipoleAmplitude_a(cosHalfDelta);
        double dipoleAmplitude_b = this._getDipoleAmplitude_b(cosHalfDelta);

        double dipoleSquared_XX = this._getDipoleSquared_XX(dipoleAmplitude_a, dipoleAmplitude_b, cosSquaredEta,
                cosSquaredRho, sinSquaredEta, sinFourRho, cosFourRho, sin2Rho);

        double dipoleSquared_YY = this._getDipoleSquared_YY(dipoleAmplitude_a, dipoleAmplitude_b, cosSquaredEta,
                sinSquaredEta, sinSquaredRho, sinFourRho, cosFourRho, sin2Rho);

        double dipoleSquared_ZZ = this._getDipoleSquared_ZZ(dipoleAmplitude_a, dipoleAmplitude_b, cosSquaredEta,
                sinSquaredEta);

        double dipoleSquared_XY = this._getDipoleSquared_XY(dipoleAmplitude_a, dipoleAmplitude_b, sin2Rho,
                sinSquaredEta);

        IPolarizationsIntensity intensity = null;

        try {
            double pol0Intensity = this._getPol0Intensity(dipoleSquared_XX, dipoleSquared_YY, dipoleSquared_ZZ,
                    dipoleSquared_XY);

            double pol45Intensity = this._getPol45Intensity(dipoleSquared_XX, dipoleSquared_YY, dipoleSquared_ZZ,
                    dipoleSquared_XY);

            double pol90Intensity = this._getPol90Intensity(dipoleSquared_XX, dipoleSquared_YY, dipoleSquared_ZZ,
                    dipoleSquared_XY);

            double pol135Intensity = this._getPol135Intensity(dipoleSquared_XX, dipoleSquared_YY, dipoleSquared_ZZ,
                    dipoleSquared_XY);

            intensity = new PolarizationsIntensity(pol0Intensity, pol45Intensity, pol90Intensity, pol135Intensity);        
        } catch (PropagationFactorNotFound e) {
            // This exception is never caught, because the constructor checks for it. 
        }

        return intensity;
    }

    private double _getPol0Intensity(
        double dipoleSquared_XX, double dipoleSquared_YY, double dipoleSquared_ZZ,
        double dipoleSquared_XY) throws PropagationFactorNotFound {
        return dipoleSquared_XX * _propFactor_xx_0 + dipoleSquared_YY * _propFactor_yy_0 +
            dipoleSquared_ZZ * _propFactor_zz_0 + dipoleSquared_XY * _propFactor_xy_0;
    }

    private double _getPol45Intensity(
        double dipoleSquared_XX, double dipoleSquared_YY, double dipoleSquared_ZZ,
        double dipoleSquared_XY) throws PropagationFactorNotFound {
        return dipoleSquared_XX * _propFactor_xx_45 + dipoleSquared_YY * _propFactor_yy_45
            + dipoleSquared_ZZ * _propFactor_zz_45 + dipoleSquared_XY * _propFactor_xy_45;
    }

    private double _getPol90Intensity(
        double dipoleSquared_XX, double dipoleSquared_YY, double dipoleSquared_ZZ,
        double dipoleSquared_XY) throws PropagationFactorNotFound {
        return dipoleSquared_XX * _propFactor_xx_90 + dipoleSquared_YY * _propFactor_yy_90 +
            dipoleSquared_ZZ * _propFactor_zz_90 + dipoleSquared_XY * _propFactor_xy_90;
    }

    private double _getPol135Intensity(
        double dipoleSquared_XX, double dipoleSquared_YY, double dipoleSquared_ZZ,
        double dipoleSquared_XY) throws PropagationFactorNotFound {
        return dipoleSquared_XX * _propFactor_xx_135 + dipoleSquared_YY * _propFactor_yy_135 +
            dipoleSquared_ZZ * _propFactor_zz_135 + dipoleSquared_XY * _propFactor_xy_135;
        }

    private double _getDipoleAmplitude_b(double cosHalfDelta) {
        return (cosHalfDelta * cosHalfDelta * cosHalfDelta - 1) / (3 * (cosHalfDelta - 1));
    }

    private double _getDipoleAmplitude_a(double cosHalfDelta) {
        return (1 - cosHalfDelta) * (2 + cosHalfDelta) / 6;
    }

    private double _getDipoleSquared_XX(double dipoleAmplitude_a, double dipoleAmplitude_b, double cosSquaredEta,
            double cosSquaredRho, double sinSquaredEta, double sinFourRho, double cosFourRho, double sin2Rho) {
        return dipoleAmplitude_b * ((2 * (cosSquaredEta + 1) * sin2Rho + sinFourRho + cosSquaredEta * cosFourRho)
                + dipoleAmplitude_a * sinSquaredEta * cosSquaredRho);
    }

    private double _getDipoleSquared_YY(double dipoleAmplitude_a, double dipoleAmplitude_b, double cosSquaredEta,
            double sinSquaredEta, double sinSquaredRho, double sinFourRho, double cosFourRho, double sin2Rho) {
        return dipoleAmplitude_b * ((2 * (cosSquaredEta + 1) * sin2Rho + cosFourRho + cosSquaredEta * sinFourRho)
                + dipoleAmplitude_a * sinSquaredEta * sinSquaredRho);
    }

    private double _getDipoleSquared_ZZ(double dipoleAmplitude_a, double dipoleAmplitude_b, double cosSquaredEta,
            double sinSquaredEta) {
        return dipoleAmplitude_b * sinSquaredEta + dipoleAmplitude_a * cosSquaredEta;
    }

    private double _getDipoleSquared_XY(double dipoleAmplitude_a, double dipoleAmplitude_b, double sin2Rho,
            double sinSquaredEta) {
        return 0.5 * sin2Rho * sinSquaredEta * (dipoleAmplitude_a - dipoleAmplitude_b);
    }
}