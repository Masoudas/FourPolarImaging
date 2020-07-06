package fr.fresnel.fourPolar.core.image.vector;

import fr.fresnel.fourPolar.core.image.generic.pixel.types.ARGB8;
import fr.fresnel.fourPolar.core.util.shape.IBoxShape;
import fr.fresnel.fourPolar.core.util.shape.ILineShape;
import fr.fresnel.fourPolar.core.util.shape.IPointShape;
import fr.fresnel.fourPolar.core.util.shape.IShape;

/**
 * An interface that defines a vector as is conventional of vector images.
 */
public interface Vector {
    /**
     * @return the shape of this vector.
     */
    public IShape shape();

    /**
     * @return the color associated with (the boundary of) this shape.
     */
    public ARGB8 color();

    /**
     * @return the fill color for this shape.
     */
    public ARGB8 fill();

    /**
     * @return the stroke width of this vector.
     */
    public int strokeWidth();

    /**
     * Sets the shape of this vector as a point shape.
     * 
     * @throws IllegalArgumentException if the {@link AxisOrder} of the point is not
     *                                  the same as the underlying image.
     */
    public void setShape(IPointShape shape);

    /**
     * Sets the shape of this vector as a box shape. To draw a box shape, the
     * dimension of the box has to be two and in the xy-plane. Note however the axis
     * order has to be equal to the underlying image. Hence for example a cube
     * cannot be drawn, however a the 2D rectangle [0,0,1]->[1,1,1] can.
     * 
     * @throws IllegalArgumentException if the {@link AxisOrder} of the box is not
     *                                  the same as the underlying image. Or in case
     *                                  the dimension of the image is not equal to
     *                                  two in the xy plane.
     */
    public void setShape(IBoxShape shape);

    /**
     * Sets the shape of this vector as a line shape. The line has to be in the
     * xy-plane, otherwise an exception is thrown. Note however the axis order has
     * to be equal to the underlying image. Hence for example the line from [0,0,1]
     * to [1,1,1] can be drawn, however the line from [1,0,0] to [1,1,1] cannot.
     * 
     * @throws IllegalArgumentException if the {@link AxisOrder} of the point is not
     *                                  the same as the underlying image. Or in case
     *                                  the dimension of the image is not equal to
     *                                  two.
     */
    public void setShape(ILineShape shape);

    /**
     * Sets the color associated with (the boundary of) this shape.
     */
    public void setColor(ARGB8 color);

    /**
     * Set the fill color for this shape.
     */
    public void setFill();

    /**
     * Sets the stroke width of the vector.
     */
    public void setStrokeWidth(int width);
}