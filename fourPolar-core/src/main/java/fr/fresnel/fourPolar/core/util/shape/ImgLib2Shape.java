package fr.fresnel.fourPolar.core.util.shape;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.Objects;

import fr.fresnel.fourPolar.core.physics.axis.AxisOrder;
import net.imglib2.realtransform.AffineTransform;
import net.imglib2.realtransform.AffineTransform2D;
import net.imglib2.realtransform.AffineTransform3D;
import net.imglib2.roi.IterableRegion;
import net.imglib2.roi.Masks;
import net.imglib2.roi.RealMaskRealInterval;
import net.imglib2.roi.Regions;
import net.imglib2.roi.geom.GeomMasks;
import net.imglib2.roi.geom.real.WritablePointMask;
import net.imglib2.type.logic.BoolType;
import net.imglib2.util.Intervals;
import net.imglib2.view.Views;

/**
 * Assign various library shapes to our shape. Class should not be implemented
 * for other libraries. Use set methods.
 */
class ImgLib2Shape implements IShape {
    final private RealMaskRealInterval _originalShape;
    private RealMaskRealInterval _shape;

    private final ShapeType _type;
    private final int _shapeDim;
    private final int _spaceDim;
    private final AxisOrder _axisOrder;

    /**
     * A point mask instance to check whether a point is inside the shape.
     */
    final private WritablePointMask _pointMask;

    /**
     * Construct the shape, using ImgLib2 ROI. @See RealMaskRealInterval.
     * 
     * @param shapeType is the associated shape type.
     * @param shapeDim  is the dimension of the shape (two for a 2DBox for example).
     * @param spaceDim  This is the dimension of the space over which the shape is
     *                  defined.
     * 
     */
    public ImgLib2Shape(final ShapeType shapeType, final int shapeDim, final int spaceDim, RealMaskRealInterval shape,
            final AxisOrder axisOrder) {
        this._type = shapeType;
        this._shapeDim = shapeDim;
        this._spaceDim = spaceDim;
        this._pointMask = GeomMasks.pointMask(new double[spaceDim]);
        this._originalShape = shape;
        this._shape = shape.and(shape); // Just a bogus operation to get a new copy of the shape.
        this._axisOrder = axisOrder;
    }

    @Override
    public IShapeIterator getIterator() {
        final IterableRegion<BoolType> iterableRegion = Regions
                .iterable(Views.interval(Views.raster(Masks.toRealRandomAccessible(this._shape)),
                        Intervals.largestContainedInterval(this._shape)));

        return new ShapeIterator(iterableRegion, this._spaceDim);
    }

    @Override
    public ShapeType getType() {
        return this._type;
    }

    @Override
    public int spaceDim() {
        return this._spaceDim;
    }

    @Override
    public int shapeDim() {
        return this._shapeDim;
    }

    public RealMaskRealInterval getImgLib2Shape() {
        return this._shape;
    }

    @Override
    public boolean isInside(long[] point) {
        if (point.length != this._spaceDim) {
            throw new IllegalArgumentException("The given point does not have same number of axis as shape.");
        }

        this._pointMask.setPosition(point);
        return this._shape.test(this._pointMask);
    }

    @Override
    public void and(IShape shape) {
        Objects.requireNonNull(shape, "shape should not be null.");

        if (shape.spaceDim() != this.spaceDim()) {
            throw new IllegalArgumentException("To and two shapes, they must have the same space dimension");
        }

        ImgLib2Shape shapeRef = (ImgLib2Shape) shape;
        this._shape = this._shape.and(shapeRef.getImgLib2Shape());
    }

    @Override
    public void resetToOriginalShape() {
        this._shape = this._originalShape;
    }

    @Override
    public void rotate3D(double angle1, double angle2, double angle3, int[] axis) {
        int z_axis = AxisOrder.getZAxis(this._axisOrder);
        if (AxisOrder.getZAxis(this._axisOrder) < 2){
            throw new IllegalArgumentException("Impossible to rotate 3D because no z-axis exists.");
        }
        if (axis.length != 3) {
            throw new IllegalArgumentException("Rotation angles and axis should be three");
        }

        IntSummaryStatistics stats = Arrays.stream(axis).summaryStatistics();
        if (stats.getMin() < 0 || stats.getMax() > 3 || stats.getSum() != 3) {
            throw new IllegalArgumentException(
                    "Rotation axis must be between 0 and 2 dimension and Repetition is not allowed.");
        }

        AffineTransform3D affine3D = new AffineTransform3D();
        affine3D.rotate(axis[2], -angle3);
        affine3D.rotate(axis[1], -angle2);
        affine3D.rotate(axis[0], -angle1);

        int[] rowsToFill = {0, 1, z_axis}; // Row, columns to be filled in the affine transform.
        AffineTransform fTransform = new AffineTransform(_spaceDim);
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                fTransform.set(affine3D.get(row, column), rowsToFill[row], rowsToFill[column]);
            }
        }

        this._shape = this._shape.transform(fTransform);

    }

    @Override
    public void translate(long[] translation) {
        if (translation.length != this._spaceDim) {
            throw new IllegalArgumentException("Translation dimension must equal shape space dimension.");
        }

        AffineTransform fTransform = new AffineTransform(_spaceDim);
        // Set translation
        for (int row = 0; row < this._spaceDim; row++) {
            fTransform.set(-translation[row], row, this._spaceDim);
        }

        this._shape = this._shape.transform(fTransform);
    }

    @Override
    public void rotate2D(double angle) {
        AffineTransform2D affine2D = new AffineTransform2D();
        affine2D.rotate(-angle);

        AffineTransform fTransform = new AffineTransform(_spaceDim);
        for (int row = 0; row < 2; row++) {
            for (int column = 0; column < 2; column++) {
                fTransform.set(affine2D.get(row, column), row, column);
            }
        }

        this._shape = this._shape.transform(fTransform);

    }

    @Override
    public AxisOrder getAxisOrder() {
        return this._axisOrder;
    }

}