package fr.fresnel.fourPolar.core.shape;

import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;

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
     * Returns the Axis order associated with the shape.
     * 
     */
    public AxisOrder axisOrder();

    /**
     * Returns the dimension of the shape (see axisOrder). A 2D box (shapeDim=2) can
     * be defined over a 3D space (AxisOrder = XYZ or others).
     * 
     * @return
     */
    public int shapeDim();

    /**
     * @return the dimension of the space in which the shape is defined. 
     */
    public int spaceDim();

    /**
     * Apply a 3d rotation with the given rotation order.
     * 
     * @param angle1        is the rotation over the first axis in radian.
     * @param angle2        is the rotation over the second axis in radian.
     * @param angle3        is the rotation over the second axis in radian.
     * @param rotationOrder is the desired order of rotation
     */
    public IShape rotate3D(double angle1, double angle2, double angle3, Rotation3DOrder rotation3dOrder);

    /**
     * Apply a 2d rotation to the shape (which applies to the first two dimensions).
     * 
     * @param angle is the rotation over the first axis in radian.
     */
    public IShape rotate2D(double angle);

    /**
     * Translate this shape by the given vector. The vector dimension should equal
     * the space dimension. Translation after rotation is guaranteed to work
     * properly.
     */
    public IShape translate(long[] translation);

    /**
     * Checks whether the given point is inside the shape. If the point dimension is
     * not equal to the space dimension of the shape (see shapeDim and spaceDim),
     * then false is returned.
     * 
     * @throws IllegalArgumentException in case the point does not have same number
     *                                  of axis as shape.
     */
    public boolean isInside(long[] point);

    /**
     * Ands this shape with the given shape and returns a new shape. In case there's
     * no overlap, the resulting shape has no elements. Note that the new shape
     * instance is not an instance of the old shape. For example, anding two
     * {@link IBoxShape} does not yield another box shape.
     * 
     * @throws IllegalArgumentException in case the two shapes don't have same
     *                                  number of axis.
     * 
     */
    public IShape and(IShape shape);
}