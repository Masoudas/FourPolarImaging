package fr.fresnel.fourPolar.core.util.shape;

import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import net.imglib2.roi.RealMaskRealInterval;

class ImgLib2BoxShape extends ImgLib2Shape implements IBoxShape {
    private final long[] _originalMin;
    private final long[] _originalMax;

    private long[] _min;
    private long[] _max;

    public ImgLib2BoxShape(final int shapeDim, RealMaskRealInterval shape, final AxisOrder axisOrder, long[] min,
            long[] max) {
        super(shapeDim, shape, axisOrder);
        _originalMin = min;
        _originalMax = max;
    }

    @Override
    protected void _transformShape() {
        super._transformShape();
        this._transformMinPoint();
        this._transformMaxPoint();
    }

    @Override
    public void resetToOriginalShape() {
        super.resetToOriginalShape();
        this._setMax(this._originalMax);
        this._setMin(this._originalMin);
    }

    @Override
    public long[] min() {
        return _min.clone();
    }

    @Override
    public long[] max() {
        return _max.clone();
    }

    private void _setMin(long[] min) {
        this._min = min;
    }

    private void _setMax(long[] max) {
        this._max = max;
    }

    private void _transformMinPoint() {
        long[] newMin = ImgLib2ShapeUtils.applyAffineToLong(this._appliedAffineTransform, this.min());
        this._setMin(newMin);
    }

    private void _transformMaxPoint() {
        long[] newMax = ImgLib2ShapeUtils.applyAffineToLong(this._appliedAffineTransform, this.max());
        this._setMax(newMax);
    }
}