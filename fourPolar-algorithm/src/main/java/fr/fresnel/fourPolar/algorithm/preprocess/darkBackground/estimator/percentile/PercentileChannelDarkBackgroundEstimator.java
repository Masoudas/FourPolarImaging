package fr.fresnel.fourPolar.algorithm.preprocess.darkBackground.estimator.percentile;

import java.util.Objects;

import fr.fresnel.fourPolar.algorithm.preprocess.darkBackground.estimator.IChannelDarkBackgroundEstimator;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
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
public class PercentileChannelDarkBackgroundEstimator implements IChannelDarkBackgroundEstimator {
    /**
     * The percentile in the histogram of intensity values that corresponds to
     * noise.
     */
    private static int PERCENTILE_THRESHOLD = 10;

    private final IChannelDarkBackgroundEstimator _estimator;

    public PercentileChannelDarkBackgroundEstimator(Cameras cameras) {
        Objects.requireNonNull(cameras, "cameras can't be null");

        this._estimator = this._chooseSegmenter(cameras);
    }

    private IChannelDarkBackgroundEstimator _chooseSegmenter(Cameras cameras) {
        switch (cameras) {
            case One:
                return new OneCameraPercentileDarkBackgroundEstimator(PERCENTILE_THRESHOLD);

            case Two:
                return new TwoCameraPercentileDarkBackgroundEstimator(PERCENTILE_THRESHOLD);

            case Four:
                return new FourCameraPercentileDarkBackgroundEstimator(PERCENTILE_THRESHOLD);

            default:
                return null;
        }
    }

    @Override
    public IChannelDarkBackground estimate(IPolarizationImageSet imageSet) {
        return this._estimator.estimate(imageSet);
    }

}