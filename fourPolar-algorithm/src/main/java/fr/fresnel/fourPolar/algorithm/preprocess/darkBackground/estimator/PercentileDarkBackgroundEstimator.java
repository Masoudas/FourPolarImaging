package fr.fresnel.fourPolar.algorithm.preprocess.darkBackground.estimator;

import fr.fresnel.fourPolar.core.image.captured.ICapturedImageSet;
import fr.fresnel.fourPolar.core.preprocess.darkBackground.IChannelDarkBackground;

/**
 * A dark background estimator, that estimates the dark background by
 * calculating the n-th percentile of the intensity of the captured (bead) image
 * values.
 */
public class PercentileDarkBackgroundEstimator implements IChannelBackgroundEstimator {
    public PercentileDarkBackgroundEstimator(ICapturedImageSet imageSet) {

    }

    @Override
    public IChannelDarkBackground estimate(int channel) {
        return null;
    }

}