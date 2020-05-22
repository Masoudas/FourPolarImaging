package fr.fresnel.fourPolar.algorithm.preprocess.registration.descriptorBased;

import java.util.HashMap;

import fr.fresnel.fourPolar.core.preprocess.registration.IChannelRegistrationResult;
import fr.fresnel.fourPolar.core.preprocess.registration.RegistrationOrder;
import fr.fresnel.fourPolar.core.util.transform.AffineTransform2D;

class DescriptorBasedChannelRegistrationResult implements IChannelRegistrationResult {
    public final static String _NOT_ENOUGH_FP_DESCRIPTION = "Not enough feature points found.";
    public final static String _NO_TRANSFORMATION_DESCRIPTION = "No transformation found between images.";

    private HashMap<RegistrationOrder, Boolean> _isSuccessfulRegistration;
    private HashMap<RegistrationOrder, AffineTransform2D> _affineTransform;
    private HashMap<RegistrationOrder, String> _description;
    private HashMap<RegistrationOrder, Double> _error;

    public DescriptorBasedChannelRegistrationResult() {
        this._isSuccessfulRegistration = new HashMap<>();
        this._affineTransform = new HashMap<>();
        this._error = new HashMap<>();
        this._description = new HashMap<>();
    }

    public void setAffineTransform(RegistrationOrder order, AffineTransform2D affineTransform) {
        this._affineTransform.put(order, affineTransform);
    }

    public void setError(RegistrationOrder order, double error) {
        this._error.put(order, error);
    }

    public void setDescription(RegistrationOrder order, String description) {
        this._description.put(order, description);
    }

    public void setIsSuccessfulRegistration(RegistrationOrder order, boolean isSuccessful) {
        this._isSuccessfulRegistration.put(order, isSuccessful);
    }

    @Override
    public AffineTransform2D getAffineTransform(RegistrationOrder order) {
        return _affineTransform.get(order);
    }

    @Override
    public double error(RegistrationOrder order) {
        return this._error.get(order);
    }

    @Override
    public String getDescription(RegistrationOrder order) {
        return this._description.get(order);
    }

    @Override
    public boolean registrationSuccessful(RegistrationOrder order) {
        return this._isSuccessfulRegistration.get(order);
    }

}