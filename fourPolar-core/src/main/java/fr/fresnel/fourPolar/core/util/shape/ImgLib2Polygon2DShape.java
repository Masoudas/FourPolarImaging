package fr.fresnel.fourPolar.core.util.shape;

import java.util.Arrays;

import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import net.imglib2.roi.geom.GeomMasks;
import net.imglib2.roi.geom.real.WritablePolygon2D;

public class ImgLib2Polygon2DShape extends ImgLib2Shape implements IPolygon2DShape {
    private final long[] _x_vertices;
    private final long[] _y_vertices;

    public static IPolygon2DShape create(long[] x, long[] y) {
        double[] xPoints = Arrays.stream(x).asDoubleStream().toArray();
        double[] yPoints = Arrays.stream(y).asDoubleStream().toArray();

        WritablePolygon2D polygon2D = GeomMasks.closedPolygon2D(xPoints, yPoints);

        return new ImgLib2Polygon2DShape(polygon2D, x, y);
    }

    private ImgLib2Polygon2DShape(WritablePolygon2D shape, long[] x_vertices, long[] y_vertices) {
        super(shape, AxisOrder.XY);
        _x_vertices = x_vertices;
        _y_vertices = y_vertices;
    }

    @Override
    public long[] x_vertices() {
        return this._x_vertices;
    }

    @Override
    public long[] y_vertices() {
        return this._y_vertices;
    }

}