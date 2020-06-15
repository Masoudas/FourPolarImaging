package fr.fresnel.fourPolar.algorithm.preprocess.darkBackground.estimator.percentile;

import fr.fresnel.fourPolar.algorithm.preprocess.darkBackground.estimator.IChannelDarkBackgroundEstimator;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.preprocess.darkBackground.IChannelDarkBackground;

/**
 * Estimates the dark background with the percentile method for two cameras. In
 * this constellation, the background of polarizations 0 and 90 and the
 * background of polarizations 45 and 135 are equal.
 */
class TwoCameraPercentileDarkBackgroundEstimator implements IChannelDarkBackgroundEstimator {
    private final int _percentileThreshold;

    public TwoCameraPercentileDarkBackgroundEstimator(int percentileThreshold) {
        this._percentileThreshold = percentileThreshold;
    }

    @Override
    public IChannelDarkBackground estimate(IPolarizationImageSet imageSet) {
        double pol0_90_Camerabackground = _estimateCameraPolarizationBackgrounds(imageSet,
                new Polarization[] { Polarization.pol0, Polarization.pol90 });
        double pol45_135_Camerabackground = _estimateCameraPolarizationBackgrounds(imageSet,
                new Polarization[] { Polarization.pol45, Polarization.pol135 });

        return new ChannelDarkBackground(imageSet.channel(), pol0_90_Camerabackground, pol45_135_Camerabackground,
                pol0_90_Camerabackground, pol45_135_Camerabackground);
    }

    /**
     * Estimate the polarization for the given camera (that contains polarizations).
     */
    private double _estimateCameraPolarizationBackgrounds(IPolarizationImageSet imageSet,
            Polarization[] polarizations) {
        return CameraPercentileBackgroundEstimator.estimate(imageSet, polarizations,
                this._percentileThreshold);
    }

}