package fr.fresnel.fourPolar.algorithm.visualization.figures.stickFigure.stick;

import java.awt.Point;
import java.util.Iterator;
import java.util.NoSuchElementException;

import fr.fresnel.fourPolar.core.util.DPoint;
import fr.fresnel.fourPolar.core.visualization.figures.stickFigure.stick.IAngleStickIterator;

/**
 * A concrete implementation of the {@link IAngleStickIterator}, which uses two
 * iterators, one on x < pos.x and one on x >= pos.x, and returns the
 * correspondong {@link DPoints}.
 */
class AngleStickIterator implements IAngleStickIterator {
    private final Iterator<Point> _negativeIterator;
    private final Iterator<Point> _positiveIterator;

    /**
     * Generates an iterator covering the line region.
     * 
     * @param negativeLineIterator is the iterator on negative points.
     * @param positiveLineIterator is the iterator on positive points.
     */
    public AngleStickIterator(Iterator<Point> negativeLineIterator, Iterator<Point> positiveLineIterator) {
        this._negativeIterator = negativeLineIterator;
        this._positiveIterator = positiveLineIterator;
    }

    @Override
    public boolean hasNext() {
        return _negativeIterator.hasNext() || _positiveIterator.hasNext();
    }

    @Override
    public DPoint next() throws NoSuchElementException {
        Point point;

        try {
            point = _negativeIterator.next();
        } catch (NoSuchElementException e) {
            point = _positiveIterator.next();
        }

        return new DPoint(point.x, point.y);
    }
}