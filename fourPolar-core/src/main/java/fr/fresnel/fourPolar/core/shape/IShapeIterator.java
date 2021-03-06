package fr.fresnel.fourPolar.core.shape;

import java.util.Iterator;

/**
 * An interface for iterating over discete coordinates of a shape.
 */
public interface IShapeIterator extends Iterator<long[]> {
    public void reset();
}