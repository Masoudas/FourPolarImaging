package fr.fresnel.fourPolar.core.visualization.figures.stickFigure;

import java.awt.Point;
import java.util.Iterator;

import fr.fresnel.fourPolar.core.util.DPoint;
import ij.gui.Line;

/**
 * A concrete implementation of the {@link IAngleStickIterator}, which uses the
 * {@link Line} of ImageJ to generate line pixels.
 */
class AngleStickIterator implements IAngleStickIterator {
    private final Iterator<Point> _iterator;

    /**
     * Generates an iterator covering the line region.
     * @param start is the starting point of the line.
     * @param end is the end point of the line.
     * @param thickNess is the thickness of the assciated line.
     */
    public AngleStickIterator(DPoint start, DPoint end, int thickNess) {
        Line line = new Line(start.x, start.y, end.x, end.y);
        line.setStrokeWidth(thickNess);

        this._iterator = line.iterator();
    }

    @Override
    public boolean hasNext() {
        return _iterator.hasNext();
    }

    @Override
    public DPoint next() {
        Point point = _iterator.next();
        return new DPoint(point.x, point.y);
    }
}