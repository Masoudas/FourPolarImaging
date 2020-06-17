package fr.fresnel.fourPolar.algorithm.preprocess.fov;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;

/**
 * Creates a fov calculator for the given number of cameras
 */
public class FoVCalculator {
    private static IFoVCalculator _INSTANCE_ONE_CAMERA;
    private static IFoVCalculator _INSTANCE_TWO_CAMERA;
    private static IFoVCalculator _INSTANCE_FOUR_CAMERA;

    /**
     * Create a calculator for {@link Cameras#One}.
     * 
     * @param pol0_45_90_135 is the metadata of an image captured by this camera
     *                       (preferably a registration image).
     * @return field of view.
     */
    public static IFoVCalculator oneCamera(IMetadata pol0_45_90_135) {

        if (_INSTANCE_ONE_CAMERA == null) {
            _INSTANCE_ONE_CAMERA = new FoVCalculatorOneCamera(pol0_45_90_135);
        }

        return _INSTANCE_ONE_CAMERA;
    }

    /**
     * Create a calculator for {@link Cameras#Two}.
     * 
     * @param pol0_90   is the metadata of an image captured by the camera capturing
     *                  pol0 and pol90 (preferably a registration image).
     * @param pol45_135 is the metadata of an image captured by the camera capturing
     *                  pol45 and pol135 (preferably a registration image).
     * 
     * @return field of view.
     */
    public static IFoVCalculator twoCamera(IMetadata pol0_90, IMetadata pol45_135) {
        if (_INSTANCE_TWO_CAMERA == null) {
            _INSTANCE_TWO_CAMERA = new FoVCalculatorTwoCamera(pol0_90, pol45_135);
        }

        return _INSTANCE_TWO_CAMERA;
    }

    /**
     * Create a calculator for {@link Cameras#Four}.
     * 
     * @param pol0   is the metadata of an image captured by the camera capturing
     *               pol0 (preferably a registration image).
     * @param pol45  is the metadata of an image captured by the camera capturing
     *               pol45 (preferably a registration image).
     * @param pol90  is the metadata of an image captured by the camera capturing
     *               pol90 (preferably a registration image).
     * @param pol135 is the metadata of an image captured by the camera capturing
     *               pol135 (preferably a registration image).
     * 
     * @return field of view.
     */
    public static IFoVCalculator fourCamera(IMetadata pol0, IMetadata pol45, IMetadata pol90, IMetadata pol135) {
        if (_INSTANCE_FOUR_CAMERA == null) {
            _INSTANCE_FOUR_CAMERA = new FoVCalculatorFourCamera(pol0, pol45, pol90, pol135);
        }

        return _INSTANCE_FOUR_CAMERA;
    }

    private FoVCalculator() {

    }
}