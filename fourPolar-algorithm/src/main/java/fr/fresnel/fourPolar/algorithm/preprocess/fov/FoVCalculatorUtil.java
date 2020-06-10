package fr.fresnel.fourPolar.algorithm.preprocess.fov;

import fr.fresnel.fourPolar.core.util.shape.IPointShape;

public class FoVCalculatorUtil {
    private FoVCalculatorUtil() {
        throw new AssertionError();
    }

    /**
     * Checks that the intersection point for one camera and two camera case is 2D
     * and if not, throws an exception.
     * 
     * @throws IllegalArgumentException in case point is not 2D.
     */
    public static void checkIntersectionPointIs2D(IPointShape intersectionPoint) throws IllegalArgumentException {
        if (intersectionPoint.point().length != 2) {
            throw new IllegalArgumentException("Intersection point for calculating the FoV must be two dimensional");
        }
    }
}