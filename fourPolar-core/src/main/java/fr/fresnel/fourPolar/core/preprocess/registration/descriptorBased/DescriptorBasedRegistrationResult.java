package fr.fresnel.fourPolar.core.preprocess.registration.descriptorBased;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import fr.fresnel.fourPolar.core.preprocess.registration.IRegistrationResult;
import fr.fresnel.fourPolar.core.util.transform.AffineTransform;
import fr.fresnel.fourPolar.core.util.transform.AffineTransform2D;

public class DescriptorBasedRegistrationResult implements IRegistrationResult {
    private final static String _COLORED_RESULT_REPRESENTATION = "Color Representation of Registration";
    private final static String _MARKERED_RESULT_REPRESENTATION = "Marker Representation of Registration";

    private final static String _NO_INLIERS_FOUND_DESCRIPTION = "Not enough feature points";

    private boolean _isSuccessfulRegistration;
    private AffineTransform2D _affineTransform;
    private double _error;
    private int _percentInlierDescriptors;
    private HashMap<String, File> _resultFiles = new HashMap<String, File>();

    public DescriptorBasedRegistrationResult() {
        this._isSuccessfulRegistration = false;
        this._affineTransform = new AffineTransform2D();
        this._error = -1;
        this._percentInlierDescriptors = 0;
        this._resultFiles = new HashMap<>();
    }

    public void setAffineTransform(AffineTransform2D affineTransform) {
        this._affineTransform = affineTransform;
    }

    public void setError(double error) {
        this._error = error;
    }

    public void setPrecentageInlierDescriptors(int n) {
        this._percentInlierDescriptors = n;
    }

    public void setColoredRegistrationResultImage(File file) {
        this._resultFiles.put(_COLORED_RESULT_REPRESENTATION, file);
    }

    public void setMarkeredRegistrationResultImage(File file) {
        this._resultFiles.put(_MARKERED_RESULT_REPRESENTATION, file);
    }

    public void setIsSuccessfulRegistration(boolean isSuccessful) {
        this._isSuccessfulRegistration = isSuccessful;
    }

    @Override
    public AffineTransform getAffineTransform() {
        return _affineTransform;
    }

    @Override
    public double error() {
        return this._error;
    }

    @Override
    public String getDescription() {
        if (this._percentInlierDescriptors == 0) {
            return _NO_INLIERS_FOUND_DESCRIPTION;
        } else {
            return "%Inlier points= " + this._percentInlierDescriptors;
        }
    }

    @Override
    public Map<String, File> getResultFiles() {
        return this._resultFiles;
    }

    @Override
    public boolean registrationSuccessful() {
        return this._isSuccessfulRegistration;
    }


}