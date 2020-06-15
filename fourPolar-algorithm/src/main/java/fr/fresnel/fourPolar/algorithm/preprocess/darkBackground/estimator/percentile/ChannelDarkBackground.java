package fr.fresnel.fourPolar.algorithm.preprocess.darkBackground.estimator.percentile;

import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.preprocess.darkBackground.IChannelDarkBackground;

class ChannelDarkBackground implements IChannelDarkBackground {
    private final PercentileChannelDarkBackgroundEstimator _estimator;

    public ChannelDarkBackground(PercentileChannelDarkBackgroundEstimator estimator) {
        this._estimator = estimator;
    }

    @Override
    public double getBackgroundLevel(Polarization polarization) {
        return this._estimator.getBackgroundLevel(polarization);
    }

    @Override
    public int channel() {
        return this._estimator.channel();
    }

    @Override
    public String estimationMethod() {
        return this._estimator.getDescription();
    }

}