package fr.fresnel.fourPolar.core.util.shape;

import java.util.Arrays;

import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import net.imglib2.roi.geom.real.PointMask;

public class ImgLib2PointShape extends ImgLib2Shape implements IPointShape {
    private final PointMask _shape;
    protected ImgLib2PointShape(int shapeDim, PointMask shape, AxisOrder axisOrder, long[] point) {
        super(shapeDim, shape, axisOrder);
        _shape = shape;
    }

    @Override
    public long[] point() {
        double[]  position = new double[this._shapeDim];
        this._shape.localize(position);

        return Arrays.stream(position).mapToLong((t)->(long)t).toArray();
    }

    @Override
    protected void _transformShape() {
        super._transformShape();

    }
    
}