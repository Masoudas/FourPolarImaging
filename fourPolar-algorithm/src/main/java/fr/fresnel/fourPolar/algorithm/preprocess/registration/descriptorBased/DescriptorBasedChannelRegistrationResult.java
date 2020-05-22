package fr.fresnel.fourPolar.algorithm.preprocess.registration.descriptorBased;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.preprocess.registration.RegistrationOrder;
import fr.fresnel.fourPolar.core.preprocess.registration.IChannelRegistrationResult;
import fr.fresnel.fourPolar.core.util.transform.AffineTransform;
import fr.fresnel.fourPolar.core.util.transform.AffineTransform2D;

class DescriptorBasedChannelRegistrationResult implements IChannelRegistrationResult {
    private final static String _COLORED_RESULT_REPRESENTATION = "Color Representation of Registration";
    private final static String _MARKERED_RESULT_REPRESENTATION = "Marker Representation of Registration";

    public final static String _NOT_ENOUGH_FP_DESCRIPTION = "Not enough feature points found.";
    public final static String _NO_TRANSFORMATION_DESCRIPTION = "No transformation found between images.";

    private HashMap<RegistrationOrder, Boolean> _isSuccessfulRegistration;
    private HashMap<RegistrationOrder, AffineTransform2D> _affineTransform;
    private HashMap<RegistrationOrder, String> _description;
    private HashMap<RegistrationOrder, Double> _error;
    private HashMap<RegistrationOrder, HashMap<String, Image<RGB16>>> _resultFiles;

    public DescriptorBasedChannelRegistrationResult() {
        this._isSuccessfulRegistration = new HashMap<>();
        this._affineTransform = new HashMap<>();
        this._error = new HashMap<>();
        this._description = new HashMap<>();

        this._resultFiles = new HashMap<>();
        this._resultFiles.put(RegistrationOrder.Pol45_to_Pol0, new HashMap<>());
        this._resultFiles.put(RegistrationOrder.Pol90_to_Pol0, new HashMap<>());
        this._resultFiles.put(RegistrationOrder.Pol135_to_Pol0, new HashMap<>());
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

    public void setColoredRegistrationResultImage(RegistrationOrder order, Image<RGB16> image) {
        HashMap<String, Image<RGB16>> ruleMap = this._resultFiles.get(order);
        ruleMap.put(_COLORED_RESULT_REPRESENTATION, image);
    }

    public void setIsSuccessfulRegistration(RegistrationOrder order, boolean isSuccessful) {
        this._isSuccessfulRegistration.put(order, isSuccessful);
    }

    @Override
    public AffineTransform getAffineTransform(RegistrationOrder order) {
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
    public Map<String, Image<RGB16>> getResultFiles(RegistrationOrder order) {
        return this._resultFiles.get(order);
    }

    @Override
    public boolean registrationSuccessful(RegistrationOrder order) {
        return this._isSuccessfulRegistration.get(order);
    }

}