package fr.fresnel.fourPolar.io.preprocess.registration;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonProperty;

import fr.fresnel.fourPolar.core.preprocess.registration.IChannelRegistrationResult;
import fr.fresnel.fourPolar.core.preprocess.registration.RegistrationRule;

public class IChannelRegistrationResultToJSONAdapter {
    @JsonProperty("Registration Result")
    HashMap<RegistrationRule, ChannelRegistrationRuleToJSONAdaptor> _ruleAdapters;

    public IChannelRegistrationResultToJSONAdapter() {
        this._ruleAdapters = new HashMap<>();
    }

    public void toJSON(IChannelRegistrationResult registrationResult) {
        _createChannelsJSONAdaptor(registrationResult);
    }

    private void _createChannelsJSONAdaptor(IChannelRegistrationResult registrationResult) {
        for (RegistrationRule registrationRule : RegistrationRule.values()) {
            ChannelRegistrationRuleToJSONAdaptor JSONAdaptor = this._createChannelAdaptor(registrationResult,
                    registrationRule);
            this._ruleAdapters.put(registrationRule, JSONAdaptor);
        }
    }

    private ChannelRegistrationRuleToJSONAdaptor _createChannelAdaptor(IChannelRegistrationResult registrationResult,
            RegistrationRule registrationRule) {
        ChannelRegistrationRuleToJSONAdaptor adaptor = new ChannelRegistrationRuleToJSONAdaptor();
        adaptor.toJSON(registrationResult, registrationRule);
        return adaptor;
    }

}