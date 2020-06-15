package fr.fresnel.fourPolar.io.preprocess;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import fr.fresnel.fourPolar.core.preprocess.darkBackground.IChannelDarkBackground;
import fr.fresnel.fourPolar.core.preprocess.registration.IChannelRegistrationResult;
import fr.fresnel.fourPolar.io.preprocess.darkBackground.IChannelDarkBackgroundToJSONAdaptor;
import fr.fresnel.fourPolar.io.preprocess.registration.IChannelRegistrationResultToJSONAdaptor;

/**
 * An adaptor for writing registration results of one channel to disk.
 */
@JsonSerialize(using = ChannelRegistrationSetProcessResultJSONSerializer.class)
class ChannelRegistrationSetProcessResultToJSONAdaptor {
    private final IChannelDarkBackgroundToJSONAdaptor _darkBackgroundAdaptor;

    private final IChannelRegistrationResultToJSONAdaptor _registrationResultAdaptor;

    public ChannelRegistrationSetProcessResultToJSONAdaptor(IChannelRegistrationResult registrationResult,
            IChannelDarkBackground darkBackground) {
        this._checkRegistrationAndBackgroundAreFromSameChannel(registrationResult, darkBackground);

        _darkBackgroundAdaptor = _createDarkBackgroundJSONAdaptor(darkBackground);
        _registrationResultAdaptor = _createRegistrationResultJSONAdaptor(registrationResult);

    }

    private void _checkRegistrationAndBackgroundAreFromSameChannel(IChannelRegistrationResult registrationResult,
            IChannelDarkBackground darkBackground) {
        if (registrationResult.channel() != darkBackground.channel()) {
            throw new IllegalArgumentException("Dark background and registration result don't belong to same channel.");
        }

    }

    private IChannelDarkBackgroundToJSONAdaptor _createDarkBackgroundJSONAdaptor(
            IChannelDarkBackground darkBackground) {
        return new IChannelDarkBackgroundToJSONAdaptor(darkBackground);
    }

    private IChannelRegistrationResultToJSONAdaptor _createRegistrationResultJSONAdaptor(
            IChannelRegistrationResult registrationResult) {
        return new IChannelRegistrationResultToJSONAdaptor(registrationResult);
    }

    public IChannelDarkBackgroundToJSONAdaptor getDarkBackgroundJSONAdaptor() {
        return this._darkBackgroundAdaptor;
    }

    public IChannelRegistrationResultToJSONAdaptor getRegistrationResultJSONAdaptor() {
        return this._registrationResultAdaptor;
    }

}