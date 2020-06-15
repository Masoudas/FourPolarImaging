package fr.fresnel.fourPolar.algorithm.preprocess.darkBackground.estimator.percentile;

import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.preprocess.darkBackground.IChannelDarkBackground;

class ChannelDarkBackground implements IChannelDarkBackground {
    private final double _pol0;
    private final double _pol45;
    private final double _pol90;
    private final double _pol135;

    private final int _channel;

    /**
     * Single camera case.
     */
    public ChannelDarkBackground(int channel, double pol0_45_90_135) {
        this._channel = channel;

        _pol0 = pol0_45_90_135;
        _pol45 = pol0_45_90_135;
        _pol90 = pol0_45_90_135;
        _pol135 = pol0_45_90_135;
    }

    /**
     * Two camera case.
     */
    public ChannelDarkBackground(int channel, double pol0_90, double pol45_135) {
        this._channel = channel;

        _pol0 = pol0_90;
        _pol45 = pol45_135;
        _pol90 = pol0_90;
        _pol135 = pol45_135;
    }

    /**
     * Four camera case.
     */
    public ChannelDarkBackground(int channel, double pol0, double pol45, double pol90, double pol135) {
        this._channel = channel;

        _pol0 = pol0;
        _pol45 = pol45;
        _pol90 = pol90;
        _pol135 = pol135;
    }

    @Override
    public double getBackgroundLevel(Polarization polarization) {
        switch (polarization) {
            case pol0:
                return _pol0;

            case pol45:
                return _pol45;

            case pol90:
                return _pol90;

            case pol135:
                return _pol135;

            default:
                return -1;
        }
    }

    @Override
    public int channel() {
        return this._channel;
    }

    @Override
    public String estimationMethod() {
        return null;
    }

}