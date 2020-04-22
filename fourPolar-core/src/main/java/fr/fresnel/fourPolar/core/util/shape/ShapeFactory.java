package fr.fresnel.fourPolar.core.util.shape;

import java.util.Arrays;
import java.util.Objects;

import net.imglib2.roi.geom.GeomMasks;
import net.imglib2.roi.geom.real.WritableBox;
import net.imglib2.roi.geom.real.WritablePolygon2D;

public class ShapeFactory {
    /**
     * Generates a box from min to max. The box is closed in the sense that it
     * contains the boundary. Note that the dimension of the shape can be less than
     * the space (min = [0, 0, 1], max = [2, 2, 1]);
     * 
     * @param min is the minimum coordinate.
     * @param max is the maximum coordinate.
     * 
     * @throws IllegalArgumentException in case min and max don't have equal
     *                                  dimension or shape dimension is less than 2.
     */
    public IShape closedBox(long[] min, long[] max) {
        Objects.requireNonNull(min, "min should not be null");
        Objects.requireNonNull(max, "max should not be null");

        if (min.length != max.length) {
            throw new IllegalArgumentException("min and max should have the same dimension");
        }

        int shapeDim = 0;
        do {
            shapeDim++;
        } while (shapeDim < min.length && min[shapeDim] != max[shapeDim]);

        if (shapeDim < 2) {
            throw new IllegalArgumentException("Dimension of the box has to be at least two.");
        }

        double[] minCopy = Arrays.stream(min).asDoubleStream().toArray();
        double[] maxCopy = Arrays.stream(max).asDoubleStream().toArray();
        WritableBox box = GeomMasks.closedBox(minCopy, maxCopy);

        ImgLib2Shape shape = new ImgLib2Shape(ShapeType.Closed2DBox, shapeDim, min.length);
        shape.setImgLib2Shape(box);
        return shape;
    }

    public IShape closedPolygon2D(long[] x, long[] y) {
        Objects.requireNonNull(x, "x should not be null");
        Objects.requireNonNull(y, "y should not be null");

        if (x.length != y.length || x.length < 3) {
            throw new IllegalArgumentException(
                "x and y should have the same dimension and greater than three.");
        }

        double[] xPoints = Arrays.stream(x).asDoubleStream().toArray();
        double[] yPoints = Arrays.stream(y).asDoubleStream().toArray();

        WritablePolygon2D polygon2D = GeomMasks.closedPolygon2D(xPoints, yPoints);

        ImgLib2Shape shape = new ImgLib2Shape(ShapeType.ClosedPolygon2D, 2, 2);
        shape.setImgLib2Shape(polygon2D);

        return shape;
    }

    

}