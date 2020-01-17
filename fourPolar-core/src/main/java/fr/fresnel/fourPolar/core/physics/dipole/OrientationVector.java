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
     * @param rho : rho angle in radian.
     * @param delta : delta angle in radian.
     * @param eta : eta angle in radian.
     */
    public OrientationVector(float rho, float delta, float eta) throws OrientationAngleOutOfRange{
        _checkAngle(OrientationAngle.rho, rho);
        this._angle.put(OrientationAngle.rho, rho);
        
        _checkAngle(OrientationAngle.delta, delta);
        this._angle.put(OrientationAngle.delta, delta);

        _checkAngle(OrientationAngle.eta, eta);
        this._angle.put(OrientationAngle.eta, eta);
    }

    @Override
    public float getAngle(OrientationAngle angle) {
        return this._angle.get(angle);
    }
    
    @Override
    public float getAngleInDegree(OrientationAngle angle){
        return (float)Math.toDegrees(this.getAngle(angle));
    }

    private void _checkAngle(OrientationAngle angle, float value) throws OrientationAngleOutOfRange{
        if (value < 0 || value > Math.PI){
            throw new OrientationAngleOutOfRange(angle.toString() + "is out of range");
        }
    }

    
}