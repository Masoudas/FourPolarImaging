package fr.fresnel.fourPolar.core.image.orientation;

import fr.fresnel.fourPolar.core.physics.dipole.IOrientationVector;

/**
 * Using this interface, we can randomly access a location inside the
 * orientation image, and read/set the value for that. Note that to salvage
 * memory, only one instance of {@link IOrientationVector} is created.
 */

public interface IOrientationImageRandomAccess {
    /**
     * Sets the iterator position to the position specified. If position is out of
     * range, the iterator position is not set.
     * 
     * @param position
     */
    public void setPosition(long[] position);

    /**
     * Sets the orientation associated with the position.
     * 
     * @param IOrientationImage is the pixel at this location
     * @throws ArrayIndexOutOfBoundsException in case the provided position does not
     *                                        exist.
     */
    public void setOrientation(IOrientationVector orientationVector) throws ArrayIndexOutOfBoundsException;

    /**
     * Returns the orientation associated with the position.
     * 
     * @param pixel is the pixel at this location
     * @throws ArrayIndexOutOfBoundsException in case the provided position does not
     *                                        exist.
     */
    public IOrientationVector getOrientation() throws ArrayIndexOutOfBoundsException;

}