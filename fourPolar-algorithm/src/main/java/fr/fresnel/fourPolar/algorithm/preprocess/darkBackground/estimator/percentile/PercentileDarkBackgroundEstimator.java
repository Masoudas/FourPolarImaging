package fr.fresnel.fourPolar.algorithm.preprocess.darkBackground.estimator.percentile;

import fr.fresnel.fourPolar.algorithm.preprocess.darkBackground.estimator.IChannelBackgroundEstimator;
import fr.fresnel.fourPolar.core.image.captured.ICapturedImageSet;
import fr.fresnel.fourPolar.core.preprocess.darkBackground.IChannelDarkBackground;

/**
 * A dark background estimator, that estimates the dark background by
 * calculating the n-th percentile of the intensity of the captured (bead) image
 * values, and then setting all pixels below that value to zero, and subtracting
 * that percentile value from all values that are above this value.
 * <p>
 * To estimate the background, note that the estimator uses only the first xy
 * plane of an image. This is because the noise does not change for possible z
 * or t points.
 */
public class PercentileDarkBackgroundEstimator implements IChannelBackgroundEstimator {
    /**
     * The percentile in the histogram of intensity values that corresponds to
     * noise.
     */
    private static int _backgroundPercentile = 10;

    public PercentileDarkBackgroundEstimator(ICapturedImageSet imageSet) {

    }

    @Override
    public IChannelDarkBackground estimate(int channel) {
        return null;
    }

}