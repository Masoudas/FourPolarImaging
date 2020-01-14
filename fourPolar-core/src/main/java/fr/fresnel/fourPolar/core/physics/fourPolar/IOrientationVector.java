package fr.fresnel.fourPolar.core.physics.fourPolar;

/**
 * This interface models allows access to the orientation angles derived using
 * the four polar method.
 */
public interface IOrientationVector {
    public float getAngle(FourPolarAngles angle);

    public float getAngleInDegree(FourPolarAngles angle);
}