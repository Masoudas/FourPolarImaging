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
     * Apply a 3d affine rotation and translation to the original shape in the xyz
     * space.
     * <p>
     * VERY IMPORTANT NOTE: The order of rotation would be x, z and then y. If only
     * xy rotation is needed, use z_rotation.
     * 
     * @param x_rotation is the rotation around x-axis in radian.
     * @param z_rotation is the rotation around z-axis in radian.
     * @param y_rotation is the rotation around y-axis in radian.
     */
    public void transform(long[] translation, double x_rotation, double z_rotation, double y_rotation);

    /**
     * Using this method, we can reset the shape to it's original untransformed
     * formation.
     */
    public void resetToOriginalShape();

    /**
     * Checks whether the given point is inside the shape. If the point dimension is
     * less than the space dimension of the shape (see shapeDim and spaceDim), then
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