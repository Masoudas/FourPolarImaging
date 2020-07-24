package fr.fresnel.fourPolar.core.shape;

import java.awt.Point;
import java.util.Iterator;

class ImageJ1LineShapeIterator implements IShapeIterator {
    private final Iterator<Point> _lineIterator;
    private final long[] _position;

    public ImageJ1LineShapeIterator(Iterator<Point> lineIterator, long[] position) {
        _lineIterator = lineIterator;
        _position = position;
    }

    @Override
    public boolean hasNext() {
        return _lineIterator.hasNext();
    }

    @Override
    public long[] next() {
        Point point = _lineIterator.next();

        long[] position = this._position.clone();
        position[0] = point.x;
        position[1] = point.y;
        return position;

    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Line shape iterator can't be reset.");
    }
}