package fr.fresnel.fourPolar.core.visualization.figures.stickFigure;

import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.util.DPoint;

/**
 * This interface represents a stick, which is used to visualize a particular
 * {@link OrientationAngle}. A stick has:
 * <ul>
 * <li>position: The central {@link Point} in the 2D plane (not the
 * n-dimensional position)</li>
 * <li>color: Representing the color of the stick (that corresponds to the value
 * of a particular orientation angle</li>
 * <li>length: This would be the x length of the stick. Note that the length
 * counts for negative and positive values of the stick. Hence a stick of length
 * 3 starts from -1 and ends in 1.</li>
 * </ul>
 * 
 */
public interface IAngleStick {
    /**
     * Returns a 2D vector, corresponding to the pixel position of the stick in the
     * plane.
     * 
     */
    public DPoint getPosition();

    /**
     * Returns the color associated with this stick.
     */
    public RGB16 getColor();

    /**
     * Returns the length of this stick.
     */
    public int getLength();

    /**
     * Returns the slope of the stick in radian.
     */
    public float getSlopeAngle();

    /**
     * Returns the thickness of this stick.
     */
    public int getThickness();

    /**
     * Returns the {@link IAngleStickIterator} of this stick. Each time this method
     * is called, a new instance of the iterator is returned.
     */
    public IAngleStickIterator getIterator();

}