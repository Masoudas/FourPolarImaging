package fr.fresnel.fourPolar.core.util.shape;

import net.imglib2.realtransform.AffineTransform2D;
import net.imglib2.realtransform.AffineTransform3D;
import net.imglib2.roi.IterableRegion;
import net.imglib2.roi.Masks;
import net.imglib2.roi.RealMaskRealInterval;
import net.imglib2.roi.Regions;
import net.imglib2.type.logic.BoolType;
import net.imglib2.util.Intervals;
import net.imglib2.view.Views;

/**
 * Assign various library shapes to our shape. Class should not be implemented
 * for other libraries. Use set methods.
 */
class ImgLib2Shape implements IShape {
    private static double[][] _identity2D = { { 1, 0, 0 }, { 0, 1, 0 }, { 0, 0, 1 } };
    private static double[][] _identity3D = { { 1, 0, 0, 0 }, { 0, 1, 0, 0 }, { 0, 0, 1, 0 }, { 0, 0, 0, 1 } };

    private RealMaskRealInterval _maskRealInterval;
    private ShapeType _type;
    private int _shapeDim;
    private long[] _samplePosition = null;

    private ImgLib2Shape _transformed2D;
    private ImgLib2Shape _transformed3D;

    private final AffineTransform2D _affine2D;
    private final AffineTransform3D _affine3D;

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
    public ImgLib2Shape(final ShapeType shapeType) {
        this._type = shapeType;
        this._affine2D = new AffineTransform2D();
        this._affine3D = new AffineTransform3D();
    }

    public void setImgLib2Shape(int shapeDim, long[] samplePosition, RealMaskRealInterval maskRealInterval) {
        this._maskRealInterval = maskRealInterval;        
        this._samplePosition = samplePosition;
        this._shapeDim = shapeDim;
    }

    @Override
    public IShapeIteraror getIterator() {
        IterableRegion<BoolType> iterableRegion = Regions
                .iterable(Views.interval(Views.raster(Masks.toRealRandomAccessible(this._maskRealInterval)),
                        Intervals.largestContainedInterval(this._maskRealInterval)));

        return new ShapeIterator(iterableRegion, this._shapeDim, this._samplePosition);
    }

    @Override
    public ShapeType getType() {
        return this._type;
    }

    @Override
    public int numDimension() {
        return this._samplePosition.length;
    }

    @Override
    public int shapeDim() {
        return this._shapeDim;
    }

    @Override
    public IShape transform2D(long[] translation, double rotation) {
        if (this._shapeDim != 2) {
            throw new IllegalStateException("Can't 2d transform a shape that is not 2d.");
        }

        if (translation.length != 2) {
            throw new IllegalArgumentException("translation vector has to be 2d.");
        }

        if (this._transformed2D == null){
            this._transformed2D = new ImgLib2Shape(this._type);
        }

        this._affine2D.set(_identity2D);

        this._affine2D.rotate(-rotation);
        double[] rotatedTranslation = { -translation[0], -translation[1] };
        this._affine2D.apply(rotatedTranslation, rotatedTranslation);
        this._affine2D.translate(rotatedTranslation);

        RealMaskRealInterval transformedMask = this._maskRealInterval.transform(this._affine2D);
        this._transformed2D.setImgLib2Shape(this._shapeDim, this._samplePosition, transformedMask);

        return this._transformed2D;
    }

    @Override
    public IShape transform3D(long[] translation, double x_rotation, double z_rotation, double y_rotation) {
        if (this._shapeDim != 3) {
            throw new IllegalStateException("Can't 3d transform a shape that is not 3d.");
        }

        if (translation.length != 3) {
            throw new IllegalArgumentException("translation vector has to be 3d.");
        }

        if (this._transformed3D == null){
            this._transformed3D = new ImgLib2Shape(this._type);
        }

        this._affine3D.set(_identity3D);

        // In the following order, x rotation is applied first, and then z rotation, and
        // then y.
        this._affine3D.rotate(2, -z_rotation);
        this._affine3D.rotate(0, -x_rotation);
        this._affine3D.rotate(1, -y_rotation);

        double[] rotatedTranslation = { -translation[0], -translation[1], -translation[2] };
        this._affine3D.apply(rotatedTranslation, rotatedTranslation);

        this._affine3D.translate(rotatedTranslation);

        RealMaskRealInterval transformedMask = this._maskRealInterval.transform(this._affine3D);
        this._transformed3D.setImgLib2Shape(this._shapeDim, this._samplePosition, transformedMask);

        return this._transformed3D;

    }

}