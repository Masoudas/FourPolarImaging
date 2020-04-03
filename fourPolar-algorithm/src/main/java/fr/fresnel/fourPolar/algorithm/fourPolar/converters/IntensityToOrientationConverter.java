package fr.fresnel.fourPolar.algorithm.fourPolar.converters;

import fr.fresnel.fourPolar.algorithm.exceptions.fourPolar.converters.ImpossibleOrientationVector;
import fr.fresnel.fourPolar.core.physics.dipole.DipoleSquaredComponent;
import fr.fresnel.fourPolar.core.physics.dipole.IOrientationVector;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationVector;
import fr.fresnel.fourPolar.core.physics.polarization.IntensityVector;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.physics.propagation.IInverseOpticalPropagation;

/**
 * A concreter implementation of the {@link IIntensityToOrientationConverter}.
 * This implementation is based on the paper by Brasselet.
 */
public class IntensityToOrientationConverter implements IIntensityToOrientationConverter {
    final private double _iProp_0_xx;
    final private double _iProp_0_yy;
    final private double _iProp_0_zz;
    final private double _iProp_0_xy;

    final private double _iProp_90_xx;
    final private double _iProp_90_yy;
    final private double _iProp_90_zz;
    final private double _iProp_90_xy;

    final private double _iProp_45_xx;
    final private double _iProp_45_yy;
    final private double _iProp_45_zz;
    final private double _iProp_45_xy;

    final private double _iProp_135_xx;
    final private double _iProp_135_yy;
    final private double _iProp_135_zz;
    final private double _iProp_135_xy;

    /**
     * This implementation uses an instance of the
     * {@link IInverseOpticalPropagation} to convert an polarization intensity to a
     * intensity of individual polarizations.
     * 
     * @param inverseOpticalProp is the inverse optical propagation factors.
     */
    public IntensityToOrientationConverter(IInverseOpticalPropagation inverseOpticalProp) {
        _iProp_0_xx = inverseOpticalProp.getInverseFactor(Polarization.pol0, DipoleSquaredComponent.XX);
        _iProp_0_yy = inverseOpticalProp.getInverseFactor(Polarization.pol0, DipoleSquaredComponent.YY);
        _iProp_0_zz = inverseOpticalProp.getInverseFactor(Polarization.pol0, DipoleSquaredComponent.ZZ);
        _iProp_0_xy = inverseOpticalProp.getInverseFactor(Polarization.pol0, DipoleSquaredComponent.XY);

        _iProp_90_xx = inverseOpticalProp.getInverseFactor(Polarization.pol90, DipoleSquaredComponent.XX);
        _iProp_90_yy = inverseOpticalProp.getInverseFactor(Polarization.pol90, DipoleSquaredComponent.YY);
        _iProp_90_zz = inverseOpticalProp.getInverseFactor(Polarization.pol90, DipoleSquaredComponent.ZZ);
        _iProp_90_xy = inverseOpticalProp.getInverseFactor(Polarization.pol90, DipoleSquaredComponent.XY);

        _iProp_45_xx = inverseOpticalProp.getInverseFactor(Polarization.pol45, DipoleSquaredComponent.XX);
        _iProp_45_yy = inverseOpticalProp.getInverseFactor(Polarization.pol45, DipoleSquaredComponent.YY);
        _iProp_45_zz = inverseOpticalProp.getInverseFactor(Polarization.pol45, DipoleSquaredComponent.ZZ);
        _iProp_45_xy = inverseOpticalProp.getInverseFactor(Polarization.pol45, DipoleSquaredComponent.XY);

        _iProp_135_xx = inverseOpticalProp.getInverseFactor(Polarization.pol135, DipoleSquaredComponent.XX);
        _iProp_135_yy = inverseOpticalProp.getInverseFactor(Polarization.pol135, DipoleSquaredComponent.YY);
        _iProp_135_zz = inverseOpticalProp.getInverseFactor(Polarization.pol135, DipoleSquaredComponent.ZZ);
        _iProp_135_xy = inverseOpticalProp.getInverseFactor(Polarization.pol135, DipoleSquaredComponent.XY);
    }

