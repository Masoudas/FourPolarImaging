package fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage;

import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.util.shape.IShape;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.IGaugeFigure;

/**
 * An interface for painting a stick in a {@link IGaugeFigure}.
 */
public interface IAngleGaugePainter {
    /**
     * Draw the sticks in the specified region, for positions where intensity >=
     * soiThreshold.
     * <p>
     * For the region, note that the pixels start from 0 to image dimension - 1 of
     * the orientation image (or equivalently the soi Image). If the space dimension
     * of the provided region is greater than that of soi Image, an exception is
     * thrown. Further constrains maybe set by implementation classes.
     * 
     * @param region       is the region over which we wish to draw sticks.
     * @param soiThreshold is the resired threshold for sum of intensity. For
     *                     positions below threshold, no stick is drawn.
     * 
     * @throws IllegalArgumentException in case the provided region does not satisfy
     *                                  the constrains demanded by implementations.
     */
    public void draw(IShape region, UINT16 soiThreshold) throws IllegalArgumentException;

    /**
     * Returns the gauge figure filled with this painter.
     */
    public IGaugeFigure getFigure();

}