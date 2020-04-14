package fr.fresnel.fourPolar.algorithm.visualization.figures.stickFigure.stick;

import java.awt.Point;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import fr.fresnel.fourPolar.core.util.DPoint;
import fr.fresnel.fourPolar.core.visualization.figures.stickFigure.stick.IAngleStickIterator;
import ij.gui.Line;

/**
 * A concrete implementation of the {@link IAngleStickIterator}, which uses two
 * iterators, one on x < pos.x and one on x >= pos.x, and returns the
 * correspondong {@link DPoints}.
 */
class Angle2DStickIterator implements IAngleStickIterator {
    private final Iterator<Point> _negativeIterator;
    private final Iterator<Point> _positiveIterator;
    private final long[] _position;

    /**
     * To form an iterator that iterates over the pixels corresponding to the stick,
     * define a negative line, one that starts from the end point to the position,
     * and one that starts from position to the other line. This would ensure that
     * the stick always passes through the dipole!
     * 
     * @param slopeAngle     is the slope of the dipole in particular direction.
     * @param dipolePosition is the pixel position of the dipole, as [x, y, z, ...]
     * @param length         is the desired length of the stick.
     * @param thickness      is the desired thickness of the stick.
     * @param planeDim       is the dimension of the image plane.
     * @return iterator that iterates over the region corresponding to this stick,
     *         in pixel coordinates.
     */
    public static Angle2DStickIterator form(double slopeAngle, long[] dipolePosition, int length, int thickness,
            DPoint planeDim) {
        double slope =  Math.tan(slopeAngle);
        double cosSlopeAngle = Math.cos(slopeAngle);        

        double[] xyStart = new double[2];
        double xS0 = dipolePosition[0] - cosSlopeAngle * ((length - 1) / 2);
        xyStart[0] = xS0 < 1 ? 1 : xS0;
        xyStart[1] = dipolePosition[1] + (xS0 - dipolePosition[0]) * slope;

        double[] xyEnd = new double[2];
        double xE0 = dipolePosition[0] + cosSlopeAngle * (length / 2);
        xyEnd[0] = xE0 > planeDim.x ? planeDim.x : xE0;
        xyEnd[1] = dipolePosition[1] + (xE0 - dipolePosition[0]) * slope;

        RangeChecker checkerLambda = (double[] XY) -> {
            if (XY[1] > planeDim.y) {
                XY[1] = planeDim.y;
                XY[0] = dipolePosition[0] + (planeDim.y - dipolePosition[1]) / slope;
            } else if (XY[1] < 1) {
                XY[1] = 1;
                XY[0] = dipolePosition[0] + (1 - dipolePosition[1]) / slope;
            }
        };

        checkerLambda._checkY(xyStart);
        checkerLambda._checkY(xyEnd);

        Line negativeLine = new Line(xyStart[0], xyStart[1], dipolePosition[0], dipolePosition[1]);
        negativeLine.setStrokeWidth(thickness);

        Line positiveLine = new Line(dipolePosition[0], dipolePosition[1], xyEnd[0], xyEnd[1]);
        positiveLine.setStrokeWidth(thickness);

        return new Angle2DStickIterator(negativeLine.iterator(), positiveLine.iterator(), dipolePosition);
    }

    interface RangeChecker {
        void _checkY(double[] XY);
    }

    /**
     * Generates an iterator covering the line region.
     * 
     * @param negativeLineIterator is the iterator on negative points.
     * @param positiveLineIterator is the iterator on positive points.
     * @param position             is the position of the 2D stick.
     */
    private Angle2DStickIterator(Iterator<Point> negativeLineIterator, Iterator<Point> positiveLineIterator,
            long[] positition) {
        this._negativeIterator = negativeLineIterator;
        this._positiveIterator = positiveLineIterator;
        this._position = Arrays.copyOf(positition, positition.length);
    }

    @Override
    public boolean hasNext() {
        return _negativeIterator.hasNext() || _positiveIterator.hasNext();
    }

    @Override
    public long[] next() throws NoSuchElementException {
        Point point;

        if (_negativeIterator.hasNext()) {
            point = _negativeIterator.next();
        } else {
            point = _positiveIterator.next();
        }

        this._position[0] = point.x;
        this._position[1] = point.y;
        return this._position;
    }
}