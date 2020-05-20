package fr.fresnel.fourPolar.core.preprocess.registration;

import fr.fresnel.fourPolar.core.physics.channel.ChannelUtils;

/**
 * Encapsulates the registration result of the given bead set. Note that pol45,
 * pol90 and pol135 are all registered to pol0.
 */
public class BeadSetRegistrationResult {
    private final int _numChannels;
    private final IRegistrationResult[] _pol45_pol0;
    private final IRegistrationResult[] _pol90_pol0;
    private final IRegistrationResult[] _pol135_pol0;

    public BeadSetRegistrationResult(int numChannels) {
        ChannelUtils.checkNumChannelsNonZero(numChannels);
        this._numChannels = numChannels;

        this._pol45_pol0 = new IRegistrationResult[numChannels];
        this._pol90_pol0 = new IRegistrationResult[numChannels];
        this._pol135_pol0 = new IRegistrationResult[numChannels];
    }

    /**
     * Set the result of registration of pol45 to pol0 for the given channel.
     */
    public void setResultPol45(int channel, IRegistrationResult registrationResult) {
        ChannelUtils.checkChannel(channel, this._numChannels);
        this._pol45_pol0[channel - 1] = registrationResult;
    }

    /**
     * Set the result of registration of pol90 to pol0 for the given channel.
     */
    public void setResultPol90(int channel, IRegistrationResult registrationResult) {
        ChannelUtils.checkChannel(channel, this._numChannels);
        this._pol90_pol0[channel - 1] = registrationResult;
    }

    /**
     * Set the result of registration of pol135 to pol0 for the given channel.
     */
    public void setResultPol135(int channel, IRegistrationResult registrationResult) {
        ChannelUtils.checkChannel(channel, this._numChannels);
        this._pol135_pol0[channel - 1] = registrationResult;
    }

    /**
     * Return the result of registration of pol45 to pol0 for the given channel.
     */
    public IRegistrationResult getResultPol45(int channel) {
        ChannelUtils.checkChannel(channel, this._numChannels);
        return this._pol45_pol0[channel - 1];
    }

    /**
     * Return the result of registration of pol90 to pol0 for the given channel.
     */
    public IRegistrationResult getResultPol90(int channel) {
        ChannelUtils.checkChannel(channel, this._numChannels);
        return this._pol90_pol0[channel - 1];
    }

    /**
     * Return the result of registration of pol90 to pol0 for the given channel.
     */
    public IRegistrationResult getResultPol135(int channel) {
        ChannelUtils.checkChannel(channel, this._numChannels);
        return this._pol135_pol0[channel - 1];
    }

}