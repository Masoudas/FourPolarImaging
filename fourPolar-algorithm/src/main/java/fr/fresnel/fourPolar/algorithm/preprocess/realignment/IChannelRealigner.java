package fr.fresnel.fourPolar.algorithm.preprocess.realignment;

import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;

/**
 * Realings the polarization images of a given channel. This implies applying
 * the affine transform calculated using the convention in
 * {@link IChannelRegistrator}.
 */
public interface IChannelRealigner {
    /**
     * Realigns the given polarization image set, using the corresponding channel
     * {@link IChannelRegistrationResult}.
     * 
     * @param imageSet
     */
    public void realign(IPolarizationImageSet imageSet);
}