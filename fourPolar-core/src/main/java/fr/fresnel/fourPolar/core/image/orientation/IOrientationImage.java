package fr.fresnel.fourPolar.core.image.orientation;

import fr.fresnel.fourPolar.core.image.orientation.fileContainer.IOrientationImageFileSet;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;

/**
 * An interface for accessing a set of {@link Image} interface, each of which
 * corresponds to one {@link OrientationAngle}.
 */
public interface IOrientationImage {
    /**
     * Returns the {@link IAngleImage}.
     * 
     * @param angle 
     * @return
     */
    public IAngleImage getAngleImage(OrientationAngle angle);

    /**
     * Returns the {@link IOrientationImageFileSet} that corresponds to this orientation image.
     * @return
     */
    public IOrientationImageFileSet getFileSet();

}