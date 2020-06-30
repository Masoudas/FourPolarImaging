package fr.fresnel.fourPolar.algorithm.fourPolar.converters;

import fr.fresnel.fourPolar.algorithm.exceptions.fourPolar.converters.ImpossibleOrientationVector;
import fr.fresnel.fourPolar.core.physics.dipole.DipoleSquaredComponent;
import fr.fresnel.fourPolar.core.physics.dipole.IOrientationVector;
import fr.fresnel.fourPolar.core.physics.polarization.IntensityVector;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.physics.propagation.IInverseOpticalPropagation;

/**
 * A concreter implementation of the {@link IIntensityToOrientationConverter}.
 * This implementation is based on the paper by Brasselet.
 */
public class IntensityToOrientationConverter implements IIntensityToOrientationConverter {
    /**
     * A terrible condition, set so that for (backgorund) noise, no orientation is
     * returend. Note that mathematically intensities can have any scale, hence they
     * can theoretically be less than this value, and this is why this condition is
     * terrible!
     */
    private final static double ERR_ZeroIntensity = 1e-8;

    /**
     * Checks that M_xx + M_yy + M_z > ERR_PositiveNormDipoleSquared, so that angles
     * can be computed in principle.
     */
    private final static double ERR_PositiveNormDipoleSquared = 1e-8;

    /**
     * This parameter accepts how close around 90 degree we apply modular
     * calculation for eta. Hence for eta < 90 - ERR_ModEta_90, we keep the original
     * angle, for eta > 90 + ERR_ModEta_90 we return 90 - eta and for the rest, we
     * return 90.
     */
    private final static double ERR_ModEta_90 = 1e-4;

    /**
     * This parameter indicates the acceptable error for lower range of
     * sumNormalizedDipoleSquared. In other words, for sumNormalizedDipoleSquared -
     * 1/3 < ERR_DeltaExists an exception is returned.
     */
    private final static double ERR_DeltaExists = 1e-14;

    /**
     * This parameter indicates the acceptable error for lower range of
     * sumNormalizedDipoleSquared. In other words, for normalizedDipoleSquared_Z -
     * sumNormalizedDipoleSquared < ERR_EtaExists an exception is returned.
     */
    private final static double ERR_EtaExists = 1e-14;

    /**
     * Acceptable distance of eta from zero. If this distance is violated, an
     * exception is returned.
     */
    private final static double ETA_DisTo0 = Math.PI / 180 * 0.1;

    /**
     * The distance from PI, before we consider delta angle to be pi.
     */
    private final static double ERR_DeltaIsPi = Math.PI / 180 * 0.05;

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
    private double _getRho(double normalizedDipoleSquared_XY, double normalizedDipoleSquared_XYdiff) {
        double raw_Rho = 0.5 * Math.atan2(normalizedDipoleSquared_XY, normalizedDipoleSquared_XYdiff);
        return ((raw_Rho + IOrientationVector.MAX_Rho) % IOrientationVector.MAX_Rho);
    }

    /**
     * Calculate delta using the formula. Because the result is always positive (as
     * ensured by conditions), we return the raw value.
     */
    private double _getDelta(double sumNormalizedDipoleSquared) {
        double raw_delta = 2 * Math.acos((Math.sqrt(12 * sumNormalizedDipoleSquared - 3) - 1) / 2);

        return raw_delta;
    }

    /**
     * Calculate eta using the formula (note that angle is always positive). If eta
     * < PI/2, return the original value. Otherwise, because angle greater than PI/2
     * has same propagation as it's PI complement, return the complement value.
     */
    private double _getEta(double normalizedDipoleSquared_Z, double sumNormalizedDipoleSquared) {
        double raw_eta = Math.asin(Math.sqrt(
                2 * (sumNormalizedDipoleSquared - normalizedDipoleSquared_Z) / (3 * sumNormalizedDipoleSquared - 1)));

        double eta = 0;
        if (raw_eta < IOrientationVector.MAX_Eta - ERR_ModEta_90) {
            eta = raw_eta;
        } else if (raw_eta > IOrientationVector.MAX_Eta + ERR_ModEta_90) {
            eta = raw_eta - IOrientationVector.MAX_Eta;
        } else {
            eta = IOrientationVector.MAX_Eta;
        }

        return eta;
    }

    @Override
    public void convert(IntensityVector intensityVector, IOrientationVector orientationVector)
            throws ImpossibleOrientationVector {
        // TODO : get rid of the exception and just return Nan for impossible situations
        // Getting intensities.
        double pol0Intensity = intensityVector.getIntensity(Polarization.pol0);
        double pol45Intensity = intensityVector.getIntensity(Polarization.pol45);
        double pol90Intensity = intensityVector.getIntensity(Polarization.pol90);
        double pol135Intensity = intensityVector.getIntensity(Polarization.pol135);

        /**
         * If one of the intensities is zero, then the dipole amplitude lambda_3 is
         * infinite, which is mathematically impossible. Also if all intensities are
         * zero, then no orientation can be defined. We catch both cases simultaneously
         * here.
         */
        if (pol0Intensity - 0 < ERR_ZeroIntensity || pol45Intensity - 0 < ERR_ZeroIntensity
                || pol90Intensity - 0 < ERR_ZeroIntensity || pol135Intensity - 0 < ERR_ZeroIntensity) {
            throw new ImpossibleOrientationVector(
                    "Can't compute the orientation vector because intensities can't be zero.");
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

        if (dipoleSquared_XX + dipoleSquared_YY + dipoleSquared_ZZ < ERR_PositiveNormDipoleSquared) {
            throw new ImpossibleOrientationVector(
                    "Can't compute the orientation vector because norm of dipole squareds must be positive.");
        }

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
        double delta = this._getDelta(sumNormalizedDipoleSquared);
        double eta = Double.NaN;
        double rho = Double.NaN;

        boolean deltaIsPI = Math.PI - delta < ERR_DeltaIsPi;
        if (deltaIsPI) {
            orientationVector.setAngles(rho, delta, eta);
        } else {
            eta = this._getEta(normalizedDipoleSquared_Z, sumNormalizedDipoleSquared);
            boolean etaIs0 = eta - 0 < ETA_DisTo0;
            if (etaIs0) {
                orientationVector.setAngles(rho, delta, eta);
            } else {
                rho = this._getRho(normalizedDipoleSquared_XY, normalizedDipoleSquared_XYdiff);
                orientationVector.setAngles(rho, delta, eta);
            }
        }

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
    private void _checkDeltaExistsAndPositive(double sumNormalizedDipoleSquared) throws ImpossibleOrientationVector {
        if (sumNormalizedDipoleSquared - 1 / 3 < ERR_DeltaExists) {
            throw new ImpossibleOrientationVector(
                    "Can't compute the orientation vector because delta can't be computed.");
        }
    }

    /**
     * Check eta angle exists by checking normalizedDipoleSquared_Z <=
     * sumNormalizedDipoleSquared and normalizedDipoleSquared_Z >= 0.5(1 -
     * sumNormalizedDipoleSquared)
     */
    private void _checkEtaExists(double normalizedDipoleSquared_Z, double sumNormalizedDipoleSquared)
            throws ImpossibleOrientationVector {
        if (normalizedDipoleSquared_Z - 0.5 * (1 - sumNormalizedDipoleSquared) < ERR_EtaExists) {
            throw new ImpossibleOrientationVector(
                    "Can't compute the orientation vector because eta can't be computed.");
        }

    }
}