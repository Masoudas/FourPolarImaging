package fr.fresnel.fourPolar.core.visualization.figures.polarization;

import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.preprocess.registration.RegistrationRule;

/**
 * An interface for accessing the composite image resulting from merging a
 * registered image together with the base image, using to different colors, for
 * each {@link RegstrationOrder}, for a given channel.
 */
public interface IPolarizationImageSetComposites {
    /**
     * The channel number the composite figures belong to.
     */
    public int channel();

    /**
     * Set the composite image corresponding to the given rule.
     */
    public void setCompositeImage(RegistrationRule rule, Image<RGB16> compositeImage);

    /**
     * Get the composite image corresponding to the given rule.
     */
    public Image<RGB16> getCompositeImage(RegistrationRule rule);
}