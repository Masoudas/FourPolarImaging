package fr.fresnel.fourPolar.core.util.shape;

import fr.fresnel.fourPolar.core.physics.axis.AxisOrder;
import net.imglib2.Cursor;
import net.imglib2.roi.IterableRegion;
import net.imglib2.type.logic.BoolType;

/**
 * An iterator to iterator over discrete coordinate points.
 */
class ShapeIterator implements IShapeIterator {
    private final long[] _position;
    private final Cursor<Void> _regionCursor;

    /**
     * Form the iterator, corresponding to space properties and shape.
     * @param iterableRegion is the iterable of ImgLib2.
     * @param axisOrder is the space axis associated via 
     */
    public ShapeIterator(IterableRegion<BoolType> iterableRegion, AxisOrder axisOrder) {
        this._regionCursor = iterableRegion.cursor();
        this._position = new long[AxisOrder.getNumAxis(axisOrder)];
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