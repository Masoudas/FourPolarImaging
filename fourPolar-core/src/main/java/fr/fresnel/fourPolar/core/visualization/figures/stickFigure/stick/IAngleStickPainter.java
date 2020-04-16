package fr.fresnel.fourPolar.core.visualization.figures.stickFigure.stick;

import fr.fresnel.fourPolar.core.util.shape.IShapeIteraror;
import fr.fresnel.fourPolar.core.visualization.figures.stickFigure.IStickFigure;

/**
 * An interface for painting a stick in a {@link IStickFigure}.
 */
public interface IAngleStickPainter {
    /**
     * Draw the sticks in the specified region by shapeIterator, for positions where
     * intensity >= soiThreshold.
     * 
     * @param shapeIteraror is the iterator of the region over which we wish to draw
     *                      sticks.
     * @param soiThreshold  is the resired threshold for sum of intensity. For
     *                      positions below threshold, no stick is drawn.
     */
    public void draw(IShapeIteraror shapeIteraror, double soiThreshold);

    /**
     * Returns the stick figure filled with this painter.
     */
    public IStickFigure getStickFigure();

}