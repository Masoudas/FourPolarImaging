package fr.fresnel.fourPolar.core.util.shape;

import java.util.Arrays;

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

    private RealMaskRealInterval _maskRealInterval;
    private final ShapeType _type;
    private final int _shapeDim;
    private final int _spaceDim;

    private ImgLib2Shape _transformed;

    private final AffineTransform3D _affine3D;
    private final AffineTransform _transform3D;
    private double[] _translation;

    private double _xRotation = 0;
    private double _yRotation = 0;
    private double _zRotation = 0;

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
    public ImgLib2Shape(final ShapeType shapeType, final int shapeDim, final int spaceDim) {
        this._type = shapeType;
        this._affine3D = new AffineTransform3D();
        this._transform3D = new AffineTransform(spaceDim);
        this._shapeDim = shapeDim;
        this._spaceDim = spaceDim;
        this._pointMask = GeomMasks.pointMask(new double[spaceDim]);
        this._translation = new double[spaceDim];
    }

    public void setImgLib2Shape(final RealMaskRealInterval maskRealInterval) {
        this._maskRealInterval = maskRealInterval;
    }

    @Override
    public IShapeIterator getIterator() {
        final IterableRegion<BoolType> iterableRegion = Regions
                .iterable(Views.interval(Views.raster(Masks.toRealRandomAccessible(this._maskRealInterval)),
                        Intervals.largestContainedInterval(this._maskRealInterval)));

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

    public RealMaskRealInterval getRealMaskRealInterval() {
        return this._maskRealInterval;
    }

    @Override
    public IShape rotate(final double x_rotation, final double z_rotation, final double y_rotation) {

        _xRotation = -x_rotation;
        _yRotation = -y_rotation;
        _zRotation = -z_rotation;

        return this._transformed;

    }

    public void translate(long[] translation) {
        if (translation.length != this._spaceDim) {
            throw new IllegalArgumentException("Translation dimension must equal shape space dimension.");
        }

        if (this._spaceDim < 3) {
            this._translation = new double[] { -translation[0], -translation[1], 0 };
        } else {
            this._translation = Arrays.stream(translation).mapToDouble((x) -> -x).toArray();
        }

    }

    @Override
    public IShape getTransformedShape() {
        if (this._transformed == null) {
            this._transformed = new ImgLib2Shape(this._type, this._shapeDim, this._spaceDim);
        }

        this._affine3D.translate(Arrays.stream(this._translation).limit(3).toArray());
        // In the following order, x rotation is applied first, and then z rotation, and
        // then y.
        this._affine3D.rotate(2, _zRotation);
        this._affine3D.rotate(0, _xRotation);
        this._affine3D.rotate(1, _yRotation);

        this._setAffineTransform();

        final RealMaskRealInterval transformedMask = this._maskRealInterval.transform(this._transform3D);
        this._transformed.setImgLib2Shape(transformedMask);

        this._resetTransformationParams();

        return this._transformed;

    }

    /**
     * Assign to a higher dimensional matrix from a lower dimensional matrix.
     * 
     * @param affine
     */
    private void _setAffineTransform() {
        // Set rotation
        for (int row = 0; row < this._shapeDim; row++) {
            for (int column = 0; column < this._shapeDim; column++) {
                _transform3D.set(_affine3D.get(row, column), row, column);
            }
        }

        // Set translation
        for (int row = 0; row < this._shapeDim; row++) {
            _transform3D.set(_affine3D.get(row, 3), row, this._spaceDim);
        }

        for (int row = this._shapeDim; row < this._spaceDim; row++) {
            _transform3D.set(this._translation[row], row, this._spaceDim);
        }

    }

    private void _resetTransformationParams() {
        this._affine3D.set(_identity3D);
        _xRotation = 0;
        _yRotation = 0;
        _zRotation = 0;
        Arrays.setAll(_translation, (t) -> 0d);
    }

    @Override
    public boolean isInside(long[] point) {
        this._pointMask.setPosition(point);
        return this._maskRealInterval.test(this._pointMask);
    }

    @Override
    public IShape and(IShape shape) {
        if (this._transformed == null) {
            this._transformed = new ImgLib2Shape(this._type, this._shapeDim, this._spaceDim);
        }

        if (shape.shapeDim() != this._shapeDim || shape.spaceDim() != this._spaceDim) {
            throw new IllegalArgumentException(
                    "The two shapes must have the same dimension and have the same space dimension");
        }

        RealMaskRealInterval result = null;
        if (shape instanceof ImgLib2Shape) {
            ImgLib2Shape shapeRef = (ImgLib2Shape) shape;
            result = this._maskRealInterval.and(shapeRef.getRealMaskRealInterval());
        }
        this._transformed.setImgLib2Shape(result);

        return this._transformed;
    }

}