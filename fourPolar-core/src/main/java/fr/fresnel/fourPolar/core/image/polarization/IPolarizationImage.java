package fr.fresnel.fourPolar.core.image.polarization;

import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

/**
 * An interface for accessing the polarization image. A polarization image is
 * the image corresponding to one of the four polarizations of a particular
 * channel.
 */
public interface IPolarizationImage {
    /**
     * Returns the polarization of this image.
     * 
     * @return
     */
    public Polarization getPolarization();

    /**
     * Returns the {@link Image} interface of this polarization image
     * 
     * @return
     */
    public Image<UINT16> getImage();

}