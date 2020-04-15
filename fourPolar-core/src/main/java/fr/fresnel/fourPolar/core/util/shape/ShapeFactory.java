package fr.fresnel.fourPolar.core.util.shape;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import net.imglib2.realtransform.AffineTransform;
import net.imglib2.realtransform.AffineTransform2D;
import net.imglib2.realtransform.AffineTransform3D;
import net.imglib2.roi.IterableRegion;
import net.imglib2.roi.Masks;
import net.imglib2.roi.RealMaskRealInterval;
import net.imglib2.roi.Regions;
import net.imglib2.roi.geom.GeomMasks;
import net.imglib2.roi.geom.real.WritableBox;
import net.imglib2.type.logic.BoolType;
import net.imglib2.util.Intervals;
import net.imglib2.view.Views;

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
     * Generates a 2d box from min to max. Both coordinates can be
     * multi-dimensional, but third dimension and above should be equal (min higher
     * dimensions will be taken). The box is closed in the sense that it contains
     * the boundary.
     * 
     * @param min is the minimum coordinate.
     * @param max is the maximum coordinate.
     */
    public IShape close2DBox(long[] min, long[] max) {
        Objects.requireNonNull(min, "min should not be null");
        Objects.requireNonNull(max, "max should not be null");

        if (min.length != max.length || min.length < 2 || max.length < 2) {
            throw new IllegalArgumentException("min and max should have the same dimension and greater than one.");
        }

        WritableBox box = GeomMasks.closedBox(new double[] { min[0], min[1] }, new double[] { max[0], max[1] });

        Shape shape = this._getUniqueShape(ShapeType.Close2DBox);
        shape.setImgLib2Shape(2, min, box);
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
    public IShape close2DBox(long[] center, long xlen, long ylen, double rotationAngle) {
        Objects.requireNonNull(center, "center should not be null");

        if (center.length < 2) {
            throw new IllegalArgumentException("Center dimension should be greater than one.");
        }

        if (xlen < 1 || ylen < 1) {
            throw new IllegalArgumentException("xlen and ylen should be greater than one.");
        }

        long[] min = { -xlen / 2, -ylen / 2 };
        long[] max = { xlen / 2 + 1, ylen / 2 + 1 };

        WritableBox box = GeomMasks.closedBox(new double[] { min[0], min[1] }, new double[] { max[0], max[1] });
        AffineTransform2D transform2d = _generate2DTransform(center, rotationAngle);
        RealMaskRealInterval rotatedTranslatedBox = box.transform(transform2d);

        Shape shape = this._getUniqueShape(ShapeType.Close2DBox);
        shape.setImgLib2Shape(2, min, rotatedTranslatedBox);

        return shape;
    }

    /**
     * Generates a 3d box from min to max. Both coordinates can be
     * multi-dimensional, but fourth dimension and above should be equal (min higher
     * dimensions will be taken). The box is closed in the sense that it contains
     * the boundary.
     * 
     * @param min is the minimum coordinate.
     * @param max is the maximum coordinate.
     */

    public IShape close3DBox(long[] min, long[] max) {
        Objects.requireNonNull(min, "min should not be null");
        Objects.requireNonNull(max, "max should not be null");

        if (min.length != max.length || min.length < 3 || max.length < 3) {
            throw new IllegalArgumentException("min and max should have the same dimension and greater than two.");
        }

        WritableBox box = GeomMasks.closedBox(new double[] { min[0], min[1], min[2] },
                new double[] { max[0], max[1], max[2] });

        Shape shape = this._getUniqueShape(ShapeType.Close3DBox);
        shape.setImgLib2Shape(3, min, box);
        return shape;

    }

    /**
     * Generate a 3d box placed at the center, with xlen/2, ylen/2, zlen/2 over each
     * side of the center. The box is rotated around the z-axis by z_rotation and
     * around x-axis by x_rotation. The box is closed in the sense that it contains
     * the boundary.
     * 
     * @param center     is the center of the box.
     * @param xlen       is the len in the x-direction.
     * @param ylen       is the len in the y-direction.
     * @param zlen       is the len in the z-direction.
     * @param z_rotation is the rotation around the z-axis.
     * @param x_rotation is the rotation around the x-axis.
     */
    public IShape close3DBox(long[] center, long xlen, long ylen, long zlen, double z_rotation, double x_rotation) {
        Objects.requireNonNull(center, "center should not be null");

        if (center.length < 3) {
            throw new IllegalArgumentException("Center dimension should be greater than two.");
        }

        if (xlen < 1 || ylen < 1 || zlen < 1) {
            throw new IllegalArgumentException("xlen and ylen should be greater than equal one.");
        }

        long[] min = { -xlen / 2, -ylen / 2, -zlen / 2 };
        long[] max = { xlen / 2 + 1, ylen / 2 + 1, zlen / 2 + 1 };

        WritableBox box = GeomMasks.closedBox(new double[] { min[0], min[1], min[2] },
                new double[] { max[0], max[1], max[2] });
        AffineTransform3D transform3d = _generate3DTransform(center, z_rotation, x_rotation);
        RealMaskRealInterval rotatedTranslatedBox = box.transform(transform3d);

        Shape shape = this._getUniqueShape(ShapeType.Close3DBox);
        shape.setImgLib2Shape(3, min, rotatedTranslatedBox);
        return shape;
    }

    /**
     * Generate 2D transform from the given translation and rotation angle.
     */
    private AffineTransform2D _generate2DTransform(long[] translation, double rotationAngle) {
        final AffineTransform2D transform2d = new AffineTransform2D();
        transform2d.rotate(-rotationAngle);

        final double[] rotatedTranslation = { translation[0], translation[1] };
        transform2d.apply(rotatedTranslation, rotatedTranslation);
        transform2d.translate(rotatedTranslation);

        return transform2d;
    }

    private AffineTransform3D _generate3DTransform(long[] translation, double z_rotation, double x_rotation) {
        final AffineTransform3D transform3d = new AffineTransform3D();
        transform3d.rotate(0, -z_rotation);
        transform3d.rotate(3, -x_rotation);

        final double[] rotatedTranslation = { translation[0], translation[1], translation[2] };
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