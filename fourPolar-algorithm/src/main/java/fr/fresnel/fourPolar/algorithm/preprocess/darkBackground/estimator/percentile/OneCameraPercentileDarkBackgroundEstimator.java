package fr.fresnel.fourPolar.algorithm.preprocess.darkBackground.estimator.percentile;

import fr.fresnel.fourPolar.algorithm.preprocess.darkBackground.estimator.ChannelDarkBackground;
import fr.fresnel.fourPolar.algorithm.preprocess.darkBackground.estimator.IChannelBackgroundEstimator;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.preprocess.darkBackground.IChannelDarkBackground;

/**
 * Estimates the dark background with the percentile method for one cameras. In
 * this constellation, the background of polarization is equal to the rest of
 * polarizations.
 */
public class OneCameraPercentileDarkBackgroundEstimator implements IChannelBackgroundEstimator {
    private final int _percentileThreshold;

    public OneCameraPercentileDarkBackgroundEstimator(int percentileThreshold) {
        this._percentileThreshold = percentileThreshold;
    }

    @Override
    public IChannelDarkBackground estimate(IPolarizationImageSet imageSet) {
        Image<UINT16> pol0 = imageSet.getPolarizationImage(Polarization.pol0).getImage();
        double[] pol0AsArray = PercentileDarkBackgroundUtil.getFirstPlaneAsArray(pol0);

        Image<UINT16> pol45 = imageSet.getPolarizationImage(Polarization.pol45).getImage();
        double[] pol45AsArray = PercentileDarkBackgroundUtil.getFirstPlaneAsArray(pol45);

        Image<UINT16> pol90 = imageSet.getPolarizationImage(Polarization.pol90).getImage();
        double[] pol90AsArray = PercentileDarkBackgroundUtil.getFirstPlaneAsArray(pol90);

        Image<UINT16> pol135 = imageSet.getPolarizationImage(Polarization.pol135).getImage();
        double[] pol135AsArray = PercentileDarkBackgroundUtil.getFirstPlaneAsArray(pol135);

        double background = PercentileDarkBackgroundUtil.computePercentile(pol0AsArray, pol45AsArray, pol90AsArray, pol135AsArray,
                this._percentileThreshold);
        
        return new ChannelDarkBackground(imageSet.channel(), background);
    }

}