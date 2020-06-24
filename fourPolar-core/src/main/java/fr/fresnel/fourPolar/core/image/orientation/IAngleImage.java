package fr.fresnel.fourPolar.core.image.orientation;

import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImage;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;

/**
 * The interface for accessing an angle image. An angle image is the equivalent
 * image representation of an orientation angle (rho, delta or eta) of a sample
 * for a particular frequency, where each pixel represents the angle at that
 * location. Note that the underlying angles are in radians.
 */
public interface IAngleImage {
    /**
     * Axis order of the image.
     */
    public static final AxisOrder AXIS_ORDER = IPolarizationImage.AXIS_ORDER;

    /**
     * Returns the orientation angle image interface.
     * 
     * @return
     */
    public Image<Float32> getImage();

    /**
     * Returns the orientation angle this image corresponds to.
     * 
     * @return
     */
    public OrientationAngle getOrientationAngle();

}