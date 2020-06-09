package fr.fresnel.fourPolar.core.util.shape;

import java.util.Arrays;

import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import net.imglib2.roi.geom.GeomMasks;
import net.imglib2.roi.geom.real.WritableBox;

class ImgLib2BoxShape extends ImgLib2Shape implements IBoxShape {
    private final long[] _min;
    private final long[] _max;

    public static IBoxShape create(long[] min, long[] max, AxisOrder axisOrder) {
        int shapeDim = 0;
        while (shapeDim < min.length && min[shapeDim] < max[shapeDim]) {
            shapeDim++;
        }

        double[] minCopy = Arrays.stream(min).asDoubleStream().toArray();
        double[] maxCopy = Arrays.stream(max).asDoubleStream().toArray();
        WritableBox box = GeomMasks.closedBox(minCopy, maxCopy);

        return new ImgLib2BoxShape(min.length, box, axisOrder, min, max);
    }

    private ImgLib2BoxShape(final int shapeDim, WritableBox shape, final AxisOrder axisOrder, long[] min, long[] max) {
        super(shapeDim, shape, axisOrder);
        this._min = min;
        this._max = max;
    }

    @Override
    public long[] min() {
        return this._min;
    }

    @Override
    public long[] max() {
        return this._max;
    }


}