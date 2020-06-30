package fr.fresnel.fourPolar.core.physics.dipole;

import fr.fresnel.fourPolar.core.exceptions.physics.dipole.OrientationAngleOutOfRange;

/**
 * This interface models allows access to the orientation angles derived using
 * the four polar method.
 */
public interface IOrientationVector {
    /**
     * Maximum possible value for the rho
     */
    public static final double MAX_Rho = Math.PI;

    /**
     * Maximum possible value for the delta
     */
    public static final double MAX_Delta = Math.PI;

    /**
     * Maximum possible value for the delta
     */
    public static final double MAX_Eta = Math.PI / 2;
    
    /**
     * The maximum allowed devation from the given angle ranges. Hence: rho in [0 -
     * ERR_Angle, MAX_Rho + ERR_Angle] delta in [0 - ERR_Angle, MAX_delta +
     * ERR_Angle] eta in [0 - ERR_Angle, MAX_eta + ERR_Angle]
     */
    public static final double ERR_Angle = Math.PI / 180 * 0.0001;

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
     * Method to set the orientation angles in radian.
     * 
     * @param rho   is rho in radian.
     * @param delta is delta in radian.
     * @param eta   is eta in radian.
     * @throws OrientationAngleOutOfRange if the an angle is out of the specified
     *                                    range.
     */
    public void setAngles(double rho, double delta, double eta) throws OrientationAngleOutOfRange;

    /**
     * Returns true if none of the orientation angles are undefined (are NaN).
     */
    public boolean isWellDefined();
}