package fr.fresnel.fourPolar.core.util.shape;


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


}