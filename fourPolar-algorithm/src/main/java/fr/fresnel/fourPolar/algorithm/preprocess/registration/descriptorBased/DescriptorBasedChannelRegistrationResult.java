package fr.fresnel.fourPolar.algorithm.preprocess.registration.descriptorBased;

import java.util.HashMap;

import fr.fresnel.fourPolar.core.preprocess.registration.IChannelRegistrationResult;
import fr.fresnel.fourPolar.core.preprocess.registration.RegistrationRule;
import fr.fresnel.fourPolar.core.util.transform.Affine2D;

class DescriptorBasedChannelRegistrationResult implements IChannelRegistrationResult {
    public final static String _NOT_ENOUGH_FP_DESCRIPTION = "Not enough feature points found.";
    public final static String _NO_TRANSFORMATION_DESCRIPTION = "No (invertible) transformation found between images.";

    private HashMap<RegistrationRule, Boolean> _isSuccessfulRegistration;
    private HashMap<RegistrationRule, Affine2D> _affineTransform;
    private HashMap<RegistrationRule, String> _description;
    private HashMap<RegistrationRule, Double> _error;
    private int _channelNum = 0;

    public DescriptorBasedChannelRegistrationResult() {
        this._isSuccessfulRegistration = _initializeIsSuccessfulRegistrationToFalse();
        this._affineTransform = new HashMap<>();
        this._error = new HashMap<>();
        this._description = new HashMap<>();
    }

    /**
     * All inital results are set to false, so that if the registration algorithm
     * fails with unknown exception, is successful will be false.
     * 
     * @return
     */
    private HashMap<RegistrationRule, Boolean> _initializeIsSuccessfulRegistrationToFalse() {
        HashMap<RegistrationRule, Boolean> isSuccessful = new HashMap<>();
        for (RegistrationRule rule : RegistrationRule.values()) {
            isSuccessful.put(rule, false);
        }
        return isSuccessful;
    }

    public void setAffineTransform(RegistrationRule order, Affine2D affineTransform) {
        this._affineTransform.put(order, affineTransform);
    }

    public void setError(RegistrationRule order, double error) {
        this._error.put(order, error);
    }

    public void setDescription(RegistrationRule order, String description) {
        this._description.put(order, description);
    }

    public void setIsSuccessfulRegistration(RegistrationRule order, boolean isSuccessful) {
        this._isSuccessfulRegistration.put(order, isSuccessful);
    }

    public void setChannel(int channelNum) {
        this._channelNum = channelNum;
    }

    @Override
    public Affine2D getAffineTransform(RegistrationRule order) {
        return _affineTransform.get(order);
    }

    @Override
    public double error(RegistrationRule order) {
        return this._error.get(order);
    }

    @Override
    public String getFailureDescription(RegistrationRule order) {
        return this._description.get(order);
    }

    @Override
    public boolean registrationSuccessful(RegistrationRule order) {
        return this._isSuccessfulRegistration.get(order);
    }

    @Override
    public int channel() {
        return this._channelNum;
    }

}