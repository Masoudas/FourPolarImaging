package fr.fresnel.fourPolar.core.fourPolar;

import java.util.Iterator;

import fr.fresnel.fourPolar.core.physics.dipole.IOrientationVector;

/**
 * An iterator for iterating over a set of {@link IOrientationVector}.
 */
public interface IOrientationVectorIterator extends Iterator<IOrientationVector> {
    /**
     * Set the {@link IOrientationVector} of the current location.
     * @param vector
     */
    public void set(IOrientationVector vector);

    /**
     * Returns the number of orientation vectors associated with this iterator.
     * @return
     */
    public long size();
}