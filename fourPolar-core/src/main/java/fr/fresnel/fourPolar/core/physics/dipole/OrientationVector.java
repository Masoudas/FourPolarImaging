package fr.fresnel.fourPolar.core.physics.dipole;

import fr.fresnel.fourPolar.core.exceptions.physics.dipole.OrientationAngleOutOfRange;

/**
 * Models the orientation angles calculated using the four polar method.
 */
public class OrientationVector implements IOrientationVector {
    private double _rho;
    private double _delta;
    private double _eta;

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
     * The maximum allowed devation from the given angle ranges. Hence:
     * rho in [0 - ERR_Angle, MAX_Rho + ERR_Angle]
     * delta in [0 - ERR_Angle, MAX_delta + ERR_Angle]
     * eta in [0 - ERR_Angle, MAX_eta + ERR_Angle]
     */
    final private static double ERR_Angle = Math.PI / 180 * 0.0001;

    /**
     * Models the orientation angles calculated using the four polar method. 
     * 
     * @param rho   : rho angle in radian, ranges from 0 to pi. NaN is acceptable.
     * @param delta : delta angle in radian, ranges from 0 to pi. NaN is acceptable.
     * @param eta   : eta angle in radian, ranges from 0 to pi/2. NaN is acceptable.
     */
    public OrientationVector(double rho, double delta, double eta) throws OrientationAngleOutOfRange {
        this.setAngles(rho, delta, eta);
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

    @Override
    public void setAngles(double rho, double delta, double eta) throws OrientationAngleOutOfRange {
        _checkRho(rho);
        _rho = rho;

        _checkDelta(delta);
        _delta = delta;

        _checkEta(eta);
        _eta = eta; 
    }

    private void _checkRho(double value) throws OrientationAngleOutOfRange {
        if (value < -ERR_Angle || value - MAX_Rho > ERR_Angle) {
            throw new OrientationAngleOutOfRange("Rho is out of [0, pi] range");
        }
    }

    private void _checkDelta(double value) throws OrientationAngleOutOfRange {
        if (value < -ERR_Angle || value - MAX_Delta > ERR_Angle) {
            throw new OrientationAngleOutOfRange("Delta is out of [0, pi] range");
        }
    }

    private void _checkEta(double value) throws OrientationAngleOutOfRange {
        if (value < -ERR_Angle || value - MAX_Eta > ERR_Angle) {
            throw new OrientationAngleOutOfRange("Eta is out of [0, pi/2) range");
        }
    }

}