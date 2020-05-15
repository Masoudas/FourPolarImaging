package fr.fresnel.fourPolar.core.util.shape;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.IntStream;

import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import net.imglib2.roi.geom.GeomMasks;
import net.imglib2.roi.geom.real.PointMask;
import net.imglib2.roi.geom.real.WritableBox;
import net.imglib2.roi.geom.real.WritablePolygon2D;

public class ShapeFactory {
    /**
     * Generates a box from min to max. The box is closed in the sense that it
     * contains the boundary. Note that the dimension of the shape can be less than
     * the space (min = [0, 0, 1], max = [2, 2, 1]);
     * 
     * @param min       is the minimum coordinate.
     * @param max       is the maximum coordinate.
     * @param axisOrder is the axis composition associated with the shape.
     * 
     * @throws IllegalArgumentException in case min and max don't have equal
     *                                  dimension or shape dimension is less than 2.
     */
    public IBoxShape closedBox(long[] min, long[] max, AxisOrder axisOrder) {
        Objects.requireNonNull(min, "min should not be null");
        Objects.requireNonNull(max, "max should not be null");

        if (min.length != max.length) {
            throw new IllegalArgumentException("min and max should have the same dimension");
        }

        if (axisOrder != AxisOrder.NoOrder && AxisOrder.getNumDefinedAxis(axisOrder) != min.length) {
            throw new IllegalArgumentException("Number of axis must correspond to shape min and max");
        }

        if (IntStream.range(0, max.length).anyMatch((i) -> {
            return min[i] > max[i];
        })) {
            throw new IllegalArgumentException("max should be greater than or equal to min");
        }

        int shapeDim = 0;
        while (shapeDim < min.length && min[shapeDim] < max[shapeDim]) {
            shapeDim++;
        }

        if (shapeDim < 2) {
            throw new IllegalArgumentException("Dimension of the box has to be at least two.");
        }

        double[] minCopy = Arrays.stream(min).asDoubleStream().toArray();
        double[] maxCopy = Arrays.stream(max).asDoubleStream().toArray();
        WritableBox box = GeomMasks.closedBox(minCopy, maxCopy);

        return new ImgLib2BoxShape(min.length, box, axisOrder, min, max);
    }

    public IPolygon2DShape closedPolygon2D(long[] x, long[] y) {
        Objects.requireNonNull(x, "x should not be null");
        Objects.requireNonNull(y, "y should not be null");

        if (x.length != y.length || x.length < 3) {
            throw new IllegalArgumentException("x and y should have the same dimension and greater than three.");
        }

        double[] xPoints = Arrays.stream(x).asDoubleStream().toArray();
        double[] yPoints = Arrays.stream(y).asDoubleStream().toArray();

        WritablePolygon2D polygon2D = GeomMasks.closedPolygon2D(xPoints, yPoints);

        return new ImgLib2Polygon2DShape(2, polygon2D, AxisOrder.XY, x, y);
    }

    public IPointShape point(long[] point, AxisOrder axisOrder) {
        Objects.requireNonNull(point, "location cannot be null.");

        if (axisOrder != AxisOrder.NoOrder && AxisOrder.getNumDefinedAxis(axisOrder) != point.length) {
            throw new IllegalArgumentException("Number of axis must correspond to point dimension.");
        }

        PointMask mask = GeomMasks.pointMask(Arrays.stream(point).asDoubleStream().toArray());
        return new ImgLib2PointShape(1, mask, axisOrder, point);
    }

}