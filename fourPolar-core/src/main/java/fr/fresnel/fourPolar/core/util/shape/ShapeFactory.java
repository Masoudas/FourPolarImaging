package fr.fresnel.fourPolar.core.util.shape;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import net.imglib2.roi.geom.GeomMasks;
import net.imglib2.roi.geom.real.WritableBox;
import net.imglib2.roi.geom.real.WritablePolygon2D;

public class ShapeFactory {
    private final Map<ShapeType, ImgLib2Shape> _shapes;

    /**
     * This factory returns only one instance of IShape for each particular shape.
     * To get new instances of IShape, use a new factory.
     */
    public ShapeFactory() {
        _shapes = new HashMap<>();

    }

    /**
     * Generates a box from min to max. The box is closed in the sense that it
     * contains the boundary.
     * 
     * @param min is the minimum coordinate.
     * @param max is the maximum coordinate.
     */
    public IShape closedBox(long[] min, long[] max) {
        Objects.requireNonNull(min, "min should not be null");
        Objects.requireNonNull(max, "max should not be null");

        if (min.length != max.length) {
            throw new IllegalArgumentException("min and max should have the same dimension");
        }

        double[] minCopy = Arrays.stream(min).asDoubleStream().toArray();
        double[] maxCopy = Arrays.stream(max).asDoubleStream().toArray();

        WritableBox box = GeomMasks.closedBox(minCopy, maxCopy);

        ImgLib2Shape shape = this._getUniqueShape(ShapeType.Closed2DBox);
        shape.setImgLib2Shape(min.length, min, box);
        return shape;
    }

    

    public IShape closedPolygon2D(long[] x, long[] y) {
        Objects.requireNonNull(x, "x should not be null");
        Objects.requireNonNull(y, "y should not be null");

        if (x.length != y.length) {
            throw new IllegalArgumentException("x and y should have the same dimension.");
        }

        double[] xPoints =  Arrays.stream(x).asDoubleStream().toArray();
        double[] yPoints = Arrays.stream(y).asDoubleStream().toArray();
        
        WritablePolygon2D polygon2D = GeomMasks.closedPolygon2D(xPoints, yPoints);

        ImgLib2Shape shape = _getUniqueShape(ShapeType.ClosedPolygon2D);
        shape.setImgLib2Shape(2, new long[]{x[0], y[0]}, polygon2D);

        return shape;
    }


    private ImgLib2Shape _getUniqueShape(ShapeType type) {
        if (!_shapes.containsKey(type)) {
            _shapes.put(type, new ImgLib2Shape(type));
        }

        return _shapes.get(type);
    }

}