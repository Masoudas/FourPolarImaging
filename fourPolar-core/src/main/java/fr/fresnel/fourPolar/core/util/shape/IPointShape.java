package fr.fresnel.fourPolar.core.util.shape;

import fr.fresnel.fourPolar.core.util.shape.IShape;

/**
 * An interface for a point shape.
 */
public interface IPointShape extends IShape {
    public long[] point();
}