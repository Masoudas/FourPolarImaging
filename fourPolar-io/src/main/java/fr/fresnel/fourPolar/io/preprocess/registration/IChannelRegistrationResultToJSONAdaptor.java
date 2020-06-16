package fr.fresnel.fourPolar.io.preprocess.registration;

import java.util.TreeMap;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import fr.fresnel.fourPolar.core.preprocess.registration.IChannelRegistrationResult;
import fr.fresnel.fourPolar.core.preprocess.registration.RegistrationRule;

/**
 * Adaptor of {@link IChannelRegistrationResult} to
 */
@JsonSerialize(using = IChannelRegistrationResultJSONSerializer.class)
public class IChannelRegistrationResultToJSONAdaptor {
    private final TreeMap<RegistrationRule, ChannelRegistrationRuleToJSONAdaptor> _ruleAdapters;

    /**
     * Adapt this result to an object ready to be written to JSON.
     * 
     */
    public IChannelRegistrationResultToJSONAdaptor(IChannelRegistrationResult registrationResult) {
        this._ruleAdapters = this._createRegistrationRulesJSONAdaptor(registrationResult);
    }

    private TreeMap<RegistrationRule, ChannelRegistrationRuleToJSONAdaptor> _createRegistrationRulesJSONAdaptor(
            IChannelRegistrationResult registrationResult) {
        TreeMap<RegistrationRule, ChannelRegistrationRuleToJSONAdaptor> ruleAdapters = new TreeMap<>();
        for (RegistrationRule registrationRule : RegistrationRule.values()) {
            ChannelRegistrationRuleToJSONAdaptor JSONAdaptor = this._createRegistrationRuleAdaptor(registrationResult,
                    registrationRule);
            ruleAdapters.put(registrationRule, JSONAdaptor);
        }

        return ruleAdapters;
    }

    private ChannelRegistrationRuleToJSONAdaptor _createRegistrationRuleAdaptor(
            IChannelRegistrationResult registrationResult, RegistrationRule registrationRule) {
        return new ChannelRegistrationRuleToJSONAdaptor(registrationResult, registrationRule);
    }

    public ChannelRegistrationRuleToJSONAdaptor getRuleAdaptor(RegistrationRule rule) {
        return _ruleAdapters.get(rule);

    }

}