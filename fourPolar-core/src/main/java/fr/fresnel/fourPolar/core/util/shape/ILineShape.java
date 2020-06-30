package fr.fresnel.fourPolar.core.util.shape;

/**
 * An interface for a line shape.
 */
public interface ILineShape extends IShape {
    /**
     * @return the starting point of this line.
     */
    public long[] lineStart();

    /**
     * @return the end of this line.
     */
    public long[] lineEnd();
}