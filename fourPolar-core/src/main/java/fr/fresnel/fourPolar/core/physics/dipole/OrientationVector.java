package fr.fresnel.fourPolar.core.physics.dipole;

import fr.fresnel.fourPolar.core.exceptions.physics.dipole.OrientationAngleOutOfRange;

/**
 * Models the orientation angles calculated using the four polar method.
 */
public class OrientationVector implements IOrientationVector {
    final private double _rho;
    final private double _delta;
    final private double _eta;

    /**
     * Maximum possible value for the rho
     */
    public final static double MAX_Rho = Math.PI;

    /**
     * Maximum possible value for the delta
     */
    public final static double MAX_Delta = Math.PI;

    /**
     * Maximum possible value for the delta
     */
    public final static double MAX_Eta = Math.PI / 2;

    /**
     * Models the orientation angles calculated using the four polar method. Note
     * that NaN is acceptable for each angle, and if NaN is given for one angle,
     * all angles are set to NaN.
     * 
     * @param rho   : rho angle in radian, ranges from 0 to pi.
     * @param delta : delta angle in radian, ranges from 0 to pi.
     * @param eta   : eta angle in radian, ranges from 0 to pi/2.
     */
    public OrientationVector(double rho, double delta, double eta) throws OrientationAngleOutOfRange {
        if (Double.isNaN(rho) || Double.isNaN(delta) || Double.isNaN(eta)) {
            _rho = Double.NaN;
            _delta = Double.NaN;
            _eta = Double.NaN;
        } else {
            _checkRho(rho);
            _rho = rho;

            _checkDelta(delta);
            _delta = delta;

            _checkEta(eta);
            _eta = eta;
        }
 
    }

    @Override
    public double getAngle(OrientationAngle angle) {
        double orientation = 0;
        switch (angle) {
            case rho:
                orientation = _rho;
                break;

            case delta:
                orientation = _delta;
                break;

            case eta:
                orientation = _eta;
                break;

            default:
                break;
        }

        return orientation;
    }

    @Override
    public double getAngleInDegree(OrientationAngle angle) {
        return Math.toDegrees(this.getAngle(angle));
    }

    private void _checkRho(double value) throws OrientationAngleOutOfRange {
        if (value < 0 || value >= MAX_Rho) {
            throw new OrientationAngleOutOfRange("Rho is out of [0, pi) range");
        }
    }

    private void _checkDelta(double value) throws OrientationAngleOutOfRange {
        if (value < 0 || value > MAX_Delta) {
            throw new OrientationAngleOutOfRange("Delta is out of [0, pi] range");
        }
    }

    private void _checkEta(double value) throws OrientationAngleOutOfRange {
        if (value < 0 || value >= MAX_Eta) {
            throw new OrientationAngleOutOfRange("Eta is out of [0, pi/2) range");
        }
    }

}