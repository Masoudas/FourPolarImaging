package fr.fresnel.fourPolar.core.util.shape;

import java.util.Arrays;

import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import net.imglib2.roi.geom.GeomMasks;
import net.imglib2.roi.geom.real.WritableBox;

class ImgLib2BoxShape extends ImgLib2Shape implements IBoxShape {
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
    }

    @Override
    public long[] min() {
        long[] min = new long[this._shapeDim];
        for (int i = 0; i < min.length; i++) {
            min[i] = this._getMinDimension(i);
        }
        return min;
    }

    @Override
    public long[] max() {
        long[] max = new long[this._shapeDim];
        for (int i = 0; i < max.length; i++) {
            max[i] = this._getMaxDimension(i);
        }
        return max;
    }

    private long _getMinDimension(int d) {
        WritableBox box = this._getShapeAsBox();
        return (long) (box.center().getDoublePosition(d) - box.sideLength(d));
    }

    private long _getMaxDimension(int d) {
        WritableBox box = this._getShapeAsBox();
        return (long) (box.center().getDoublePosition(d) + box.sideLength(d));
    }

    private WritableBox _getShapeAsBox() {
        return (WritableBox) super.getImgLib2Shape();
    }

}