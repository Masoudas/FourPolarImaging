package fr.fresnel.fourPolar.core.preprocess;

import java.util.Objects;

import fr.fresnel.fourPolar.core.physics.channel.ChannelUtils;
import fr.fresnel.fourPolar.core.preprocess.darkBackground.IChannelDarkBackground;
import fr.fresnel.fourPolar.core.preprocess.registration.IChannelRegistrationResult;

/**
 * Encapsulates the results of all the analysis performed on the captured image
 * set (be it image bead set or other designated set that may be used for
 * preprocess).
 */
public class PreprocessResult {
    private final int _numChannels;

    private final IChannelRegistrationResult[] _channelRegistrationResults;
    private final IChannelDarkBackground[] _channelDarkBackground;

    public PreprocessResult(int numChannels) {
        ChannelUtils.checkNumChannelsNonZero(numChannels);
        this._numChannels = numChannels;

        this._channelRegistrationResults = new IChannelRegistrationResult[numChannels];
        this._channelDarkBackground = new IChannelDarkBackground[numChannels];
    }

    /**
     * Set the result of registration of the given channel.
     */
    public void setRegistrationResult(int channel, IChannelRegistrationResult registrationResult) {
        Objects.requireNonNull(registrationResult, "Registration result can't be null for channel + " + channel);
        ChannelUtils.checkChannel(channel, this._numChannels);
        this._channelRegistrationResults[channel - 1] = registrationResult;
    }

    public void setDarkBackground(int channel, IChannelDarkBackground darkBackground) {
        Objects.requireNonNull(darkBackground, "Channel dark background can't be null for channel + " + channel);
        ChannelUtils.checkChannel(channel, this._numChannels);
        this._channelDarkBackground[channel - 1] = darkBackground;
    }

    /**
     * Return the result of registration for the given channel.
     */
    public IChannelRegistrationResult getRegistrationResult(int channel) {
        ChannelUtils.checkChannel(channel, this._numChannels);
        return this._channelRegistrationResults[channel - 1];
    }

    public IChannelDarkBackground getDarkBackground(int channel) {
        ChannelUtils.checkChannel(channel, this._numChannels);
        return this._channelDarkBackground[channel - 1];
    }

}