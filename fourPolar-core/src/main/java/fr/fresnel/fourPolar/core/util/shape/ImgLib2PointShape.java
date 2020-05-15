package fr.fresnel.fourPolar.core.util.shape;

import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import net.imglib2.roi.RealMaskRealInterval;

public class ImgLib2PointShape extends ImgLib2Shape implements IPointShape {
    private final long[] _point;
    protected ImgLib2PointShape(int shapeDim, RealMaskRealInterval shape, AxisOrder axisOrder, long[] point) {
        super(shapeDim, shape, axisOrder);
        this._point = point;
    }

    @Override
    public long[] point() {
        return _point;
    }
    
}