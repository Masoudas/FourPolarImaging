package fr.fresnel.fourPolar.algorithm.preprocess.fov;

import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.IFieldOfView;
import fr.fresnel.fourPolar.core.util.shape.IBoxShape;

/**
 * An interface for calculating the {@link IFieldOfView} of a particular
 * {@link Camera},
 */
public interface IFoVCalculator {

    /**
     * Calculate fov using four {@link IBoxShape} defined on each polarization. For
     * this end, the minimum box dimension is calculated among boxes (so that all
     * {@link IPolarizationImage} have the same dimension, and then fov for each
     * polarization is returned from box minimum plus the minimum length of all
     * boxes.
     * 
     * @param pol0_fov   is the field of view of pol0.
     * @param pol45_fov  is the field of view of pol45.
     * @param pol90_fov  is the field of view of pol90.
     * @param pol135_fov is the field of view of pol135.
     * @return the field of view.
     */
    public IFieldOfView calculate(IBoxShape pol0_fov, IBoxShape pol45_fov, IBoxShape pol90_fov, IBoxShape pol135_fov);
}