    /**
     * Calculate rho using the formula. If result is positive, return original
     * value. Otherwise, add a pi to it, to get the equivalent positive angle and
     * then return it.
     */
    private float _getRho(double normalizedDipoleSquared_XY, double normalizedDipoleSquared_XYdiff) {
        double raw_Rho = 0.5 * Math.atan2(normalizedDipoleSquared_XY, normalizedDipoleSquared_XYdiff);
        return (float)((raw_Rho + OrientationVector.MAX_Rho) % OrientationVector.MAX_Rho);
    }

    /**
     * Calculate delta using the formula. Because the result is always positive (as ensured 
     * by conditions), we return the raw value.
     */
    private float _getDelta(double sumNormalizedDipoleSquared) {
        double raw_delta = 2 * Math.acos((Math.sqrt(12 * sumNormalizedDipoleSquared - 3) - 1) / 2);

        return (float)raw_delta;
    }

    /**
     * Calculate eta using the formula (note that angle is always positive). If
     * eta < PI/2, return the original value. Otherwise, because angle greater
     * than PI/2 has same propagation as it's PI complement, return the complement
     * value.
     */
    private float _getEta(double normalizedDipoleSquared_Z, double sumNormalizedDipoleSquared) {
        double raw_eta = Math.asin(Math.sqrt(
                2 * (sumNormalizedDipoleSquared - normalizedDipoleSquared_Z) / (3 * sumNormalizedDipoleSquared - 1)));

        if (raw_eta < OrientationVector.MAX_Eta) {
            return (float) raw_eta;
        } else {
            return (float) (raw_eta - OrientationVector.MAX_Eta);
        }
    }

    @Override
    public IOrientationVector convert(IntensityVector intensity) throws ImpossibleOrientationVector {
        // Getting intensities.
        double pol0Intensity = intensity.getIntensity(Polarization.pol0);
        double pol45Intensity = intensity.getIntensity(Polarization.pol45);
        double pol90Intensity = intensity.getIntensity(Polarization.pol90);
        double pol135Intensity = intensity.getIntensity(Polarization.pol135);

        // ‌If one of the intensities is zero, then the dipole amplitude lambda_3 is
        // infinity, which is mathematically
        // impossible. Also if all intensities are zero, then no orientation can be
        // defined. We cath
        // both cases simultaneously here.
        if (pol0Intensity == 0 || pol45Intensity == 0 || pol90Intensity == 0 || pol135Intensity == 0) {
            throw new ImpossibleOrientationVector("All intensities cannot be zero simultaneously.");
        }

        // Computing dipole squared.
        double dipoleSquared_XX = this._computeDipoleSquared_XX(pol0Intensity, pol45Intensity, pol90Intensity,
                pol135Intensity);
        double dipoleSquared_YY = this._computeDipoleSquared_YY(pol0Intensity, pol45Intensity, pol90Intensity,
                pol135Intensity);
        double dipoleSquared_ZZ = this._computeDipoleSquared_ZZ(pol0Intensity, pol45Intensity, pol90Intensity,
                pol135Intensity);
        double dipoleSquared_XY = this._computeDipoleSquared_XY(pol0Intensity, pol45Intensity, pol90Intensity,
                pol135Intensity);

        // Computing normalized dipole squared.
        double normalizedDipoleSquared_XYdiff = this._normalizedDipoleSquared_XYdiff(dipoleSquared_XX, dipoleSquared_YY,
                dipoleSquared_ZZ);

        double normalizedDipoleSquared_XY = this._normalizedDipoleSquared_XY(dipoleSquared_XX, dipoleSquared_YY,
                dipoleSquared_ZZ, dipoleSquared_XY);

        double normalizedDipoleSquared_Z = this._normalizedDipoleSquared_Z(dipoleSquared_XX, dipoleSquared_YY,
                dipoleSquared_ZZ);

        double sumNormalizedDipoleSquared = this._sumNormalizedDipoleSquared(normalizedDipoleSquared_XY,
                normalizedDipoleSquared_XYdiff, normalizedDipoleSquared_Z);

        // Check necessary conditions for angles to exist.
        _checkDeltaExistsAndPositive(sumNormalizedDipoleSquared);
        _checkEtaExists(normalizedDipoleSquared_Z, sumNormalizedDipoleSquared);

        // Computing the angles
        float rho = this._getRho(normalizedDipoleSquared_XY, normalizedDipoleSquared_XYdiff);
        float delta = this._getDelta(sumNormalizedDipoleSquared);
        float eta = this._getEta(normalizedDipoleSquared_Z, sumNormalizedDipoleSquared);

        return new OrientationVector(rho, delta, eta);
    }

