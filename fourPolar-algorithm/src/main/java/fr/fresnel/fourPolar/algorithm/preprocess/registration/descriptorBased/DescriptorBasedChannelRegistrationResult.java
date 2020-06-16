package fr.fresnel.fourPolar.algorithm.preprocess.registration.descriptorBased;

import java.util.HashMap;

import fr.fresnel.fourPolar.core.preprocess.registration.IChannelRegistrationResult;
import fr.fresnel.fourPolar.core.preprocess.registration.RegistrationRule;
import fr.fresnel.fourPolar.core.util.transform.Affine2D;

class DescriptorBasedChannelRegistrationResult implements IChannelRegistrationResult {
    private HashMap<RegistrationRule, Affine2D> _affineTransform;
    private HashMap<RegistrationRule, Double> _error;
    private int _channelNum = 0;

    public DescriptorBasedChannelRegistrationResult() {
        this._affineTransform = new HashMap<>();
        this._error = new HashMap<>();
    }

    public void setAffineTransform(RegistrationRule order, Affine2D affineTransform) {
        this._affineTransform.put(order, affineTransform);
    }

    public void setError(RegistrationRule order, double error) {
        this._error.put(order, error);
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
    public int channel() {
        return this._channelNum;
    }

    @Override
    public String registrationMethod() {
        return DescriptorBasedRegistration.METHOD_DESCRIPTION;
    }

}