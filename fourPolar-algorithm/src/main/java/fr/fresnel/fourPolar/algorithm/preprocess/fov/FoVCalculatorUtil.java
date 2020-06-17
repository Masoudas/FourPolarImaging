package fr.fresnel.fourPolar.algorithm.preprocess.fov;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.util.shape.IBoxShape;
import fr.fresnel.fourPolar.core.util.shape.IPointShape;

class FoVCalculatorUtil {
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

    /**
     * Checks wether this 2D box is inside the image.
     */
    public static void checkFoVBoxIsInImageBoundary(IMetadata imageMetadata, IBoxShape fovBox, Polarization pol) {
        _checkFoVBoxIs2D(fovBox, pol);
        _checkFoVBoxMinIsPositive(fovBox, pol);

        long[] fovMax = fovBox.max(); 
        long[] imgDim = imageMetadata.getDim();
        
        if (fovMax[0] > imgDim[0] || fovMax[1] > imgDim[1]) {
            throw new IllegalArgumentException("Field of View Box is outside image boundary.");
        }
    }

    private static void _checkFoVBoxIs2D(IBoxShape fovBox, Polarization pol) {
        if (fovBox.shapeDim() != 2) {
            throw new IllegalArgumentException("fov region for " + pol + " must be a 2D box.");
        }
    }

    private static void _checkFoVBoxMinIsPositive(IBoxShape fovBox, Polarization pol) {
        long[] fovMin = fovBox.min();
        if (fovMin[0] < 0 || fovMin[1] < 0) {
            throw new IllegalArgumentException("FoV box should be non-negative");
        }

    }
}