    private double _computeDipoleSquared_XX(double pol0Intensity, double pol45Intensity, double pol90Intensity,
            double pol135Intensity) {
        return pol0Intensity * _iProp_0_xx + pol45Intensity * _iProp_45_xx + pol90Intensity * _iProp_90_xx
                + pol135Intensity * _iProp_135_xx;
    }

    private double _computeDipoleSquared_YY(double pol0Intensity, double pol45Intensity, double pol90Intensity,
            double pol135Intensity) {
        return pol0Intensity * _iProp_0_yy + pol45Intensity * _iProp_45_yy + pol90Intensity * _iProp_90_yy
                + pol135Intensity * _iProp_135_yy;
    }

    private double _computeDipoleSquared_ZZ(double pol0Intensity, double pol45Intensity, double pol90Intensity,
            double pol135Intensity) {
        return pol0Intensity * _iProp_0_zz + pol45Intensity * _iProp_45_zz + pol90Intensity * _iProp_90_zz
                + pol135Intensity * _iProp_135_zz;
    }

    private double _computeDipoleSquared_XY(double pol0Intensity, double pol45Intensity, double pol90Intensity,
            double pol135Intensity) {
        return pol0Intensity * _iProp_0_xy + pol45Intensity * _iProp_45_xy + pol90Intensity * _iProp_90_xy
                + pol135Intensity * _iProp_135_xy;
    }

    private double _normalizedDipoleSquared_XYdiff(double dipoleSquared_XX, double dipoleSquared_YY,
            double dipoleSquared_ZZ) {
        return (dipoleSquared_XX - dipoleSquared_YY) / (dipoleSquared_XX + dipoleSquared_YY + dipoleSquared_ZZ);
    }

    private double _normalizedDipoleSquared_XY(double dipoleSquared_XX, double dipoleSquared_YY,
            double dipoleSquared_ZZ, double dipoleSquared_XY) {
        return 2 * dipoleSquared_XY / (dipoleSquared_XX + dipoleSquared_YY + dipoleSquared_ZZ);
    }

    private double _normalizedDipoleSquared_Z(double dipoleSquared_XX, double dipoleSquared_YY,
            double dipoleSquared_ZZ) {
        return dipoleSquared_ZZ / (dipoleSquared_XX + dipoleSquared_YY + dipoleSquared_ZZ);
    }

    private double _sumNormalizedDipoleSquared(double normalizedDipoleSquared_XY, double normalizedDipoleSquared_XYdiff,
            double normalizedDipoleSquared_Z) throws ImpossibleOrientationVector {
        return normalizedDipoleSquared_Z + Math.sqrt(normalizedDipoleSquared_XYdiff * normalizedDipoleSquared_XYdiff
                + normalizedDipoleSquared_XY * normalizedDipoleSquared_XY);
    }

    /**
     * Check that delta exists and positive is in the range 0.5 and 1.
     */
    private void _checkDeltaExistsAndPositive(double sumNormalizedDipoleSquared)
            throws ImpossibleOrientationVector {
        if (sumNormalizedDipoleSquared > 1 || sumNormalizedDipoleSquared < 1 / 2) {
            throw new ImpossibleOrientationVector("Sum of normalized dipole squared must be in range [1/2, 1].");
        }
    }

    /**
     * Check eta angle exists by checking normalizedDipoleSquared_Z <= sumNormalizedDipoleSquared and
     * normalizedDipoleSquared_Z >= 0.5(1 - sumNormalizedDipoleSquared)
     */
    private void _checkEtaExists(double normalizedDipoleSquared_Z, double sumNormalizedDipoleSquared)
            throws ImpossibleOrientationVector {
        if (normalizedDipoleSquared_Z > sumNormalizedDipoleSquared
                || normalizedDipoleSquared_Z < 0.5 * (1 - sumNormalizedDipoleSquared)) {
            throw new ImpossibleOrientationVector("Pz is not in the accepted range.");
        }

    }
}