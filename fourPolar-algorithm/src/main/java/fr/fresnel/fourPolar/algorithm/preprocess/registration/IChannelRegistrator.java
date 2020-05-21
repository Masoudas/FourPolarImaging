package fr.fresnel.fourPolar.algorithm.preprocess.registration;

import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.preprocess.registration.IChannelRegistrationResult;

/**
 * An interface for registering the images of a particular channel. Note that
 * for applying registration to different images, {@link IChannelRealigner}
 * should be used.
 */
public interface IChannelRegistrator {
    /**
     * Register pol45, pol90 and pol135 image of the given polarization image set to
     * pol0 image and return the resulting registration parameters as an
     * {@link IRegistrationResult}.
     */
    public IChannelRegistrationResult register(IPolarizationImageSet polarizationImageSet);

}