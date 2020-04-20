package fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage;

import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.util.shape.IShape;
import fr.fresnel.fourPolar.core.util.shape.IShapeIteraror;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.IGaugeFigure;

/**
 * An interface for painting a stick in a {@link IGaugeFigure}.
 */
public interface IAngleGaugePainter {
    /**
     * Draw the sticks in the specified region by shapeIterator, for positions where
     * intensity >= soiThreshold.
     * <p>
     * For the shape, note that the pixels start from 0 to image dimension - 1. If a
     * pixel of the shape is out of image dimension, no sticks are drawn, and the
     * process continues.
     * 
     * @param region       is the region over which we wish to draw sticks.
     * @param soiThreshold is the resired threshold for sum of intensity. For
     *                     positions below threshold, no stick is drawn.
     */
    public void draw(IShape region, UINT16 soiThreshold);

    /**
     * Returns the stick figure filled with this painter.
     */
    public IGaugeFigure getStickFigure();

}