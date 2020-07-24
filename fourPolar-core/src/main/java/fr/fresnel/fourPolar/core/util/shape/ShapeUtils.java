package fr.fresnel.fourPolar.core.util.shape;

import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.shape.IShape;

/**
 * A set of utility methods for shape.
 */
public class ShapeUtils {
    private ShapeUtils() {

    }

    /**
     * Adds new axis to the given shape, from 0 point to @param dim.
     * @param dim must correspond to the number of newly added dimensions.
     * <p>
     * Example: We can add a new axis to a circle in XY, say Z (scaledAxisOrder =
     * XYZ). if @param dim = [2], then the scaled shape would be a cylinder from z = 0 to
     * z = 2;
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

    public static IShape addNewDimension(IShape shape, AxisOrder newAxisOrder, long[] dim) {
        return new ScaledShape(shape, newAxisOrder, dim);
    }

}