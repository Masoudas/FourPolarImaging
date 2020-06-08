package fr.fresnel.fourPolar.core.util.shape;

import java.util.List;

import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import net.imglib2.RealLocalizable;
import net.imglib2.roi.geom.real.WritablePolygon2D;

public class ImgLib2Polygon2DShape extends ImgLib2Shape implements IPolygon2DShape {
    private final WritablePolygon2D _shape;

    protected ImgLib2Polygon2DShape(int shapeDim, WritablePolygon2D shape, AxisOrder axisOrder, long[] x_vertices,
            long[] y_vertices) {
        super(shapeDim, shape, axisOrder);
        this._shape = shape;
    }

    @Override
    public long[] x_vertices() {
        return this._getVerticesOnDimension(0);
    }

    @Override
    public long[] y_vertices() {
        return this._getVerticesOnDimension(1);
    }

    private long[] _getVerticesOnDimension(int dimension) {
        List<RealLocalizable> vertices = this._shape.vertices();

        long[] x_vertices = new long[this._shapeDim];
        for (int i = 0; i < this._shapeDim; i++) {
            x_vertices[i] = (long) vertices.get(0).getDoublePosition(dimension);
        }
        return x_vertices;
    }

}