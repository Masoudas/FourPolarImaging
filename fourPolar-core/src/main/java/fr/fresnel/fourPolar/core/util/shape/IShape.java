package fr.fresnel.fourPolar.core.util.shape;

/**
 * An interface that models a shape. A shape is a region of discrete coordinates,
 * e.g, Box, Polygon, etc.
 */
public interface IShape {
    /**
     * Returns an iterator that goes over the shape and returns the coordinates.
     */
    public IShapeIteraror getIterator();

    /**
     * Returns the underlying type of the shape.
     */
    public ShapeType getType();

    /**
     * Returns the number of dimensions the shape span. For example, a 2D box
     * span two or more dimensions.
     */
    public int numDimension();

    /**
     * Returns the dimension of the shape (see numDimension).
     * @return
     */
    public int shapeDim();

}