package fr.fresnel.fourPolar.algorithm.preprocess.darkBackground.estimator.percentile;

import java.util.HashMap;

import fr.fresnel.fourPolar.algorithm.preprocess.darkBackground.estimator.IChannelDarkBackgroundEstimator;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.preprocess.darkBackground.IChannelDarkBackground;

/**
 * Estimates the dark background with the percentile method for one cameras. In
 * this constellation, the background of polarization is equal to the rest of
 * polarizations.
 */
class OneCameraPercentileDarkBackgroundEstimator implements IChannelDarkBackgroundEstimator {
    private final int _percentileThreshold;

    public OneCameraPercentileDarkBackgroundEstimator(int percentileThreshold) {
        this._percentileThreshold = percentileThreshold;
    }

    @Override
    public IChannelDarkBackground estimate(IPolarizationImageSet imageSet) {
        double pol0_45_90_135_CameraBackgrounds = this._estimateCameraPolarizationBackgrounds(imageSet,
                Polarization.values());

        return new ChannelDarkBackground(imageSet.channel(), pol0_45_90_135_CameraBackgrounds,
                pol0_45_90_135_CameraBackgrounds, pol0_45_90_135_CameraBackgrounds, pol0_45_90_135_CameraBackgrounds);
    }

    /**
     * Estimate the polarization for the given camera (that contains polarizations).
     */
    private double _estimateCameraPolarizationBackgrounds(IPolarizationImageSet imageSet,
            Polarization[] polarizations) {
        return CameraPercentileBackgroundEstimator.estimate(imageSet, polarizations, this._percentileThreshold);
    }

}