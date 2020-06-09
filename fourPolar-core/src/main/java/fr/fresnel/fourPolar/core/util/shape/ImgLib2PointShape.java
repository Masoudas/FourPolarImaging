package fr.fresnel.fourPolar.core.util.shape;

import java.util.Arrays;

import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import net.imglib2.roi.geom.GeomMasks;
import net.imglib2.roi.geom.real.PointMask;

public class ImgLib2PointShape extends ImgLib2Shape implements IPointShape {
    public static IPointShape create(long[] point, AxisOrder axisOrder) {
        PointMask mask = GeomMasks.pointMask(Arrays.stream(point).asDoubleStream().toArray());
        return new ImgLib2PointShape(1, mask, axisOrder, point);
    }

    private ImgLib2PointShape(int shapeDim, PointMask shape, AxisOrder axisOrder, long[] point) {
        super(shapeDim, shape, axisOrder);
        _shape = shape;
    }

    @Override
    public long[] point() {
        double[] position = new double[this._shapeDim];
        this._getShapeAsPoint().localize(position);

        return Arrays.stream(position).mapToLong((t) -> (long) t).toArray();
    }

    @Override
    protected void _transformShape() {
        super._transformShape();

    }

    private PointMask _getShapeAsPoint() {
        return (PointMask) super.getImgLib2Shape();
    }

}