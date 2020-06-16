package fr.fresnel.fourPolar.io.preprocess;

import java.util.Iterator;
import java.util.TreeMap;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import fr.fresnel.fourPolar.core.imagingSetup.IFourPolarImagingSetup;
import fr.fresnel.fourPolar.core.physics.channel.ChannelUtils;
import fr.fresnel.fourPolar.core.preprocess.RegistrationSetProcessResult;
import fr.fresnel.fourPolar.core.preprocess.darkBackground.IChannelDarkBackground;
import fr.fresnel.fourPolar.core.preprocess.registration.IChannelRegistrationResult;

/**
 * An adaptor that adapts a {@link RegistrationSetProcessResult} to an object
 * ready to be written as JSON.
 */
@JsonSerialize(using = RegistrationSetProcessResultJSONSerializer.class)
class RegistrationSetProcessResultToJSONAdaptor {
    private final TreeMap<String, ChannelRegistrationSetProcessResultToJSONAdaptor> _channelAdaptors;
    private final String _registrationMethod;
    private final String _backgroundEstimationMethod;

    public RegistrationSetProcessResultToJSONAdaptor(IFourPolarImagingSetup imagingSetup,
            RegistrationSetProcessResult result) {
        this._channelAdaptors = this._createChannelsResultJSONAdaptor(result, imagingSetup.getNumChannel());
        
        this._backgroundEstimationMethod = _getBackgroundEstimationMethod(result);
        this._registrationMethod = this._getRegistrationMethod(result);
    }

    /**
     * Returns the background estimation method. Given that the method is the same
     * across all channels, we return the instance for channel 1.
     */
    private String _getBackgroundEstimationMethod(RegistrationSetProcessResult result) {
        return result.getDarkBackground(1).estimationMethod();
    }

    /**
     * Returns the registration method. Given that this method is the same across all channels,
     * we use only the method name for channel 1.
     */
    private String _getRegistrationMethod(RegistrationSetProcessResult result) {
        return result.getRegistrationResult(1).registrationMethod();
    }

    private TreeMap<String, ChannelRegistrationSetProcessResultToJSONAdaptor> _createChannelsResultJSONAdaptor(
            RegistrationSetProcessResult result, int numChannels) {
        TreeMap<String, ChannelRegistrationSetProcessResultToJSONAdaptor> channelAdaptors = new TreeMap<>();
        for (int channel = 1; channel <= numChannels; channel++) {
            String channelLabel = this._channelAsString(channel);

            IChannelDarkBackground channelDarkBackground = result.getDarkBackground(channel);
            IChannelRegistrationResult registrationResult = result.getRegistrationResult(channel);

            channelAdaptors.put(channelLabel,
                    this._createChannelJSONAdaptor(channelDarkBackground, registrationResult));
        }

        return channelAdaptors;
    }

    private ChannelRegistrationSetProcessResultToJSONAdaptor _createChannelJSONAdaptor(
            IChannelDarkBackground channelDarkBackground, IChannelRegistrationResult registrationResult) {
        return new ChannelRegistrationSetProcessResultToJSONAdaptor(registrationResult, channelDarkBackground);
    }

    private String _channelAsString(int channel) {
        return ChannelUtils.channelAsString(channel);
    };

    public Iterator<String> getChannelLabels() {
        return this._channelAdaptors.keySet().iterator();
    }

    public ChannelRegistrationSetProcessResultToJSONAdaptor getChannelResultAdaptor(String channelLabel) {
        return this._channelAdaptors.get(channelLabel);
    }

    public String getRegistrationMethod() {
        return this._registrationMethod;
    }

    public String getBackgroundEstimationMethod(){
        return this._backgroundEstimationMethod;
    }

}