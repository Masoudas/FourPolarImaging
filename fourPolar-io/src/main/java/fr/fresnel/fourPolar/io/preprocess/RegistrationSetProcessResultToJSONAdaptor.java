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

    public RegistrationSetProcessResultToJSONAdaptor(IFourPolarImagingSetup imagingSetup,
            RegistrationSetProcessResult result) {
        this._channelAdaptors = this._createChannelsResultJSONAdaptor(result, imagingSetup.getNumChannel());
    }

    public TreeMap<String, ChannelRegistrationSetProcessResultToJSONAdaptor> _createChannelsResultJSONAdaptor(
            RegistrationSetProcessResult result, int numChannels) {
        TreeMap<String, ChannelRegistrationSetProcessResultToJSONAdaptor> channelAdaptors = new TreeMap<>();
        for (int channel = 1; channel <= numChannels; channel++) {
            IChannelDarkBackground channelDarkBackground = result.getDarkBackground(channel);
            IChannelRegistrationResult registrationResult = result.getRegistrationResult(channel);
            String channelLabel = this._channelAsString(channel);

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

    public ChannelRegistrationSetProcessResultToJSONAdaptor getChannelResultAdaptor(String channelLabel){
        return this._channelAdaptors.get(channelLabel);
    }

}