package fr.fresnel.fourPolar.algorithm.preprocess.fov;

import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.IFieldOfView;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

/**
 * An interface for calculating the {@link IFieldOfView} of a particular
 * {@link Camera},
 */
public interface IFoVCalculator {
    /**
     * Set the x and y coordinate of minimum point (starting point) of this
     * polarization.
     * 
     * @param x   is the x coordiante.
     * @param y   is the y coordinate.
     * @param pol is the polarization.
     * 
     * @throws IllegalArgumentException if the x and y points are negative or are
     *                                  outside the boundary of this polarization
     *                                  dimension.
     */
    public void setMin(long x, long y, Polarization pol);

    /**
     * Set the x and y coordinate of maximum point (end point) of this polarization.
     * 
     * @param x   is the x coordiante.
     * @param y   is the y coordinate.
     * @param pol is the polarization.
     * 
     * @throws IllegalArgumentException if the x and y points are negative or are
     *                                  outside the boundary of this polarization
     *                                  dimension.
     */
    public void setMax(long x, long y, Polarization pol);

    /**
     * Calculate fov using the minimum and maximum points for each polarization.
     * 
     * @return the field of view.
     * 
     * @throws IllegalStateException if the top and bottom point is not provided for
     *                               all polarizations.
     */
    public IFieldOfView calculate();

}