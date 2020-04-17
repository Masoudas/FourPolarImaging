package fr.fresnel.fourPolar.core.util.shape;

import java.util.Iterator;

/**
 * An interface for iterating over discete coordinates of a shape.
 */
public interface IShapeIteraror extends Iterator<long[]> {
    public void reset();
}