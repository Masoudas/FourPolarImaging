package fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage;

import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.util.shape.IShape;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.IGaugeFigure;

/**
 * An interface for painting a stick in a {@link IGaugeFigure}.
 */
public interface IAngleGaugePainter {
    /**
     * Draw the sticks in the specified region of the orientation image inside the
     * gauge figure, for positions where intensity >= soiThreshold.
     * <p>
     * For @param region, note that pixels start from 0 to image dimension - 1 of
     * the orientation image (or equivalently the soi Image). @param region must be
     * an XYCZT shape, because the underlying orientation image is always XYCZT,
     * otherwise an exception is thrown.
     * 
     * @param region       is the region over which we wish to draw sticks.
     * @param soiThreshold is the resired threshold for sum of intensity. For
     *                     positions below threshold, no stick is drawn.
     * 
     * @throws IllegalArgumentException in case the provided region is not XYCZT, or
     *                                  is not the desired shape (e.g, point region
     *                                  for {@link SingleDipoleStick2DPainterBuilder}).
     */
    public void draw(IShape region, UINT16 soiThreshold) throws IllegalArgumentException;

    /**
     * Returns the gauge figure filled with this painter.
     */
    public IGaugeFigure getFigure();

}