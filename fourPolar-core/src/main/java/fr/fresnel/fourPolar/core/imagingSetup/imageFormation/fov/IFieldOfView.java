package fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov;

import fr.fresnel.fourPolar.core.physics.polarization.Polarizations;

/**
 * The interface for accessing the field of view (fov) information of the bead images.
 */
public interface IFieldOfView {
    public Rectangle getFoV(Polarizations pol);
    
}