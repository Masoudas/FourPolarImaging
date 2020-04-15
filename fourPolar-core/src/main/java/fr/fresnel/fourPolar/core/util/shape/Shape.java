package fr.fresnel.fourPolar.core.util.shape;

import net.imglib2.roi.IterableRegion;
import net.imglib2.roi.Masks;
import net.imglib2.roi.RealMaskRealInterval;
import net.imglib2.roi.Regions;
import net.imglib2.type.logic.BoolType;
import net.imglib2.util.Intervals;
import net.imglib2.view.Views;

/**
 * Assign various library shapes to our shape. Class should not be implemented for
 * other libraries. Use set methods.
 */
class Shape implements IShape {
    private IShapeIteraror _iterator = null;
    private ShapeType _type;
    private int _shapeDim = 0;
    private long[] _samplePosition = null;

    /**
     * Construct shape be defining it's dimension and type. Set set methods.
     * @param type
     * @param shapeDim
     */
    public Shape(final ShapeType type) {
        this._type = type;
    }

    @Override
    public IShapeIteraror getIterator() {
        return this._iterator;
    }

    @Override
    public ShapeType getType() {
        return this._type;
    }

    @Override
    public int numDimension() {
        return this._samplePosition.length;
    }

    @Override
    public int shapeDim() {
        return this._shapeDim;
    }

    /**
     * Construct the shape, using ImgLib2 ROI. @See RealMaskRealInterval.
     * 
     * @param type             is the associated shape type.
     * @param shapeDim         is the dimension of the shape (two for a 2DBox for
     *                         example).
     * @param samplePosition   is one arbitrary position associated with the shape.
     *                         Dimensions higher than shapeDim will be read from
     *                         this value.
     * @param maskRealInterval is the associated ImgLib2 shape interval.
     */
    public void setImgLib2Shape(int shapeDim, long[] samplePosition,
            RealMaskRealInterval maskRealInterval) {
        IterableRegion<BoolType> iterableRegion = Regions
                .iterable(Views.interval(Views.raster(Masks.toRealRandomAccessible(maskRealInterval)),
                        Intervals.largestContainedInterval(maskRealInterval)));

        this._setIterator(new ShapeIterator(iterableRegion, shapeDim, samplePosition));
        this._setSamplePosition(samplePosition);
    }

    private void _setSamplePosition(long[] samplePosition) {
        this._samplePosition = samplePosition;
    }
    
    public void _setIterator(IShapeIteraror iterator) {
        this._iterator = iterator;
    }

}