package fr.fresnel.fourPolar.core.physics.dipole;

/**
 * This interface models allows access to the orientation angles derived using
 * the four polar method.
 */
public interface IOrientationVector {
    public float getAngle(OrientationAngle angle);

    public float getAngleInDegree(OrientationAngle angle);
}