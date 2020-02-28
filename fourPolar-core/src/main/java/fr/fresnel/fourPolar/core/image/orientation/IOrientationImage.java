package fr.fresnel.fourPolar.core.image.orientation;

import fr.fresnel.fourPolar.core.fourPolar.IOrientationVectorIterator;
import fr.fresnel.fourPolar.core.image.captured.fileContainer.ICapturedImageFileSet;
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
     * Returns the {@link ICapturedImageFileSet} that this orientation image is made from.
     * @return
     */
    public ICapturedImageFileSet getCapturedSet();

    /**
     * Return the implementation of {@link IOrientationVectorIterator} for the orientation image.
     * @return
     */
    public IOrientationVectorIterator getOrientationVectorIterator();
}