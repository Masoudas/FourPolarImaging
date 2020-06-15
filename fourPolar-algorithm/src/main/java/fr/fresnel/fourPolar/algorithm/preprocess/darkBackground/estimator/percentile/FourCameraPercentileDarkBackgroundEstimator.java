package fr.fresnel.fourPolar.algorithm.preprocess.darkBackground.estimator.percentile;

import fr.fresnel.fourPolar.algorithm.preprocess.darkBackground.estimator.IChannelDarkBackgroundEstimator;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.preprocess.darkBackground.IChannelDarkBackground;

/**
 * Estimates the dark background with the percentile method for four cameras. In
 * this constellation, the background of each camera is independent from the
 * others.
 */
class FourCameraPercentileDarkBackgroundEstimator implements IChannelDarkBackgroundEstimator {
    private final int _percentileThreshold;

    public FourCameraPercentileDarkBackgroundEstimator(int percentileThreshold) {
        this._percentileThreshold = percentileThreshold;
    }

    @Override
    public IChannelDarkBackground estimate(IPolarizationImageSet imageSet) {
        double pol0Camerabackground = _estimateCameraPolarizationBackgrounds(imageSet,
                new Polarization[] { Polarization.pol0 });
        double pol45Camerabackground = _estimateCameraPolarizationBackgrounds(imageSet,
                new Polarization[] { Polarization.pol45 });
        double pol90Camerabackground = _estimateCameraPolarizationBackgrounds(imageSet,
                new Polarization[] { Polarization.pol90 });
        double pol135Camerabackground = _estimateCameraPolarizationBackgrounds(imageSet,
                new Polarization[] { Polarization.pol135 });

        return new ChannelDarkBackground(imageSet.channel(), pol0Camerabackground, pol45Camerabackground,
                pol90Camerabackground, pol135Camerabackground);
    }

    /**
     * Estimate the polarization for the given camera (that contains polarizations).
     */
    private double _estimateCameraPolarizationBackgrounds(IPolarizationImageSet imageSet,
            Polarization[] polarizations) {
        return CameraPercentileBackgroundEstimator.estimate(imageSet, polarizations, this._percentileThreshold);
    }

}