package fr.fresnel.fourPolar.core.visualization.figures.registration;

import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.preprocess.registration.RegistrationRule;

/**
 * An interface for accessing the composite image resulting from merging a
 * registered image together with the base image, using to different colors, for
 * each {@link RegstrationOrder}.
 */
public interface IRegistrationCompositeFigure {
    public Image<RGB16> getCompositeImage(RegistrationRule rule);
}