package fr.fresnel.fourPolar.algorithm.preprocess.registration;

import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.preprocess.registration.IChannelRegistrationResult;

/**
 * An interface for registering the images of a particular channel. As we assume
 * that the need for registration arises due to image segmentation (one or two
 * camera case), or lack of match between pixels (four camera case), and
 * variation in channels, and that no distortion occurs for different time and z
 * values, we enforce the given polarization image to be planar (because only
 * one z and t for a channel suffices.) However, this may change in the future
 * if we decide that registration for different z is also required.
 * 
 * Note that for applying registration result to different images,
 * {@link IChannelRealigner} should be used.
 */
public interface IChannelRegistrator {
    /**
     * Register pol45, pol90 and pol135 image of the given polarization image set to
     * pol0 image and return the resulting registration parameters as an
     * {@link IRegistrationResult}.
     * 
     * @throws IllegalArgumentException if the given polarization image is not
     *                                  planar.
     */
    public IChannelRegistrationResult register(IPolarizationImageSet polarizationImageSet)
            throws IllegalArgumentException;

}