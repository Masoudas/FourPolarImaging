package fr.fresnel.fourPolar.core.util.shape;

import java.util.Arrays;
import java.util.Objects;

import net.imglib2.realtransform.AffineTransform;
import net.imglib2.realtransform.AffineTransform3D;
import net.imglib2.roi.RealMaskRealInterval;

/**
 * A set of utility methods for shape.
 */
public class ShapeUtils {
    private static double[][] _identity3D = { { 1, 0, 0, 0 }, { 0, 1, 0, 0 }, { 0, 0, 1, 0 } };

    private final static AffineTransform3D _affine3D = new AffineTransform3D();

    private ShapeUtils() {

    }

    /**
     * Scales the given shape over higher dimensions, so that the same shape can be
     * repeated in higher dimensions.
     * <p>
     * Example: Suppose a box as [0,0] to [1,1]. If scaleDimension is [5, 5, 2],
     * then the iterator would iterate over the following boxes, [0,0,0] to [1,1,0],
     * [0,0,1] to [1,1,1].
     * 
     * @param shape
     * @param scaleDimension
     * @return
     */
    public static IShapeIterator scaleShapeOverHigherDim(IShape shape, long[] scaleDimension) {
        return ShapeScalarIterator.getIterator(shape, scaleDimension);
    }

    /**
     * Ands the source shape with the destination shape and puts the result in the
     * destination shape. In case there's no overlap, the resulting shape has no
     * elements.
     * 
     * @throws IllegalArgumentException in case source and destination shape don't
     *                                  have the same space dimension.
     * 
     */
    public static void and(IShape shapeSource, IShape shapeDestination) {
        Objects.requireNonNull(shapeSource, "shapeSource should not be null.");
        Objects.requireNonNull(shapeDestination, "shapeDestination should not be null.");

        if (shapeSource.spaceDim() != shapeDestination.spaceDim()) {
            throw new IllegalArgumentException(
                    "The two shapes must have the same dimension and have the same space dimension");
        }

        ImgLib2Shape shapeSourceRef = (ImgLib2Shape) shapeSource;
        ImgLib2Shape shapeDestRef = (ImgLib2Shape) shapeDestination;
        RealMaskRealInterval result = shapeSourceRef.getRealMaskRealInterval()
                .and(shapeDestRef.getRealMaskRealInterval());

        shapeDestRef.setImgLib2Shape(result);
    }

    public static void transform(IShape sourceShape, IShape destinationShape, long[] translation,
            final double x_rotation, final double z_rotation, final double y_rotation) {
        if (translation.length != sourceShape.spaceDim()) {
            throw new IllegalArgumentException("Translation dimension must equal shape space dimension.");
        }

        double[] t = null;
        if (sourceShape.spaceDim() < 3) {
            t = new double[] { -translation[0], -translation[1], 0 };
        } else {
            t = Arrays.stream(translation).mapToDouble((x) -> -x).toArray();
        }

        _affine3D.translate(t);
        
        // In the following order, x rotation is applied first, and then z rotation, and
        // then y.
        _affine3D.rotate(2, z_rotation);
        _affine3D.rotate(0, x_rotation);
        _affine3D.rotate(1, y_rotation);

        AffineTransform nDimTransform = _setNDimAffineTransform(sourceShape, t);

        ImgLib2Shape sourceShapeRef = (ImgLib2Shape) sourceShape;
        ImgLib2Shape destinationShapeRef = (ImgLib2Shape) sourceShape;

        final RealMaskRealInterval transformedMask = sourceShapeRef.getRealMaskRealInterval().transform(nDimTransform);
        destinationShapeRef.setImgLib2Shape(transformedMask);

        _resetTransformationParams();

    }

    private static AffineTransform _setNDimAffineTransform(IShape sourceShape, double[] translation) {
        AffineTransform finalTransform = new AffineTransform(sourceShape.spaceDim());
        int shapeDim = sourceShape.shapeDim();
        int spaceDim = sourceShape.spaceDim();

        // Set rotation
        for (int row = 0; row < shapeDim; row++) {
            for (int column = 0; column < shapeDim; column++) {
                finalTransform.set(_affine3D.get(row, column), row, column);
            }
        }

        // Set translation
        for (int row = 0; row < shapeDim; row++) {
            finalTransform.set(_affine3D.get(row, 3), row, spaceDim);
        }

        for (int row = shapeDim; row < spaceDim; row++) {
            finalTransform.set(translation[row], row, spaceDim);
        }

        return finalTransform;

    }

    private static void _resetTransformationParams() {
        _affine3D.set(_identity3D);
    }

}