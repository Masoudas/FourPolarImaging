package fr.fresnel.fourPolar.core.util.shape;

import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;

public class ShapeUtils {

	/**
	 * Throws IllegalArgumentException if axisOrder is not undefined and shape
	 * dimension and axis order are not equal.
	 */
	public static void checkShapeDimAndNumAxisEqual(int shapeDim, AxisOrder axisOrder) {
		if (axisOrder != AxisOrder.NoOrder && shapeDim != axisOrder.numAxis) {
			throw new IllegalArgumentException("Shape dimension and axis order are not equal");
		}
	}

	/**
	 * Throws IllegalArgumentException is shape dimension is non-zero.
	 */
	public static void checkShapeDimNonzero(int shapeDim) {
		if (shapeDim <= 0) {
			throw new IllegalArgumentException("Shape dimension must be nonzero");
		}
	}
    
}