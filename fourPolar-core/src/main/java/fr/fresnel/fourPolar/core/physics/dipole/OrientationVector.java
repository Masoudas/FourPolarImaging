package fr.fresnel.fourPolar.core.physics.dipole;

import fr.fresnel.fourPolar.core.exceptions.physics.dipole.OrientationAngleOutOfRange;

/**
 * Models the orientation angles calculated using the four polar method.
 */
public class OrientationVector implements IOrientationVector {
    final private float _rho;
    final private float _delta;
    final private float _eta;

    /**
     * Models the orientation angles calculated using the four polar method.
     * Note that NaN is acceptable for each angle.
     * 
     * @param rho   : rho angle in radian, ranges from 0 to pi.
     * @param delta : delta angle in radian, ranges from 0 to pi.
     * @param eta   : eta angle in radian, ranges from 0 to pi/2.
     */
    public OrientationVector(float rho, float delta, float eta) throws OrientationAngleOutOfRange {
        _checkRho(rho);
        _rho = rho;

        _checkDelta(delta);
        _delta = delta;

        _checkEta(eta);
        _eta = eta;
    }

    @Override
    public float getAngle(OrientationAngle angle) {
        float orientation = 0;
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
    public float getAngleInDegree(OrientationAngle angle) {
        return (float) Math.toDegrees(this.getAngle(angle));
    }

    private void _checkRho(float value) throws OrientationAngleOutOfRange {
        if (!Float.isNaN(value) && (value < 0 || value > (float)Math.PI)) {
            throw new OrientationAngleOutOfRange("Rho is out of [0, pi] range");
        }
    }

    private void _checkDelta(float value) throws OrientationAngleOutOfRange {
        if (!Float.isNaN(value) && (value < 0 || value > (float)Math.PI)) {
            throw new OrientationAngleOutOfRange("Delta is out of [0, pi] range");
        }
    }

    private void _checkEta(float value) throws OrientationAngleOutOfRange {
        if (!Float.isNaN(value) && (value < 0 || value > (float)(Math.PI / 2))) {
            throw new OrientationAngleOutOfRange("Eta is out of [0, pi/2] range");
        }
    }

}