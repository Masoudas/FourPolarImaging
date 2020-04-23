package fr.fresnel.fourPolar.core.util.shape;

import net.imglib2.roi.IterableRegion;
import net.imglib2.roi.Masks;
import net.imglib2.roi.RealMaskRealInterval;
import net.imglib2.roi.Regions;
import net.imglib2.roi.geom.GeomMasks;
import net.imglib2.roi.geom.real.WritablePointMask;
import net.imglib2.type.logic.BoolType;
import net.imglib2.util.Intervals;
import net.imglib2.view.Views;

/**
 * Assign various library shapes to our shape. Class should not be implemented
 * for other libraries. Use set methods.
 */
class ImgLib2Shape implements IShape {
    private RealMaskRealInterval _maskRealInterval;
    private final ShapeType _type;
    private final int _shapeDim;
    private final int _spaceDim;

    /**
     * A point mask instance to check whether a point is inside the shape.
     */
    final private WritablePointMask _pointMask;

    /**
     * Construct the shape, using ImgLib2 ROI. @See RealMaskRealInterval.
     * 
     * @param shapeType is the associated shape type.
     * @param shapeDim  is the dimension of the shape (two for a 2DBox for example).
     * @param spaceDim  This is the dimension of the space over which the shape is
     *                  defined.
     * 
     */
    public ImgLib2Shape(final ShapeType shapeType, final int shapeDim, final int spaceDim) {
        this._type = shapeType;
        this._shapeDim = shapeDim;
        this._spaceDim = spaceDim;
        this._pointMask = GeomMasks.pointMask(new double[spaceDim]);
    }

    public void setImgLib2Shape(final RealMaskRealInterval maskRealInterval) {
        this._maskRealInterval = maskRealInterval;
    }

    @Override
    public IShapeIterator getIterator() {
        final IterableRegion<BoolType> iterableRegion = Regions
                .iterable(Views.interval(Views.raster(Masks.toRealRandomAccessible(this._maskRealInterval)),
                        Intervals.largestContainedInterval(this._maskRealInterval)));

        return new ShapeIterator(iterableRegion, this._spaceDim);
    }

    @Override
    public ShapeType getType() {
        return this._type;
    }

    @Override
    public int spaceDim() {
        return this._spaceDim;
    }

    @Override
    public int shapeDim() {
        return this._shapeDim;
    }

    public RealMaskRealInterval getRealMaskRealInterval() {
        return this._maskRealInterval;
    }

    @Override
    public boolean isInside(long[] point) {
        if (point.length < this._spaceDim) {
            return false;
        }

        this._pointMask.setPosition(point);
        return this._maskRealInterval.test(this._pointMask);
    }

}