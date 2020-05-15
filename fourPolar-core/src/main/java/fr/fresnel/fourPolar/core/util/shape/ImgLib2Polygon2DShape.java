package fr.fresnel.fourPolar.core.util.shape;

import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import net.imglib2.roi.RealMaskRealInterval;

public class ImgLib2Polygon2DShape extends ImgLib2Shape implements IPolygon2DShape {
    private final long[] _x_vertices;
    private final long[] _y_vertices;

    protected ImgLib2Polygon2DShape(int shapeDim, RealMaskRealInterval shape, AxisOrder axisOrder, long[] x_vertices,
            long[] y_vertices) {
        super(shapeDim, shape, axisOrder);
        _x_vertices = x_vertices;
        _y_vertices = y_vertices;
    }

    @Override
    public long[] x_vertices() {
        return _x_vertices;
    }

    @Override
    public long[] y_vertices() {
        return _y_vertices;
    }

}