package fr.fresnel.fourPolar.algorithm.preprocess.darkBackground.estimator;

import fr.fresnel.fourPolar.core.preprocess.darkBackground.IChannelDarkBackground;

/**
 * An interface for estimating the dark background of a channel.
 */
public interface IChannelBackgroundEstimator {
    /**
     * Estimate the background for the given channel.
     */
    public IChannelDarkBackground estimate(int channel);
}