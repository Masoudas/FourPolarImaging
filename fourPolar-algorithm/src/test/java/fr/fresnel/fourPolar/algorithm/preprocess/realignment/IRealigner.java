package fr.fresnel.fourPolar.algorithm.preprocess.realignment;

import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;

public interface IRealigner {
    /**
     * Realigns the given polarization image set, using the corresponding
     * {@link IChannelRegistrationResult}.
     * 
     * @param imageSet
     */
    public void realign(IPolarizationImageSet imageSet);
}