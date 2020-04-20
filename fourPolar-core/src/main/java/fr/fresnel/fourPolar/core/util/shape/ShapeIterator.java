package fr.fresnel.fourPolar.core.util.shape;

import net.imglib2.Cursor;
import net.imglib2.roi.IterableRegion;
import net.imglib2.type.logic.BoolType;

class ShapeIterator implements IShapeIterator {
    private final long[] _position;
    private final Cursor<Void> _regionCursor;

    /**
     * Forms the iterator. shapeDim determines the dimension of the shape (or
     * dimensions where iteratation occurs). Sample position is used to determine
     * the constant dimensions (or dimensions greater than shapeDim). Hence, over
     * each iteration, the first shapeDim elements will vary, and the rest remain
     * constant.
     * 
     * @param iterableRegion
     * @param shapeDim
     * @param samplePosition
     */
    public ShapeIterator(IterableRegion<BoolType> iterableRegion, int shapeDim) {
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