package fr.fresnel.fourPolar.core.preprocess.registration;

import fr.fresnel.fourPolar.core.physics.channel.ChannelUtils;

/**
 * Encapsulates the registration result of the given bead set. Note that pol45,
 * pol90 and pol135 are all registered to pol0.
 */
public class BeadSetRegistrationResult {
    private final int _numChannels;
    private final IChannelRegistrationResult[] _channelResults;

    public BeadSetRegistrationResult(int numChannels) {
        ChannelUtils.checkNumChannelsNonZero(numChannels);
        this._numChannels = numChannels;

        this._channelResults = new IChannelRegistrationResult[numChannels];
    }

    /**
     * Set the result of registration of the given channel.
     */
    public void setResult(int channel, IChannelRegistrationResult registrationResult) {
        ChannelUtils.checkChannel(channel, this._numChannels);
        this._channelResults[channel - 1] = registrationResult;
    }

    /**
     * Return the result of registration for the given channel.
     */
    public IChannelRegistrationResult getResult(int channel) {
        ChannelUtils.checkChannel(channel, this._numChannels);
        return this._channelResults[channel - 1];
    }

}