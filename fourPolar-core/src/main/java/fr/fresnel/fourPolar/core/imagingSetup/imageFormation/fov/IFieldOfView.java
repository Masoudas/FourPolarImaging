package fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov;

import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.util.shape.IBoxShape;

/**
 * The interface for accessing the field of view (fov) information of the bead
 * images. Note that for convenience, the pixels are supposed to start from 1 to
 * dimension of the image.
 */
public interface IFieldOfView {
    public IBoxShape getFoV(Polarization pol);

}