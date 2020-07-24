package fr.fresnel.fourPolar.core.shape;

import java.util.Arrays;
import java.util.Objects;

import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.util.image.generic.metadata.MetadataUtil;
import net.imglib2.roi.geom.GeomMasks;
import net.imglib2.roi.geom.real.PointMask;

public class ImgLib2PointShape extends ImgLib2Shape implements IPointShape {
    private final long[] _point;

    public static IPointShape create(long[] point, AxisOrder axisOrder) {
        Objects.requireNonNull(point, "point cannot be null.");
        Objects.requireNonNull(axisOrder, "axisOrder cannot be null.");

        if (axisOrder != AxisOrder.NoOrder && !MetadataUtil.numAxisEqualsDimension(axisOrder, point)) {
            throw new IllegalArgumentException("Number of axis must correspond to point dimension.");
        }

        PointMask mask = GeomMasks.pointMask(Arrays.stream(point).asDoubleStream().toArray());
        return new ImgLib2PointShape(mask, axisOrder, point);
    }

    private ImgLib2PointShape(PointMask shape, AxisOrder axisOrder, long[] point) {
        super(shape, axisOrder, point.length);
        this._point = point;
    }

    @Override
    public long[] point() {
        return this._point;
    }

}