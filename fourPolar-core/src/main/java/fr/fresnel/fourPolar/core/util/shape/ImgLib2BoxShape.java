package fr.fresnel.fourPolar.core.util.shape;

import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import net.imglib2.roi.geom.real.WritableBox;

class ImgLib2BoxShape extends ImgLib2Shape implements IBoxShape {
    private final WritableBox _shape;

    public ImgLib2BoxShape(final int shapeDim, WritableBox shape, final AxisOrder axisOrder, long[] min,
            long[] max) {
        super(shapeDim, shape, axisOrder);
        this._shape = shape;
    }



    @Override
    public long[] min() {
        long[] min = new long[this._shapeDim];
        for (int i = 0; i < min.length; i++) {
            min[i]= this._getMinDimension(i);
        }
        return min;
    }

    @Override
    public long[] max() {
        long[] max = new long[this._shapeDim];
        for (int i = 0; i < max.length; i++) {
            max[i]= this._getMaxDimension(i);
        }
        return max;
    }


    private long _getMinDimension(int d){
        return (long)(this._shape.center().getDoublePosition(d) - this._shape.sideLength(d));
    }

    private long _getMaxDimension(int d){
        return (long)(this._shape.center().getDoublePosition(d) + this._shape.sideLength(d));
    }

}