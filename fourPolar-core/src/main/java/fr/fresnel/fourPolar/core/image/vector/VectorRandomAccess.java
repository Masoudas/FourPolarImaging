package fr.fresnel.fourPolar.core.image.vector;

import java.util.RandomAccess;

/**
 * An interface for randomly accessing the underlying {@link IVector} of the
 * {@link VectorImage}.
 */
public interface VectorRandomAccess extends RandomAccess {
    /**
     * Sets the position for accessing the underlying vector. It's the
     * responsability of the caller to ensure that the given position is valid.
     */
    public void setPosition(double[] position);

    /**
     * Sets the vector of the given position.
     */
    public void setVector(IVector vector);

}