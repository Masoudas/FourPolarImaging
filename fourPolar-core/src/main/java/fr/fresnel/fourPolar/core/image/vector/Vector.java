package fr.fresnel.fourPolar.core.image.vector;

import java.util.Optional;

import fr.fresnel.fourPolar.core.image.generic.pixel.types.ARGB8;
import fr.fresnel.fourPolar.core.image.vector.filter.FilterComposite;
import fr.fresnel.fourPolar.core.util.shape.IBoxShape;
import fr.fresnel.fourPolar.core.util.shape.ILineShape;
import fr.fresnel.fourPolar.core.util.shape.IPointShape;
import fr.fresnel.fourPolar.core.util.shape.IShape;

/**
 * An interface that defines a vector as is conventional of vector images.
 */
public interface Vector {
    /**
     * Creates a concrete vector that has a line shape, with the same fill and
     * boundary color, and stroke width equal to one.
     * 
     * @param lineShape is the line shape.
     * @return a concrete vector.
     */
    public static Vector createLineVector(ILineShape lineShape, ARGB8 color) {
        return new DefaultVector(lineShape, color);
    }

    /**
     * Creates a concrete vector that has a box shape, with the same fill and
     * boundary color, and stroke width equal to one.
     * 
     * @param boxShape is the line shape.
     * @return a concrete vector.
     */
    public static Vector createBoxVector(IBoxShape boxShape, ARGB8 color) {
        return new DefaultVector(boxShape, color);
    }

    /**
     * @return the filter composite that is applied to this element.
     */
    public Optional<FilterComposite> filter();

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

    /**
     * Sets the filter composite that should be applied to this element. If the
     * filter is not set, the optional in {@link #getFilter()} would be empty. It's
     * the responsibility of the caller to ensure that the filter is defined in the
     * SVG document, otherwise adding the filter has no effect on the element.
     */
    public void setFilter(FilterComposite composite);
}