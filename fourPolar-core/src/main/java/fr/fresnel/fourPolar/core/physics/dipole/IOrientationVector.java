package fr.fresnel.fourPolar.core.physics.dipole;

/**
 * This interface models allows access to the orientation angles derived using
 * the four polar method.
 */
public interface IOrientationVector {
    /**
     * Method to get the orientation angle (in radian).
     * 
     * @param angle
     * @return
     */
    public float getAngle(OrientationAngle angle);

    /**
     * Method to get the orientation angle in degrees.
     * 
     * @param angle
     * @return
     */
    public float getAngleInDegree(OrientationAngle angle);
}