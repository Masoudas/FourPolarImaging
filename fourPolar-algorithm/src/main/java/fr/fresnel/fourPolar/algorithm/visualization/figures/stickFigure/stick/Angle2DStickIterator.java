package fr.fresnel.fourPolar.algorithm.visualization.figures.stickFigure.stick;

import java.awt.Point;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

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
     * @return iterator that iterates over the region corresponding to this stick,
     *         in pixel coordinates.
     */
    public static Angle2DStickIterator form(double slopeAngle, long[] dipolePosition, int length, int thickness) {
        double xStart = dipolePosition[0] - Math.cos(slopeAngle) * ((length - 1) / 2);
        double yStart = dipolePosition[1] - Math.sin(slopeAngle) * ((length - 1) / 2);

        double xEnd = dipolePosition[0] + Math.cos(slopeAngle) * (length / 2);
        double yEnd = dipolePosition[1] + Math.sin(slopeAngle) * (length / 2);

        Line negativeLine = new Line(xStart, yStart, dipolePosition[0], dipolePosition[1]);
        negativeLine.setStrokeWidth(thickness);

        Line positiveLine = new Line(dipolePosition[0], dipolePosition[1], xEnd, yEnd);
        positiveLine.setStrokeWidth(thickness);

        return new Angle2DStickIterator(negativeLine.iterator(), positiveLine.iterator(), dipolePosition);
    }

    /**
     * Generates an iterator covering the line region.
     * 
     * @param negativeLineIterator is the iterator on negative points.
     * @param positiveLineIterator is the iterator on positive points.
     * @param position             is the position of the 2D stick.
     */
    public Angle2DStickIterator(Iterator<Point> negativeLineIterator, Iterator<Point> positiveLineIterator,
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

        try {
            point = _negativeIterator.next();
        } catch (NoSuchElementException e) {
            point = _positiveIterator.next();
        }

        this._position[0] = point.x;
        this._position[1] = point.y;
        return this._position;
    }
}