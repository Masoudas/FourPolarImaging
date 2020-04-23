package fr.fresnel.fourPolar.core.util.shape;

import java.util.Arrays;
import java.util.Objects;

import net.imglib2.realtransform.AffineTransform;
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
    private static double[][] _identity3D = { { 1, 0, 0, 0 }, { 0, 1, 0, 0 }, { 0, 0, 1, 0 } };

    final private RealMaskRealInterval _originalShape;
    private RealMaskRealInterval _shape;

    private final ShapeType _type;
    private final int _shapeDim;
    private final int _spaceDim;

    private final AffineTransform3D _affine3D;
    private final AffineTransform _finalTransform;

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
    public ImgLib2Shape(final ShapeType shapeType, final int shapeDim, final int spaceDim, RealMaskRealInterval shape) {
        this._type = shapeType;
        this._affine3D = new AffineTransform3D();
        this._finalTransform = new AffineTransform(spaceDim);
        this._shapeDim = shapeDim;
        this._spaceDim = spaceDim;
        this._pointMask = GeomMasks.pointMask(new double[spaceDim]);
        this._originalShape = shape;
        this._shape = shape.and(shape); // Just a bogus operation to get a new copy of the shape.
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
    public void transform(final long[] translation, final double x_rotation, final double z_rotation,
            final double y_rotation) {
        if (translation.length != this._spaceDim) {
            throw new IllegalArgumentException("Translation dimension must equal shape space dimension.");
        }

        double[] t;
        if (this._spaceDim < 3) {
            t = new double[] { -translation[0], -translation[1], 0 };
        } else {
            t = Arrays.stream(translation).mapToDouble((x) -> -x).toArray();
        }

        this._affine3D.translate(Arrays.stream(t).limit(3).toArray());

        // In the following order, x rotation is applied first, and then z rotation, and
        // then y.
        this._affine3D.rotate(2, -z_rotation);
        this._affine3D.rotate(0, -x_rotation);
        this._affine3D.rotate(1, -y_rotation);

        this._setAffineTransform(t);
        this._reset3DTransform();

        this._shape = this._shape.transform(this._finalTransform);

    }

    /**
     * Transforms the 3D affine transform to the trasnform appropriate for the
     * shape.
     * 
     * @param affine
     */
    private void _setAffineTransform(double[] translation) {
        // Set rotation
        for (int row = 0; row < this._shapeDim; row++) {
            for (int column = 0; column < this._shapeDim; column++) {
                _finalTransform.set(_affine3D.get(row, column), row, column);
            }
        }

        // Set translation
        for (int row = 0; row < this._shapeDim; row++) {
            _finalTransform.set(_affine3D.get(row, 3), row, this._spaceDim);
        }

        for (int row = this._shapeDim; row < this._spaceDim; row++) {
            _finalTransform.set(translation[row], row, this._spaceDim);
        }

    }

    /**
     * Resets the 3D transform for future uses.
     */
    private void _reset3DTransform() {
        this._affine3D.set(_identity3D);
    }

    @Override
    public boolean isInside(long[] point) {
        if (point.length < this._spaceDim) {
            return false;
        }

        this._pointMask.setPosition(point);
        return this._shape.test(this._pointMask);
    }

    /**
     * Ands this shape with the given shape. In case there's no overlap, the
     * resulting shape has no elements.
     * 
     * @throws IllegalArgumentException in case source and destination shape don't
     *                                  have the same space dimension.
     * 
     */
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

}