package fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov;

import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.util.shape.IBoxShape;

/**
 * The interface for accessing the field of view (fov) information of the bead images.
 */
public interface IFieldOfView {
    public IBoxShape getFoV(Polarization pol);
    
}