package fr.fresnel.fourPolar.algorithm.preprocess.darkBackground;

import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;

/**
 * An interface for removing the dark background from the given
 * {@link IPolarizationImageSet}.
 */
public interface IChannelDarkBackgroundRemover {
    public void remove(IPolarizationImageSet imageSet);
}