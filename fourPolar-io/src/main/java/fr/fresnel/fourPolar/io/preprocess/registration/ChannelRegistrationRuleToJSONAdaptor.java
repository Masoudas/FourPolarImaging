package fr.fresnel.fourPolar.io.preprocess.registration;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import fr.fresnel.fourPolar.core.preprocess.registration.IChannelRegistrationResult;
import fr.fresnel.fourPolar.core.preprocess.registration.RegistrationRule;
import fr.fresnel.fourPolar.core.util.transform.Affine2D;

class ChannelRegistrationRuleToJSONAdaptor {
    private static final int _DIGIT_ROUND_AFTERZERO = 2;

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
        this._registrationError = _roundToDecimalPlaces(registrationError);
    }

    private String _convertMatrixToString(Affine2D affine2d) {
        return "[[" + _roundToDecimalPlaces(affine2d.get(0, 0)) + ", " + _roundToDecimalPlaces(affine2d.get(0, 1))
                + ", " + _roundToDecimalPlaces(affine2d.get(0, 2)) + "], [" + _roundToDecimalPlaces(affine2d.get(1, 0))
                + ", " + _roundToDecimalPlaces(affine2d.get(1, 1)) + ", " + _roundToDecimalPlaces(affine2d.get(1, 2))
                + "]]";

    }

    private double _roundToDecimalPlaces(double value) {
        return new BigDecimal(value).setScale(_DIGIT_ROUND_AFTERZERO, BigDecimal.ROUND_HALF_EVEN).doubleValue();
    }

}