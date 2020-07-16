package fr.fresnel.fourPolar.core.util.shape;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.IntStream;

import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.util.image.metadata.MetadataUtil;
import net.imglib2.roi.geom.GeomMasks;
import net.imglib2.roi.geom.real.WritableBox;

class ImgLib2BoxShape extends ImgLib2Shape implements IBoxShape {
    private final long[] _min;
    private final long[] _max;

    public static IBoxShape create(long[] min, long[] max, AxisOrder axisOrder) {
        Objects.requireNonNull(min, "min should not be null");
        Objects.requireNonNull(max, "max should not be null");

        _checkMinAndMaxHaveEqualDimension(min, max);
        _checkBoxIsAtLeastTwoDimensional(min, max);
        _checkMaxIsGreaterThanEqualMin(min, max);
        _checkMinDimensionEqualsNumAxis(min, axisOrder);

        double[] minCopy = Arrays.stream(min).asDoubleStream().toArray();
        double[] maxCopy = Arrays.stream(max).asDoubleStream().toArray();
        WritableBox box = GeomMasks.closedBox(minCopy, maxCopy);

        return new ImgLib2BoxShape(box, axisOrder, min, max);
    }

    private static void _checkMinDimensionEqualsNumAxis(long[] min, AxisOrder axisOrder) {
        if (axisOrder != AxisOrder.NoOrder && !MetadataUtil.numAxisEqualsDimension(axisOrder, min)) {
            throw new IllegalArgumentException("Number of axis must correspond to shape min and max");
        }
    }

    private static void _checkMaxIsGreaterThanEqualMin(long[] min, long[] max) {
        if (IntStream.range(0, max.length).anyMatch((i) -> {
            return min[i] > max[i];
        })) {
            throw new IllegalArgumentException("max should be greater than or equal to min");
        }
    }

    private static void _checkBoxIsAtLeastTwoDimensional(long[] min, long[] max) {
        if (min.length <= 1) {
            throw new IllegalArgumentException("Box dimension must be at least two");
        }

        long dim = IntStream.range(0, min.length).filter((i)->(min[i] < max[i])).count();
        
        if (dim < 2 ){
            throw new IllegalArgumentException("Box dimension must be at least two");
        }
    }

    private static void _checkMinAndMaxHaveEqualDimension(long[] min, long[] max) {
        if (min.length != max.length) {
            throw new IllegalArgumentException("min and max should have the same dimension");
        }
    }

    private ImgLib2BoxShape(WritableBox shape, final AxisOrder axisOrder, long[] min, long[] max) {
        super(shape, axisOrder);
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