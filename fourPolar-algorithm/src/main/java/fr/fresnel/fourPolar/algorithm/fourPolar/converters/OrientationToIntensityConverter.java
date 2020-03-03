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

    /**
     * This implementation uses an instance of the {@link IOpticalPropagation} to
     * convert an orientation vector to a polarizations intensity.
     * 
     * @param inverseProp contains the inverse optical propagation factors.
     */
    public OrientationToIntensityConverter(IOpticalPropagation opticalPropagation) throws PropagationFactorNotFound {
        this._opticalPropagation = opticalPropagation;

        // Run dummy methods to ensure that all propagation factors exist.
        this._getPol0Intensity(0, 0, 0, 0);
        this._getPol45Intensity(0, 0, 0, 0);
        this._getPol90Intensity(0, 0, 0, 0);
        this._getPol135Intensity(0, 0, 0, 0);
    }

    @Override
    public IPolarizationsIntensity convert(IOrientationVector orientationVector) {
        double cosHalfDelta = Math.cos(orientationVector.getAngle(OrientationAngle.delta) / 2);

        double cosEta = Math.cos(orientationVector.getAngle(OrientationAngle.eta));
        double cosSquaredEta = cosEta * cosEta;

        double sinEta = Math.sin(orientationVector.getAngle(OrientationAngle.eta));
        double sinSquaredEta = sinEta * sinEta;

        double sinRho = Math.sin(orientationVector.getAngle(OrientationAngle.eta));
        double sin2Rho = Math.sin(2 * orientationVector.getAngle(OrientationAngle.eta));
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

    private double _getPol0Intensity(double dipoleSquared_XX, double dipoleSquared_YY, double dipoleSquared_ZZ,
            double dipoleSquared_XY) throws PropagationFactorNotFound {
        double pol0_xx = dipoleSquared_XX
                * this._opticalPropagation.getPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol0);

        double pol0_yy = dipoleSquared_YY
                * this._opticalPropagation.getPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol0);

        double pol0_zz = dipoleSquared_ZZ
                * this._opticalPropagation.getPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol0);

        double pol0_xy = dipoleSquared_XY
                * this._opticalPropagation.getPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol0);

        return pol0_xx + pol0_yy + pol0_zz + pol0_xy;
    }

    private double _getPol45Intensity(double dipoleSquared_XX, double dipoleSquared_YY, double dipoleSquared_ZZ,
            double dipoleSquared_XY) throws PropagationFactorNotFound {
        double pol45_xx = dipoleSquared_XX
                * this._opticalPropagation.getPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol45);

        double pol45_yy = dipoleSquared_YY
                * this._opticalPropagation.getPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol45);

        double pol45_zz = dipoleSquared_ZZ
                * this._opticalPropagation.getPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol45);

        double pol45_xy = dipoleSquared_XY
                * this._opticalPropagation.getPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol45);

        return pol45_xx + pol45_yy + pol45_zz + pol45_xy;
    }

    private double _getPol90Intensity(double dipoleSquared_XX, double dipoleSquared_YY, double dipoleSquared_ZZ,
            double dipoleSquared_XY) throws PropagationFactorNotFound {
        double pol90_xx = dipoleSquared_XX
                * this._opticalPropagation.getPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol90);

        double pol90_yy = dipoleSquared_YY
                * this._opticalPropagation.getPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol90);

        double pol90_zz = dipoleSquared_ZZ
                * this._opticalPropagation.getPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol90);

        double pol90_xy = dipoleSquared_XY
                * this._opticalPropagation.getPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol90);

        return pol90_xx + pol90_yy + pol90_zz + pol90_xy;
    }

    private double _getPol135Intensity(double dipoleSquared_XX, double dipoleSquared_YY, double dipoleSquared_ZZ,
            double dipoleSquared_XY) throws PropagationFactorNotFound {
        double pol135_xx = dipoleSquared_XX
                * this._opticalPropagation.getPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol135);

        double pol135_yy = dipoleSquared_YY
                * this._opticalPropagation.getPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol135);

        double pol135_zz = dipoleSquared_ZZ
                * this._opticalPropagation.getPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol135);

        double pol135_xy = dipoleSquared_XY
                * this._opticalPropagation.getPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol135);

        return pol135_xx + pol135_yy + pol135_zz + pol135_xy;
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