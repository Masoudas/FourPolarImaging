package fr.fresnel.fourPolar.core.shape;

/**
 * An interface for a two dimensional polygon.
 */
public interface IPolygon2DShape extends IShape{
    /**
     * Returns the x vertices of the shape.
     */
    public long[] x_vertices();

    /**
     * Returns the y vertices of the shape.
     */
    public long[] y_vertices();
}