package fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov;

import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.shape.IBoxShape;

/**
 * The interface for accessing the field of view (fov) information of the bead
 * images. Note that for convenience, the pixels are supposed to start from 1 to
 * dimension of the image.
 */
public interface IFieldOfView {
    /**
     * Return fov as an {@link IBoxShape}, where fov starts from
     * {@link IBoxShape#min()} to {@link IBoxShape#max()} inclusive.
     */
    public IBoxShape getFoV(Polarization pol);

    /**
     * Returns the maximum length among x and y axis of all field of views.
     * 
     * @return an array as [maximum_x, maximum_y].
     */
    public long[] getMaximumLength();

}