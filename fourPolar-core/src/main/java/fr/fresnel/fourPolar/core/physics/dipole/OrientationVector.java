package fr.fresnel.fourPolar.core.physics.dipole;

import java.util.Hashtable;

import fr.fresnel.fourPolar.core.exceptions.physics.dipole.OrientationAngleOutOfRange;

/**
 * Models the orientation angles calculated using the four polar method.
 */
public class OrientationVector implements IOrientationVector {
    private Hashtable<OrientationAngle, Float> _angle = new Hashtable<OrientationAngle, Float>(3);

    /**
     * Models the orientation angles calculated using the four polar method.
     * 
     * @param rho   : rho angle in radian.
     * @param delta : delta angle in radian.
     * @param eta   : eta angle in radian.
     */
    public OrientationVector(float rho, float delta, float eta) throws OrientationAngleOutOfRange {
        _checkRho(rho);
        this._angle.put(OrientationAngle.rho, rho);

        _checkDelta(delta);
        this._angle.put(OrientationAngle.delta, delta);

        _checkEta(eta);
        this._angle.put(OrientationAngle.eta, eta);
    }

    @Override
    public float getAngle(OrientationAngle angle) {
        return this._angle.get(angle);
    }

    @Override
    public float getAngleInDegree(OrientationAngle angle) {
        return (float) Math.toDegrees(this.getAngle(angle));
    }

    private void _checkRho(float value) throws OrientationAngleOutOfRange {
        if (value < 0 || value > (float)Math.PI) {
            throw new OrientationAngleOutOfRange("Rho is out of range");
        }
    }

    private void _checkDelta(float value) throws OrientationAngleOutOfRange {
        if (value < 0 || value > (float)Math.PI) {
            throw new OrientationAngleOutOfRange("Delta is out of range");
        }
    }

    private void _checkEta(float value) throws OrientationAngleOutOfRange {
        if (value < 0 || value > (float)(Math.PI / 2)) {
            throw new OrientationAngleOutOfRange("Eta is out of range");
        }
    }

}