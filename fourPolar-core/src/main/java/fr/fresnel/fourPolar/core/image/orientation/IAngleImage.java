package fr.fresnel.fourPolar.core.image.orientation;

import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;

/**
 * This interface is for access to an {@link OrientationAngle} image.
 */
public interface IAngleImage {
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