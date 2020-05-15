package fr.fresnel.fourPolar.algorithm.preprocess.fov;

import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.IFieldOfView;

/**
 * An interface for calculating the FoV from the bead images and intersection points.
 * See {@link FoVCalculatorOneCamera}, {@link FoVCalculatorTwoCamera} and 
 * {@link FoVCalculatorFourCamera}
 */
public interface IFoVCalculator {
    public IFieldOfView calculate();
    
}