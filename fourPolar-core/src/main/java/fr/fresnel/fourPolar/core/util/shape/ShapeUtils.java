package fr.fresnel.fourPolar.core.util.shape;

import fr.fresnel.fourPolar.core.physics.axis.AxisOrder;

/**
 * A set of utility methods for shape.
 */
public class ShapeUtils {
    private ShapeUtils() {

    }

    /**
     * Add new axis to the given shape and iterates over all axis, from o point to
     * max point (inclusive). max must correspond to the number of newly added dimensions.
     * <p>
     * Example: We can add a new axis to a circle in XY, say Z (scaledAxisOrder =
     * XYZ) starting from min = [0], to max[2], which turns circle into cylinder it
     * into a cylinder.
     * 
     * @param shape           is the original shape.
     * @param scaledAxisOrder is the new axis order. Note that the unscaled axis
     *                        must be the same as original shape.
     * @param max             is the final point of new axis.
     * @return the shape iterator for the scaled shape.
     * 
     * @throws IllegalArgumentException in case min or max have unequal length, or
     *                                  their length is unequal to the number of
     *                                  scaled dimension.
     */

    public static IShapeIterator addNewDimension(IShape shape, AxisOrder newAxisOrder, long[] max) {
        return ShapeScalarIterator.getIterator(shape, newAxisOrder, max);
    }

}