package fr.fresnel.fourPolar.core.physics.na;

import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

/**
 * An interface that defines the numerical aperture of the imaging setup.
 */
public interface INumericalAperture {
    public double getNA(Polarization pol);   
}