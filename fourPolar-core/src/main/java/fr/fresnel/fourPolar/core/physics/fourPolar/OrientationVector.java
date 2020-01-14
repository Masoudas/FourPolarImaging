package fr.fresnel.fourPolar.core.physics.fourPolar;

import java.util.Hashtable;

/**
 * Models the orientation angles calculated using the four polar method.
 */
public class OrientationVector implements IOrientationVector {
    private Hashtable<FourPolarAngles, Float> _angle = new Hashtable<FourPolarAngles, Float>(3); 
    
    /**
     * Models the orientation angles calculated using the four polar method.
     * @param rho : rho angle in radian.
     * @param delta : delta angle in radian.
     * @param eta : eta angle in radian.
     */
    public OrientationVector(float rho, float delta, float eta){
        this._angle.put(FourPolarAngles.rho, rho);
        this._angle.put(FourPolarAngles.delta, delta);
        this._angle.put(FourPolarAngles.eta, eta);
    }

    @Override
    public float getAngle(FourPolarAngles angle) {
        return this._angle.get(angle);
    }
    
    public float getAngleInDegree(FourPolarAngles angle){
        return (float)Math.toDegrees(this.getAngle(angle));
    }
    
}