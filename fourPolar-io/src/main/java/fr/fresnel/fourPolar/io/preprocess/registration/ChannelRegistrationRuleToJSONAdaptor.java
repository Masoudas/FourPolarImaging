package fr.fresnel.fourPolar.io.preprocess.registration;

import com.fasterxml.jackson.annotation.JsonProperty;

import fr.fresnel.fourPolar.core.preprocess.registration.IChannelRegistrationResult;
import fr.fresnel.fourPolar.core.preprocess.registration.RegistrationRule;
import fr.fresnel.fourPolar.core.util.transform.AffineTransform;

class ChannelRegistrationRuleToJSONAdaptor {
    @JsonProperty("Rule")
    private String _ruleDescription;

    @JsonProperty("Rule Affine Transform")
    private double[][] _affineTransform;

    @JsonProperty("Rule Registration Error")
    private double _registrationError;


    public ChannelRegistrationRuleToJSONAdaptor(IChannelRegistrationResult result, RegistrationRule rule) {
        this._setRuleDescription(rule.description);
        this._setAffineTransform(result.getAffineTransform(rule).get());
        this._setRegistrationError(result.error(rule));
    }

    private void _setRuleDescription(String ruleDescription) {
        this._ruleDescription = ruleDescription;
    }

    private void _setAffineTransform(AffineTransform affineTransform) {
        this._affineTransform = affineTransform.get();
    }

    private void _setRegistrationError(double registrationError) {
        this._registrationError = registrationError;
    }

}