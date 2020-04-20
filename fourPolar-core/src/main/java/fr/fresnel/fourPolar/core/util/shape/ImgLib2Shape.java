package fr.fresnel.fourPolar.core.util.shape;

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
    private static double[][] _identity2D = { { 1, 0, 0 }, { 0, 1, 0 } };
    private static double[][] _identity3D = { { 1, 0, 0, 0 }, { 0, 1, 0, 0 }, { 0, 0, 1, 0 } };

    private RealMaskRealInterval _maskRealInterval;
    private final ShapeType _type;
    private int _shapeDim;

    private ImgLib2Shape _transformed;

    private final AffineTransform2D _affine2D;
    private final AffineTransform3D _affine3D;
    
    private final AffineTransform _transform2D;
    private final AffineTransform _transform3D;

    /**
     * A point mask instance to check whether a point is inside the shape.
     */
    final private WritablePointMask _pointMask;

    /**
     * Construct the shape, using ImgLib2 ROI. @See RealMaskRealInterval.
     * 
     * @param shapeType        is the associated shape type.
     * @param shapeDim         is the dimension of the shape (two for a 2DBox for
     *                         example).
     * @param samplePosition   is one arbitrary position associated with the shape.
     *                         Dimensions higher than shapeDim will be read from
     *                         this value.
     * @param maskRealInterval is the associated ImgLib2 shape interval.
     */
    public ImgLib2Shape(final ShapeType shapeType, final int shapeDim) {
        this._type = shapeType;
        this._affine2D = new AffineTransform2D();
        this._affine3D = new AffineTransform3D();
        this._transform2D = new AffineTransform(shapeDim);
        this._transform3D = new AffineTransform(shapeDim);
        this._shapeDim = shapeDim;
        this._pointMask = GeomMasks.pointMask(new double[shapeDim]);
    }

    public void setImgLib2Shape(final RealMaskRealInterval maskRealInterval) {
        this._maskRealInterval = maskRealInterval;
    }

    @Override
    public IShapeIteraror getIterator() {
        final IterableRegion<BoolType> iterableRegion = Regions
                .iterable(Views.interval(Views.raster(Masks.toRealRandomAccessible(this._maskRealInterval)),
                        Intervals.largestContainedInterval(this._maskRealInterval)));

        return new ShapeIterator(iterableRegion, this._shapeDim);
    }

    @Override
    public ShapeType getType() {
        return this._type;
    }

    @Override
    public int numDimension() {
        return this._shapeDim;
    }

    @Override
    public int shapeDim() {
        return this._shapeDim;
    }

    @Override
    public IShape transform2D(final long[] translation, final double rotation) {
        if (translation.length != 2) {
            throw new IllegalArgumentException("translation vector has to be 2d.");
        }

        if (this._transformed == null) {
            this._transformed = new ImgLib2Shape(this._type, this._shapeDim);
        }

        this._affine2D.set(_identity2D);

        final double[] t = { -translation[0], -translation[1] };
        // this._affine2D.apply(rotatedTranslation, rotatedTranslation);
        this._affine2D.translate(t);
        this._affine2D.rotate(-rotation);
        
        this._setAffineTransform(this._transform2D, this._affine2D);

        final RealMaskRealInterval transformedMask = this._maskRealInterval.transform(this._transform2D);
        this._transformed.setImgLib2Shape(transformedMask);

        return this._transformed;
    }

    @Override
    public IShape transform3D(final long[] translation, final double x_rotation, final double z_rotation,
            final double y_rotation) {
        if (translation.length != 3) {
            throw new IllegalArgumentException("translation vector has to be 3d.");
        }

        if (this._transformed == null) {
            this._transformed = new ImgLib2Shape(this._type, this._shapeDim);
        }

        this._affine3D.set(_identity3D);

        final double[] t = { -translation[0], -translation[1], -translation[2] };
        this._affine3D.translate(t);

        // In the following order, x rotation is applied first, and then z rotation, and
        // then y.
        this._affine3D.rotate(2, -z_rotation);
        this._affine3D.rotate(0, -x_rotation);
        this._affine3D.rotate(1, -y_rotation);

        this._setAffineTransform(this._transform3D, this._affine3D);

        final RealMaskRealInterval transformedMask = this._maskRealInterval.transform(this._transform3D);
        this._transformed.setImgLib2Shape(transformedMask);

        return this._transformed;

    }

    /**
     * Assign to a higher dimensional matrix from a lower dimensional matrix.
     * 
     * @param affine
     */
    private void _setAffineTransform(AffineTransform finalTransform, AffineGet affine) {
        int dim = affine.numDimensions();
        // Set rotation
        for (int row = 0; row < dim; row++) {
            for (int column = 0; column < dim; column++) {
                finalTransform.set(affine.get(row, column), row, column);
            }
        }

        int lastColumn = finalTransform.numDimensions();
        // Set translation
        for (int row = 0; row < dim; row++) {
            finalTransform.set(affine.get(row, dim), row, lastColumn);
        }

    }

    @Override
    public boolean isInside(long[] point) {        
        this._pointMask.setPosition(point);
        return this._maskRealInterval.test(this._pointMask);
    }

}