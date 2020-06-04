package fr.fresnel.fourPolar.core.visualization.figures.polarization;

import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.preprocess.registration.RegistrationRule;

/**
 * An interface for accessing the composite image of a polarization. A composite
 * image is the result of merging two polarization images according to a
 * {@link RegstrationOrder}, where one is called the base and the other is
 * called to register.
 */
public interface IPolarizationImageComposite {
    /**
     * The registration rule this image is associated with.
     */
    public RegistrationRule getRegistrationRule();

    /**
     * Get the image.
     */
    public Image<RGB16> getImage();
}