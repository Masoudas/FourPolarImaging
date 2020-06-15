package fr.fresnel.fourPolar.io.preprocess.registration;

import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonProperty;

import fr.fresnel.fourPolar.core.preprocess.registration.IChannelRegistrationResult;
import fr.fresnel.fourPolar.core.preprocess.registration.RegistrationRule;

/**
 * Adaptor of {@link IChannelRegistrationResult} to
 */
public class IChannelRegistrationResultToJSONAdaptor {
    @JsonProperty("Registration Result")
    TreeMap<RegistrationRule, ChannelRegistrationRuleToJSONAdaptor> _ruleAdapters;

    /**
     * Adapt this result to an object ready to be written to JSON.
     * 
     */
    public IChannelRegistrationResultToJSONAdaptor(IChannelRegistrationResult registrationResult) {
        this._ruleAdapters = new TreeMap<>();
        this._createRegistrationRulesJSONAdaptor(registrationResult);
    }

    private void _createRegistrationRulesJSONAdaptor(IChannelRegistrationResult registrationResult) {
        for (RegistrationRule registrationRule : RegistrationRule.values()) {
            ChannelRegistrationRuleToJSONAdaptor JSONAdaptor = this._createRegistrationRuleAdaptor(registrationResult,
                    registrationRule);
            this._ruleAdapters.put(registrationRule, JSONAdaptor);
        }
    }

    private ChannelRegistrationRuleToJSONAdaptor _createRegistrationRuleAdaptor(
            IChannelRegistrationResult registrationResult, RegistrationRule registrationRule) {
        return new ChannelRegistrationRuleToJSONAdaptor(registrationResult, registrationRule);
    }

}