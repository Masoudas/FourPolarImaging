package fr.fresnel.fourPolar.core.util.shape;

import java.util.Iterator;

/**
 * An interface for iterating over the pixels corresponding to an angle stick of
 * a particular length and thickness.
 */
public interface IShapeIteraror extends Iterator<long[]> {
    public void reset();
}