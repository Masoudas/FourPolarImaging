package fr.fresnel.fourPolar.core.util.shape;

/**
 * An interface that models a shape. A shape is a region of discrete
 * coordinates, e.g, Box, Polygon, etc.
 */
public interface IShape {
    /**
     * Returns an iterator that goes over the shape and returns the coordinates.
     */
    public IShapeIterator getIterator();

    /**
     * Returns the underlying type of the shape.
     */
    public ShapeType getType();

    /**
     * Returns the dimension of the space over which the shape is defined. A 2D box
     * (shapeDim=2) can be defined a 3D space (spaceDim=3).
     */
    public int spaceDim();

    /**
     * Returns the dimension of the shape (see spaceDim). A 2D box (shapeDim=2) can
     * be defined a 3D space (spaceDim=3).
     * 
     * @return
     */
    public int shapeDim();

    /**
     * Apply a 3d rotation with the given axis order, where axis in {0, 1, 2},
     * non-repetitive.
     * 
     * @param angle1 is the rotation over the first axis in radian.
     * @param angle2 is the rotation over the second axis in radian.
     * @param angle3 is the rotation over the second axis in radian.
     * @param axis   is a 3d vector. Axis should be less than space dim.
     */
    public void rotate3D(double angle1, double angle2, double angle3, int[] axis);

    /**
     * Apply a 2d rotation to the shape (which applies to the first two dimensions).
     * 
     * @param angle is the rotation over the first axis in radian.
     */
    public void rotate2D(double angle);

    /**
     * Translate this shape by the given vector. The vector dimension should equal
     * the space dimension. Translation after rotation is guaranteed to work
     * properly.
     */
    public void translate(long[] translation);

    /**
     * Using this method, we can reset the shape to it's original untransformed
     * formation.
     */
    public void resetToOriginalShape();

    /**
     * Checks whether the given point is inside the shape. If the point dimension is
     * not equal to the space dimension of the shape (see shapeDim and spaceDim), then
     * false is returned.
     */
    public boolean isInside(long[] point);

    /**
     * Ands this shape with the given shape. In case there's no overlap, the
     * resulting shape has no elements.
     * 
     * @throws IllegalArgumentException in case source and destination shape don't
     *                                  have the same space dimension.
     * 
     */
    public void and(IShape shape);

}