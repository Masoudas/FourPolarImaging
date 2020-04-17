package fr.fresnel.fourPolar.core.util.shape;

/**
 * An interface that models a shape. A shape is a region of discrete
 * coordinates, e.g, Box, Polygon, etc.
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
     * Returns the number of dimensions the shape span. For example, a 2D box span
     * two or more dimensions.
     */
    public int numDimension();

    /**
     * Returns the dimension of the shape (see numDimension).
     * 
     * @return
     */
    public int shapeDim();

    /**
     * Apply a 2d affine transform to the original shape in the xy plane. Note that
     * the same reference is returned for every rotated shape.
     * 
     * @param translation is the translation. Must be 2d.
     * @param rotation    is the rotation angle in radian.
     */
    public IShape transform2D(long[] translation, double rotation);

    /**
     * Apply a 3d affine transform to the original shape in the xyz space. Note that
     * the same reference is returned for every rotated shape.
     * <p>
     * VERY IMPORTANT NOTE: The order of rotation would be x, z and then y.
     * 
     * @param translation is the translation. Must be 3d.
     * @param x_rotation  is the rotation around x-axis in radian.
     * @param z_rotation  is the rotation around z-axis in radian.
     * @param y_rotation  is the rotation around y-axis in radian.
     */
    public IShape transform3D(long[] translation, double x_rotation, double z_rotation, double y_rotation);

    /**
     * Checks whether the given point is inside the shape.
     */
    public boolean isInside(long[] point);

}