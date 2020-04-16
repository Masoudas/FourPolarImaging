package fr.fresnel.fourPolar.core.util.shape;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import net.imglib2.realtransform.AffineTransform2D;
import net.imglib2.realtransform.AffineTransform3D;
import net.imglib2.roi.RealMaskRealInterval;
import net.imglib2.roi.geom.GeomMasks;
import net.imglib2.roi.geom.real.WritableBox;
import net.imglib2.roi.geom.real.WritablePolygon2D;

public class ShapeFactory {
    private final Map<ShapeType, Shape> _shapes;

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

        Shape shape = this._getUniqueShape(ShapeType.Closed2DBox);
        shape.setImgLib2Shape(min.length, min, box);
        return shape;
    }

    /**
     * Generates a 2D box, placed at the center, with xlen/2 and ylen/2 over both
     * sides of the center, rotated by rotation angle. Note that center can be
     * multi-dimensional. The box is closed in the sense that it contains the
     * boundary.
     * 
     * @param center        is the center of the box.
     * @param xlen          is the horizontal length of rectangle (half on each side
     *                      of rectangle).
     * @param ylen          is the vertical length of rectangle (half on each side
     *                      of rectangle).
     * @param rotationAngle is the rotation of the rectangle in Radian.
     * @return
     */
    public IShape closed2DBox(long[] center, long xlen, long ylen, double rotationAngle) {
        Objects.requireNonNull(center, "center should not be null");

        if (center.length < 2) {
            throw new IllegalArgumentException("Center dimension should be greater than one.");
        }

        if (xlen < 1 || ylen < 1) {
            throw new IllegalArgumentException("xlen and ylen should be greater than one.");
        }

        long[] min = { -xlen / 2, -ylen / 2 };

        WritableBox box = GeomMasks.closedBox(new double[] { min[0], min[1] }, new double[] { xlen / 2, ylen / 2 });
        AffineTransform2D transform2d = _generate2DTransform(center, rotationAngle);
        RealMaskRealInterval rotatedTranslatedBox = box.transform(transform2d);

        Shape shape = this._getUniqueShape(ShapeType.Closed2DBox);
        shape.setImgLib2Shape(2, min, rotatedTranslatedBox);

        return shape;
    }

    /**
     * Generate a 3d box placed at the center, with xlen/2, ylen/2, zlen/2 over each
     * side of the center. The box is rotated around the z-axis by z_rotation and
     * around x-axis by x_rotation. The box is closed in the sense that it contains
     * the boundary.
     * <p>
     * VERY IMPORTANT NOTE: The order of rotation would be x and then z.
     * 
     * @param center     is the center of the box.
     * @param xlen       is the len in the x-direction.
     * @param ylen       is the len in the y-direction.
     * @param zlen       is the len in the z-direction.
     * @param z_rotation is the rotation around the z-axis.
     * @param x_rotation is the rotation around the x-axis.
     */
    public IShape closed3DBox(long[] center, long xlen, long ylen, long zlen, double x_rotation, double z_rotation) {
        Objects.requireNonNull(center, "center should not be null");

        if (center.length < 3) {
            throw new IllegalArgumentException("Center dimension should be greater than two.");
        }

        if (xlen < 1 || ylen < 1 || zlen < 1) {
            throw new IllegalArgumentException("xlen and ylen should be greater than equal one.");
        }

        long[] min = { -xlen / 2, -ylen / 2, -zlen / 2 };

        WritableBox box = GeomMasks.closedBox(new double[] { min[0], min[1], min[2] },
                new double[] { xlen / 2, ylen / 2, zlen / 2 });

        AffineTransform3D transform3d = _generate3DTransform(center, x_rotation, z_rotation);
        RealMaskRealInterval rotatedTranslatedBox = box.transform(transform3d);

        Shape shape = this._getUniqueShape(ShapeType.Closed3DBox);
        shape.setImgLib2Shape(3, min, rotatedTranslatedBox);
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

        Shape shape = _getUniqueShape(ShapeType.ClosedPolygon2D);
        shape.setImgLib2Shape(2, new long[]{x[0], y[0]}, polygon2D);

        return shape;
    }

    /**
     * Generate 2D transform from the given translation and rotation angle.
     */
    private AffineTransform2D _generate2DTransform(long[] translation, double rotationAngle) {
        final AffineTransform2D transform2d = new AffineTransform2D();
        transform2d.rotate(-rotationAngle);

        double[] rotatedTranslation = { -translation[0], -translation[1] };
        transform2d.apply(rotatedTranslation, rotatedTranslation);
        transform2d.translate(rotatedTranslation);

        return transform2d;
    }

    private AffineTransform3D _generate3DTransform(long[] translation, double x_rotation, double z_rotation) {
        final AffineTransform3D transform3d = new AffineTransform3D();

        // In the following order, x rotation is applied first, and then z rotation.
        transform3d.rotate(2, -z_rotation);
        transform3d.rotate(0, -x_rotation);
        
        double[] rotatedTranslation = { -translation[0], -translation[1], -translation[2] };
        transform3d.apply(rotatedTranslation, rotatedTranslation);
        
        transform3d.translate(rotatedTranslation);
        
        return transform3d;

    }

    private Shape _getUniqueShape(ShapeType type) {
        if (!_shapes.containsKey(type)) {
            _shapes.put(type, new Shape(type));
        }

        return _shapes.get(type);
    }

}