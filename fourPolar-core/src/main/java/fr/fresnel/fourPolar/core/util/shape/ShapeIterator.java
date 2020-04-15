package fr.fresnel.fourPolar.core.util.shape;

import java.util.Arrays;

import net.imglib2.Cursor;
import net.imglib2.roi.IterableRegion;
import net.imglib2.type.logic.BoolType;

class ShapeIterator implements IShapeIteraror {
    private final long[] _position;
    private final Cursor<Void> _regionCursor;
    private final long[] _varyingPosition;
    private final int _shapeDim;

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
    public ShapeIterator(IterableRegion<BoolType> iterableRegion, int shapeDim, long[] samplePosition) {
        this._regionCursor = iterableRegion.cursor();
        this._position = Arrays.copyOf(samplePosition, samplePosition.length);
        this._varyingPosition = new long[shapeDim];
        this._shapeDim = shapeDim;
    }

    @Override
    public boolean hasNext() {
        return _regionCursor.hasNext();
    }

    @Override
    public long[] next() {
        _regionCursor.next();
        this._regionCursor.localize(this._varyingPosition);

        System.arraycopy(this._varyingPosition, 0, this._position, 0, this._shapeDim);
        return this._position;
    }

    @Override
    public void reset() {
        _regionCursor.reset();
    }

    
    
}