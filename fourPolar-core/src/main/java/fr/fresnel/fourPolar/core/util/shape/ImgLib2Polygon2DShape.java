package fr.fresnel.fourPolar.core.util.shape;

import java.util.Arrays;
import java.util.Objects;

import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import net.imglib2.roi.geom.GeomMasks;
import net.imglib2.roi.geom.real.WritablePolygon2D;

public class ImgLib2Polygon2DShape extends ImgLib2Shape implements IPolygon2DShape {
    private final long[] _x_vertices;
    private final long[] _y_vertices;

    /**
     * Creates a polygon2D shape, with x and y as coordinates. We expect at least
     * three points to create a polygon, eventough this object can be tricked to
     * create 1D shapes (for example, if all three points are the same).
     * 
     * @param x is the x coordinate of the vertices.
     * @param y is the y coordinate of the vertices.
     * 
     * @throws IllegalArgument exception if at least three points are not supplied.
     * @return
     */
    public static IPolygon2DShape create(long[] x, long[] y) {
        Objects.requireNonNull(x, "x should not be null");
        Objects.requireNonNull(y, "y should not be null");

        _checkXAndYHaveSameDimension(x, y);
        _checkAtLeastThreeVerticesExist(x, y);

        double[] xPoints = Arrays.stream(x).asDoubleStream().toArray();
        double[] yPoints = Arrays.stream(y).asDoubleStream().toArray();

        WritablePolygon2D polygon2D = GeomMasks.closedPolygon2D(xPoints, yPoints);

        return new ImgLib2Polygon2DShape(polygon2D, x, y);
    }

    /**
     * Throws illegalArgumentException if x and y are not equal.
     */
    private static void _checkXAndYHaveSameDimension(long[] x, long[] y) {
        if (x.length != y.length) {
            throw new IllegalArgumentException("x and y vertice coordinates should have equal number of elements.");
        }
    }

    private static void _checkAtLeastThreeVerticesExist(long[] x, long[] y) {
        if (x.length < 3 || y.length < 3) {
            throw new IllegalArgumentException("At least three points should be supplied to create a polygon 2d");
        }
    }

    private ImgLib2Polygon2DShape(WritablePolygon2D shape, long[] x_vertices, long[] y_vertices) {
        // TODO this 2D is not correct. It could be not 2D! 
        super(shape, AxisOrder.XY, 2);
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