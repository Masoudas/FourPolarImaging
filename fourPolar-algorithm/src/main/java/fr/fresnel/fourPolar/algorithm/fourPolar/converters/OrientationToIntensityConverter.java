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
 * This implementation is based on the paper by Brasselet.
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
        // Angle sines and cosines.
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

        // Calculate dipole amplitude aling each axis.
        double dipoleAmplitude_axis1 = this._getDipoleAmplitude_axes1(cosHalfDelta);
        double dipoleAmplitude_axis3 = this._getDipoleAmplitude_axes3(cosHalfDelta);

        // Calculate dipole squared.
        double dipoleSquared_XX = this._getDipoleSquared_XX(dipoleAmplitude_axis1, dipoleAmplitude_axis3, cosSquaredEta,
                cosSquaredRho, sinSquaredEta, sinFourRho, cosFourRho, sin2Rho);

        double dipoleSquared_YY = this._getDipoleSquared_YY(dipoleAmplitude_axis1, dipoleAmplitude_axis3, cosSquaredEta,
                sinSquaredEta, sinSquaredRho, sinFourRho, cosFourRho, sin2Rho);

        double dipoleSquared_ZZ = this._getDipoleSquared_ZZ(dipoleAmplitude_axis1, dipoleAmplitude_axis3, cosSquaredEta,
                sinSquaredEta);

        double dipoleSquared_XY = this._getDipoleSquared_XY(dipoleAmplitude_axis1, dipoleAmplitude_axis3, sin2Rho,
                sinSquaredEta);

        // Calculate polarization intensity.        
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

    /**
     * Returns the dipole amplitude along axises 1 & 3.
     */
    private double _getDipoleAmplitude_axes3(double cosHalfDelta) {
        return (cosHalfDelta * cosHalfDelta + cosHalfDelta + 1) / 3;
    }

    /**
     * Returns the dipole amplitude along axis 2.
     */
    private double _getDipoleAmplitude_axes1(double cosHalfDelta) {
        return (1 - cosHalfDelta) * (2 + cosHalfDelta) / 6;
    }

    private double _getDipoleSquared_XX(
        double dipoleAmplitude_axis1, double dipoleAmplitude_axis3, double cosSquaredEta,
        double cosSquaredRho, double sinSquaredEta, double sinFourRho, double cosFourRho, double sin2Rho) {
        return dipoleAmplitude_axis1 * (0.25 * (cosSquaredEta + 1) * sin2Rho * sin2Rho + 
            sinFourRho + cosSquaredEta * cosFourRho) + dipoleAmplitude_axis3 * sinSquaredEta * cosSquaredRho;
    }

    private double _getDipoleSquared_YY(
        double dipoleAmplitude_axis1, double dipoleAmplitude_axis3, double cosSquaredEta,
        double sinSquaredEta, double sinSquaredRho, double sinFourRho, double cosFourRho, double sin2Rho) {
        return dipoleAmplitude_axis1 * (0.25 * (cosSquaredEta + 1) * sin2Rho * sin2Rho + 
            cosFourRho + cosSquaredEta * sinFourRho) + dipoleAmplitude_axis3 * sinSquaredEta * sinSquaredRho;
    }

    private double _getDipoleSquared_ZZ(
        double dipoleAmplitude_axis1, double dipoleAmplitude_axis3, double cosSquaredEta,
        double sinSquaredEta) {
        return dipoleAmplitude_axis1 * sinSquaredEta + dipoleAmplitude_axis3 * cosSquaredEta;
    }

    private double _getDipoleSquared_XY(
        double dipoleAmplitude_axis1, double dipoleAmplitude_axis3, double sin2Rho,
        double sinSquaredEta) {
        return 0.5 * sin2Rho * sinSquaredEta * (dipoleAmplitude_axis3 - dipoleAmplitude_axis1);
    }
}