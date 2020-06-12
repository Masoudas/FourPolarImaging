package fr.fresnel.fourPolar.core.util.shape;

import java.util.Objects;
import java.util.stream.IntStream;

import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;

public class ShapeFactory {
    /**
     * Generates a box from min to max. The box is closed in the sense that it
     * contains the boundary. Note min and max and axisOrder must have the same
     * dimension. Moreover, 1D box is not acceptable.
     * 
     * 
     * @param min       is the minimum coordinate.
     * @param max       is the maximum coordinate.
     * @param axisOrder is the axis composition associated with the shape.
     * 
     * @throws IllegalArgumentException for any violation of conditions mentioned
     *                                  above.
     */
    public IBoxShape closedBox(long[] min, long[] max, AxisOrder axisOrder) {
        Objects.requireNonNull(min, "min should not be null");
        Objects.requireNonNull(max, "max should not be null");

        if (min.length != max.length) {
            throw new IllegalArgumentException("min and max should have the same dimension");
        }

        if (min.length <= 1) {
            throw new IllegalArgumentException("Number of dimension has to be at least two.");
        }

        if (axisOrder != AxisOrder.NoOrder && axisOrder.numAxis != min.length) {
            throw new IllegalArgumentException("Number of axis must correspond to shape min and max");
        }

        if (IntStream.range(0, max.length).anyMatch((i) -> {
            return min[i] > max[i];
        })) {
            throw new IllegalArgumentException("max should be greater than or equal to min");
        }

        return ImgLib2BoxShape.create(min, max, axisOrder);
    }

    public IPolygon2DShape closedPolygon2D(long[] x, long[] y) {
        Objects.requireNonNull(x, "x should not be null");
        Objects.requireNonNull(y, "y should not be null");

        if (x.length != y.length || x.length < 3) {
            throw new IllegalArgumentException("x and y should have the same dimension and greater than three.");
        }

        return ImgLib2Polygon2DShape.create(x, y);
    }

    public IPointShape point(long[] point, AxisOrder axisOrder) {
        return ImgLib2PointShape.create(point, axisOrder);
    }

}