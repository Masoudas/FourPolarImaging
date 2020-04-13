package fr.fresnel.fourPolar.core.physics.dipole;

import fr.fresnel.fourPolar.core.exceptions.physics.dipole.OrientationAngleOutOfRange;

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
    public double getAngle(OrientationAngle angle);

    /**
     * Method to get the orientation angle in degrees.
     * 
     * @param angle
     * @return
     */
    public double getAngleInDegree(OrientationAngle angle);

    /**
     * Method to set the orientation angles.
     * @param rho
     * @param delta
     * @param eta
     * @throws OrientationAngleOutOfRange
     */
    public void setAngles(double rho, double delta, double eta) throws OrientationAngleOutOfRange;
}