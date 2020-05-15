package fr.fresnel.fourPolar.core.util.shape;

import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import net.imglib2.roi.RealMaskRealInterval;

class ImgLib2BoxShape extends ImgLib2Shape implements IBoxShape {
    private final long[] _min;
    private final long[] _max;

    public ImgLib2BoxShape(final int shapeDim, RealMaskRealInterval shape, final AxisOrder axisOrder, long[] min,
            long[] max) {
            super(shapeDim, shape, axisOrder);
            _min = min;
            _max = max;
    }

    @Override
    public long[] min() {
        return _min;
    }

    @Override
    public long[] max() {
        return _max;
    }

}