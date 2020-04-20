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
     * Returns the dimension of the space over which the shape is defined.
     */
    public int spaceDim();

    /**
     * Returns the dimension of the shape (see numDimension).
     * 
     * @return
     */
    public int shapeDim();

    /**
     * Apply a 3d affine rotation to the original shape in the xyz space.
     * <p>
     * VERY IMPORTANT NOTE: The order of rotation would be x, z and then y. If only
     * xy rotation is needed, use z_rotation. Use getTransformedShape() to access
     * the rotated shape.
     * 
     * @param x_rotation is the rotation around x-axis in radian.
     * @param z_rotation is the rotation around z-axis in radian.
     * @param y_rotation is the rotation around y-axis in radian.
     */
    public IShape rotate(double x_rotation, double z_rotation, double y_rotation);

    /**
     * Apply a translation to the original shape.
     * <p>
     * VERY IMPORTANT NOTE: If rotation is applied, the order would be rotate, then
     * translate.
     * 
     * @param translation is the desired translation. If number of dimensions is
     *                    less than the shape dimension, an IllegalArgumentException
     *                    is raised.
     */
    public void translate(long[] translation);

    /**
     * Return the transformed shape. Note that the same reference is returned after
     * each transformation. Once a transformed shape is returned, this method returns
     * the original image.
     * 
     */
    public IShape getTransformedShape();

    /**
     * Checks whether the given point is inside the shape.
     */
    public boolean isInside(long[] point);

}