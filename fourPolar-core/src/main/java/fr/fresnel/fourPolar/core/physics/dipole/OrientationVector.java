package fr.fresnel.fourPolar.core.physics.dipole;

import fr.fresnel.fourPolar.core.exceptions.physics.dipole.OrientationAngleOutOfRange;

/**
 * Models the orientation angles calculated using the four polar method.
 */
public class OrientationVector implements IOrientationVector {
    private double _rho;
    private double _delta;
    private double _eta;
    private boolean _isWellDefind = false;

    public static double maxAngle(OrientationAngle angle) {
        double max = 0;
        switch (angle) {
            case rho:
                max = IOrientationVector.MAX_Rho;
                break;

            case delta:
                max = IOrientationVector.MAX_Delta;
                break;


            case eta:
                max = IOrientationVector.MAX_Eta;
                break;

            default:
                break;
        }

        return max;
    }

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

        _setIsWellDefined();
    }

    private void _checkRho(double value) throws OrientationAngleOutOfRange {
        if (value < -IOrientationVector.ERR_Angle || value - IOrientationVector.MAX_Rho > IOrientationVector.ERR_Angle) {
            throw new OrientationAngleOutOfRange("Rho is out of [0, pi] range");
        }
    }

    private void _checkDelta(double value) throws OrientationAngleOutOfRange {
        if (value < -IOrientationVector.ERR_Angle || value - IOrientationVector.MAX_Delta > IOrientationVector.ERR_Angle) {
            throw new OrientationAngleOutOfRange("Delta is out of [0, pi] range");
        }
    }

    private void _checkEta(double value) throws OrientationAngleOutOfRange {
        if (value < -IOrientationVector.ERR_Angle || value - IOrientationVector.MAX_Eta > IOrientationVector.ERR_Angle) {
            throw new OrientationAngleOutOfRange("Eta is out of [0, pi/2) range");
        }
    }

    @Override
    public boolean isWellDefined() {
        return this._isWellDefind;
    }

    private void _setIsWellDefined() {
        this._isWellDefind = !Double.isNaN(this._delta) && !Double.isNaN(this._eta) && !Double.isNaN(this._rho);
    }

}