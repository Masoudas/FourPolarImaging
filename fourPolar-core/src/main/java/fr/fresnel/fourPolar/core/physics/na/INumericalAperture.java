package fr.fresnel.fourPolar.core.physics.na;

import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

/**
 * An interface that defines the numerical aperture of the imaging setup.
 */
public interface INumericalAperture {
    /**
     * Give the numerical aperture for the given polariztion.
     * @param pol
     * @return
     */
    public double getNA(Polarization pol);   


    /**
     * Checks whether two numerical apertures are equal.
     * @param na
     * @return
     */
    public boolean equals(INumericalAperture na);
}