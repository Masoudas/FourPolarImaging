package fr.fresnel.fourPolar.core.util.shape;

/**
 * An interface for a box shape. Box is represented using minimum and maximum
 * point.
 */
public interface IBoxShape extends IShape {
    public long[] min();

    public long[] max();
}