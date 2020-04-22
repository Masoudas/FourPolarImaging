package fr.fresnel.fourPolar.core.util.shape;

import java.util.Objects;

import net.imglib2.roi.RealMaskRealInterval;

/**
 * A set of utility methods for shape.
 */
public class ShapeUtils {
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

}