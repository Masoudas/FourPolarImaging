package fr.fresnel.fourPolar.io.preprocess.registration;

import com.fasterxml.jackson.annotation.JsonProperty;

import fr.fresnel.fourPolar.core.preprocess.registration.IChannelRegistrationResult;
import fr.fresnel.fourPolar.core.preprocess.registration.RegistrationRule;
import fr.fresnel.fourPolar.core.util.transform.Affine2D;

class ChannelRegistrationRuleToJSONAdaptor {
    @JsonProperty("Affine Transform")
    private String _affineTransformAsString;

    @JsonProperty("Registration Error")
    private double _registrationError;

    public ChannelRegistrationRuleToJSONAdaptor(IChannelRegistrationResult result, RegistrationRule rule) {
        this._setAffineTransform(result.getAffineTransform(rule));
        this._setRegistrationError(result.error(rule));
    }

    private void _setAffineTransform(Affine2D affine2d) {
        this._affineTransformAsString = this._convertMatrixToString(affine2d);
    }

    private void _setRegistrationError(double registrationError) {
        this._registrationError = registrationError;
    }

    private String _convertMatrixToString(Affine2D affine2d) {
        return "[[" + affine2d.get(0, 0) + ", " + affine2d.get(0, 1) + ", " + affine2d.get(0, 2) + "], ["
                + affine2d.get(1, 0) + ", " + affine2d.get(1, 1) + ", " + affine2d.get(1, 2) + "]]";

    }

}