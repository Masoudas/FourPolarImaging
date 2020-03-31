package fr.fresnel.fourPolar.core.visualization.figures.stickFigure;

import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.util.DPoint;
import fr.fresnel.fourPolar.core.util.Point;
import ij.gui.Line;

/**
 * A concrete implementation of {@link IAngleStick}. Note that the sticks can
 * have negative coordinates in this implementation.
 */
public class AngleStick implements IAngleStick {
    private final DPoint _pose;
    private final RGB16 _color;
    private final int _len;
    private final float _slopeAngle;
    private final int _thickness;

    /**
     * Creates a stick representing the dipole based on the following parameters:
     * 
     * @param pose       is the position of the stick in the image coordinates.
     * @param slopeAngle is the angle corresponding this stick. It should be given
     *                   in radian.
     * @param len        is the length of the stick in pixels.
     * @param thickness  is the thickness of the stick in pixels.
     * @param color      is the {@link RGB16} color of the pixel.
     */
    public AngleStick(DPoint pose, float slopeAngle, int len, int thickness, RGB16 color) {
        this._pose = pose;
        this._color = color;
        this._len = len;
        this._slopeAngle = slopeAngle;
        this._thickness = thickness;
    }

    @Override
    public DPoint getPosition() {
        return _pose;
    }

    @Override
    public RGB16 getColor() {
        return _color;
    }

    @Override
    public int getLength() {
        return _len;
    }

    @Override
    public float getSlopeAngle() {
        return _slopeAngle;
    }

    @Override
    public int getThickness() {
        return _thickness;
    }

    @Override
    public IAngleStickIterator getIterator() {
        /**
         * Define a negative line, one that starts from the end point to the position,
         * and one that starts from position to the other line.
         * 
         * This would ensure that the stick always passes through the dipole!
         */
        Point[] endPoints = _getEndPoints();
        Line negativeLine = new Line(endPoints[0].x, endPoints[0].y, this._pose.x, this._pose.y);
        negativeLine.setStrokeWidth(this._thickness);
        
        Line positiveLine = new Line(this._pose.x, this._pose.y, endPoints[1].x, endPoints[1].y);
        positiveLine.setStrokeWidth(this._thickness);

        return new AngleStickIterator(negativeLine.iterator(), positiveLine.iterator());
    }

    /**
     * Calculate the end points of the stick, assuming the length of the stick and 
     * knowing the angle of the stick.
     * 
     * @return start_point and end_point.
     */
    private Point[] _getEndPoints() {
        double xStart = this._pose.x - Math.cos(this._slopeAngle) * ((this._len - 1) / 2);
        double yStart = this._pose.y - Math.sin(this._slopeAngle) * ((this._len - 1) / 2);

        double xEnd = this._pose.x + Math.cos(this._slopeAngle) * (this._len / 2);
        double yEnd = this._pose.y + Math.sin(this._slopeAngle) * (this._len / 2);

        return new Point[] { new Point(xStart, yStart), new Point(xEnd, yEnd) };
    }
}