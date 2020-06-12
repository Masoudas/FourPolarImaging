package fr.fresnel.fourPolar.core.util.shape;

import java.util.Objects;

import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import net.imglib2.realtransform.AffineGet;
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
    protected RealMaskRealInterval _shape;

    protected final int _shapeDim;
    protected final AxisOrder _axisOrder;

    /**
     * A point mask instance to check whether a point is inside the shape.
     */
    final protected WritablePointMask _pointMask;

    /**
     * Construct the shape, using ImgLib2 ROI. @See RealMaskRealInterval.
     * 
     * @param shapeType is the associated shape type.
     * @param shapeDim  is the dimension of the shape (two for a 2DBox for example).
     * @param spaceDim  This is the dimension of the space over which the shape is
     *                  defined.
     * 
     */
    protected ImgLib2Shape(RealMaskRealInterval shape, final AxisOrder axisOrder) {
        Objects.requireNonNull(shape);
        Objects.requireNonNull(axisOrder);

        this._checkShapeDimAndNumAxisEqual(shape.numDimensions(), axisOrder);
        this._checkShapeDimNonzero(shape.numDimensions());

        this._shapeDim = shape.numDimensions();
        this._pointMask = GeomMasks.pointMask(new double[shape.numDimensions()]);
        this._axisOrder = axisOrder;
        this._shape = shape;
    }

    private void _checkShapeDimAndNumAxisEqual(int shapeDim, AxisOrder axisOrder) {
        ShapeUtils.checkShapeDimAndNumAxisEqual(shapeDim, axisOrder);
    }

    private void _checkShapeDimNonzero(int shapeDim) {
        ShapeUtils.checkShapeDimNonzero(shapeDim);
    }

    @Override
    public IShapeIterator getIterator() {
        final IterableRegion<BoolType> iterableRegion = Regions
                .iterable(Views.interval(Views.raster(Masks.toRealRandomAccessible(this._shape)),
                        Intervals.largestContainedInterval(this._shape)));

        return new ShapeIterator(iterableRegion, this._shapeDim);
    }

    @Override
    public int shapeDim() {
        return this._shapeDim;
    }

    @Override
    public AxisOrder axisOrder() {
        return this._axisOrder;
    }

    public RealMaskRealInterval getImgLib2Shape() {
        return this._shape;
    }

    @Override
    public boolean isInside(long[] point) {
        if (point.length != this._shapeDim) {
            throw new IllegalArgumentException("The given point does not have same number of axis as shape.");
        }

        this._pointMask.setPosition(point);
        return this._shape.test(this._pointMask);
    }

    @Override
    public IShape and(IShape shape) {
        if (shape instanceof ImgLib2Shape) {
            return ImgLib2LogicalShape.createAndedShape(this, shape);
        } else {
            throw new IllegalArgumentException("Can't And this shape");
        }
    }

    @Override
    public IShape rotate3D(double angle1, double angle2, double angle3, Rotation3DOrder rotation3dOrder) {
        if (this._axisOrder == AxisOrder.NoOrder || this._axisOrder.z_axis < 0) {
            throw new IllegalArgumentException("Impossible to rotate 3D because no z-axis exists.");
        }

        return this._transformShape(this._createAffine3DMatrix(angle1, angle2, angle3, rotation3dOrder));
    }

    @Override
    public IShape translate(long[] translation) {
        if (translation.length != this._shapeDim) {
            throw new IllegalArgumentException(
                    "Translation must occur over all axis. Consider using zero for undesired axis.");
        }

        return this._transformShape(this._createAffineTranslation(translation, this._shapeDim));
    }

    @Override
    public IShape rotate2D(double angle) {
        if (this._axisOrder == AxisOrder.NoOrder) {
            throw new IllegalArgumentException("Impossible to rotate, because no axis-order is defined");
        }
        return _transformShape(this._createAffine2DRotation(angle));
    }

    /**
     * The access point for all transformations of the original shape.
     */
    protected IShape _transformShape(AffineGet appliedAffineTransform) {
        RealMaskRealInterval transformedShape = this._shape.transform(appliedAffineTransform);
        return new ImgLib2Shape(transformedShape, this._axisOrder);
    }

    private AffineGet _createAffine2DRotation(double angle) {
        AffineTransform2D affine2D = new AffineTransform2D();
        affine2D.rotate(-angle);

        AffineTransform fTransform = new AffineTransform(this._shapeDim);
        for (int row = 0; row < 2; row++) {
            for (int column = 0; column < 2; column++) {
                fTransform.set(affine2D.get(row, column), row, column);
            }
        }
        return fTransform;
    }

    private AffineGet _createAffine3DMatrix(double angle1, double angle2, double angle3,
            Rotation3DOrder rotation3dOrder) {
        int z_axis = this._axisOrder.z_axis;
        int[] axis = Rotation3DOrder.getAxisOrder(rotation3dOrder);

        AffineTransform3D affine3D = new AffineTransform3D();
        affine3D.rotate(axis[2], -angle3);
        affine3D.rotate(axis[1], -angle2);
        affine3D.rotate(axis[0], -angle1);

        int[] rowsToFill = { 0, 1, z_axis }; // Row, columns to be filled in the affine transform.
        AffineTransform fTransform = new AffineTransform(this._shapeDim);
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                fTransform.set(affine3D.get(row, column), rowsToFill[row], rowsToFill[column]);
            }
        }
        return fTransform;
    }

    private AffineTransform _createAffineTranslation(long[] translation, int numAxis) {
        AffineTransform fTransform = new AffineTransform(numAxis);
        // Set translation
        for (int row = 0; row < numAxis; row++) {
            fTransform.set(-translation[row], row, numAxis);
        }
        return fTransform;
    }

}