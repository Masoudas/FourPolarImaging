package fr.fresnel.fourPolar.core.shape;

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

    /**
     * @return the starting point of this line more precisely as double.
     */
    public double[] lineStartAsDouble();

    /**
     * @return the end of this line.
     */
    public double[] lineEndAsDouble();
}