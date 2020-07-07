package fr.fresnel.fourPolar.core.image.vector;

import fr.fresnel.fourPolar.core.image.generic.pixel.types.ARGB8;
import fr.fresnel.fourPolar.core.util.shape.IBoxShape;
import fr.fresnel.fourPolar.core.util.shape.ILineShape;
import fr.fresnel.fourPolar.core.util.shape.IPointShape;
import fr.fresnel.fourPolar.core.util.shape.IShape;

/**
 * An interface that defines a vector as is conventional of vector images.
 */
public interface IVector {
    /**
     * @return a concrete vector.
     */
    public static IVector createPointVector() {

    }

    /**
     * @return the shape associated with this vector.
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
     */
    public void setShape(IPointShape shape);

    /**
     * Sets the shape of this vector as a box shape. To draw a box shape, the
     * dimension of the box has to be two and in the xy-plane. Note however the axis
     * order has to be equal to the underlying image. Hence for example a cube
     * cannot be drawn, however a the 2D rectangle [0,0,1]->[1,1,1] can.
     * 
     */
    public void setShape(IBoxShape shape);

    /**
     * Sets the shape of this vector as a line shape. The line has to be in the
     * xy-plane, otherwise an exception is thrown. Note however the axis order has
     * to be equal to the underlying image. Hence for example the line from [0,0,1]
     * to [1,1,1] can be drawn, however the line from [1,0,0] to [1,1,1] cannot.
     * 
     */
    public void setShape(ILineShape shape);

    /**
     * Sets the color associated with (the boundary of) this shape.
     */
    public void setColor(ARGB8 color);

    /**
     * Sets the fill color for this shape.
     */
    public void setFill(ARGB8 color);

    /**
     * Sets the stroke width of the vector.
     */
    public void setStrokeWidth(int width);
}