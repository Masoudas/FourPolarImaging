package fr.fresnel.fourPolar.core.image.vector;

import java.util.Optional;

import fr.fresnel.fourPolar.core.image.generic.pixel.types.ARGB8;
import fr.fresnel.fourPolar.core.image.vector.animation.Animation;
import fr.fresnel.fourPolar.core.image.vector.filter.FilterComposite;
import fr.fresnel.fourPolar.core.shape.IBoxShape;
import fr.fresnel.fourPolar.core.shape.ILineShape;
import fr.fresnel.fourPolar.core.shape.IShape;

/**
 * An interface that defines a vector as is conventional of vector images.
 */
public interface Vector {
    /**
     * Creates a concrete vector that has a line shape, with all other properties
     * set to null.
     * 
     * @param lineShape is the line shape.
     * @return a concrete vector.
     */
    public static Vector createLineVector(ILineShape lineShape) {
        return new DefaultVector(lineShape);
    }

    /**
     * Creates a concrete vector that has a box shape, with all other properties
     * set to null.
     * 
     * @param boxShape is the box shape.
     * @return a concrete vector.
     */
    public static Vector createBoxVector(IBoxShape boxShape) {
        return new DefaultVector(boxShape);
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
    public Optional<ARGB8> color();

    /**
     * @return the fill color for this shape.
     */
    public Optional<ARGB8> fill();

    /**
     * @return the stroke width of this vector.
     */
    public Optional<Integer> strokeWidth();

    /**
     * @return the animation associated with this vector.
     */
    public Optional<Animation> animation();

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
     * filter is not set, the optional in {@link #filter()} would be empty. It's the
     * responsibility of the caller to ensure that the filter is defined in the SVG
     * document, otherwise adding the filter has no effect on the element.
     */
    public void setFilter(FilterComposite composite);

    /**
     * Sets an animation for this vector. The animation would solely be applied to
     * this vector. If animation is not set, the {@link #animation()} optional would
     * be empty.
     */
    public void setAnimation(Animation animation);
}