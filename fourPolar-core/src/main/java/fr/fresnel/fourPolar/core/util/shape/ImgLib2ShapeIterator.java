package fr.fresnel.fourPolar.core.util.shape;

import net.imglib2.Cursor;
import net.imglib2.roi.IterableRegion;
import net.imglib2.type.logic.BoolType;

/**
 * An iterator to iterator over discrete coordinate points.
 */
class ImgLib2ShapeIterator implements IShapeIterator {
    private final long[] _position;
    private final Cursor<Void> _regionCursor;

    /**
     * Form the iterator, corresponding to space properties and shape.
     * @param iterableRegion is the iterable of ImgLib2.
     * @param shapeDim
     */
    public ImgLib2ShapeIterator(IterableRegion<BoolType> iterableRegion, int shapeDim) {
        this._regionCursor = iterableRegion.cursor();
        this._position = new long[shapeDim];
    }

    @Override
    public boolean hasNext() {
        return _regionCursor.hasNext();
    }

    @Override
    public long[] next() {
        _regionCursor.next();
        this._regionCursor.localize(this._position);

        return this._position;
    }

    @Override
    public void reset() {
        _regionCursor.reset();
    }

    
    
}