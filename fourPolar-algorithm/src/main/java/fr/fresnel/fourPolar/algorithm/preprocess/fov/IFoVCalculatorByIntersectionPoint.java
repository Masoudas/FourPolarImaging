package fr.fresnel.fourPolar.algorithm.preprocess.fov;

import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.IFieldOfView;

/**
 * /** An interface for calculating the FoV from the bead images using
 * intersection point of polarizations. See {@link FoVCalculatorByIntersectionPointOneCamera},
 * {@link FoVCalculatorByIntersectionPointTwoCamera} and {@link FoVCalculatorByIntersectionPointFourCamera}
 * 
 * 
 * @deprecated Replaced by {@link IFoVCalculator}.
 */
@Deprecated
public interface IFoVCalculatorByIntersectionPoint {
    public IFieldOfView calculate();

}