package fr.fresnel.fourPolar.core.util.shape;

import java.util.Arrays;
import java.util.List;

import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import net.imglib2.RealLocalizable;
import net.imglib2.roi.geom.GeomMasks;
import net.imglib2.roi.geom.real.WritablePolygon2D;

public class ImgLib2Polygon2DShape extends ImgLib2Shape implements IPolygon2DShape {
    public static IPolygon2DShape create(long[] x, long[] y) {
        double[] xPoints = Arrays.stream(x).asDoubleStream().toArray();
        double[] yPoints = Arrays.stream(y).asDoubleStream().toArray();

        WritablePolygon2D polygon2D = GeomMasks.closedPolygon2D(xPoints, yPoints);

        return new ImgLib2Polygon2DShape(polygon2D, x, y);
    }

    private ImgLib2Polygon2DShape(WritablePolygon2D shape, long[] x_vertices, long[] y_vertices) {
        super(2, shape, AxisOrder.XY);
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
        List<RealLocalizable> vertices = this._getShapeAsPolygon2d().vertices();

        long[] x_vertices = new long[this._shapeDim];
        for (int i = 0; i < this._shapeDim; i++) {
            x_vertices[i] = (long) vertices.get(0).getDoublePosition(dimension);
        }
        return x_vertices;
    }

    private WritablePolygon2D _getShapeAsPolygon2d() {
        return (WritablePolygon2D) super.getImgLib2Shape();
    }

}