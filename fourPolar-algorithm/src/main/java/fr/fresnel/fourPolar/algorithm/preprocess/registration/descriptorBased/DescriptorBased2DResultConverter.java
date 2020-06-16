package fr.fresnel.fourPolar.algorithm.preprocess.registration.descriptorBased;

import java.util.Hashtable;

import fr.fresnel.fourPolar.core.preprocess.registration.IChannelRegistrationResult;
import fr.fresnel.fourPolar.core.preprocess.registration.RegistrationRule;
import fr.fresnel.fourPolar.core.util.transform.Affine2D;
import registration.descriptorBased.result.DescriptorBased2DResult;
import registration.descriptorBased.result.DescriptorBased2DResult.FailureCause;

/**
 * Wraps a {@link DescriptorBased2DResult} of the three polarizations into an
 * {@link DescriptorBasedChannelRegistrationResult}.
 */
class DescriptorBased2DResultConverter {
    public final static String _NOT_ENOUGH_FP_DESCRIPTION = "Not enough feature points found.";
    public final static String _NO_TRANSFORMATION_DESCRIPTION = "No (invertible) transformation found between images.";

    private Hashtable<RegistrationRule, DescriptorBased2DResult> _results;
    private final int _channelNum;

    public static String _convertFailureCauseToString(FailureCause failureCause) {
        if (failureCause == FailureCause.NOT_ENOUGH_FP) {
            return _NOT_ENOUGH_FP_DESCRIPTION;
        } else if (failureCause == FailureCause.NO_INLIER_AFTER_RANSAC) {
            return _NO_TRANSFORMATION_DESCRIPTION;
        } else if (failureCause == FailureCause.NO_INVERTIBLE_TRANSFORMATION) {
            return _NO_TRANSFORMATION_DESCRIPTION;
        } else {
            return null;
        } 
        
    }

    public DescriptorBased2DResultConverter(int channelNum) {
        _results = new Hashtable<>();
        this._channelNum = channelNum;
    }

    public DescriptorBased2DResultConverter set(RegistrationRule order, DescriptorBased2DResult result) {
        _results.put(order, result);
        return this;
    }

    public IChannelRegistrationResult convert() {
        DescriptorBasedChannelRegistrationResult result = this._createResultForChannel();
        this._setRegistraionError(result);
        this._setAffineTransform(result);

        return result;

    }

    private DescriptorBasedChannelRegistrationResult _createResultForChannel() {
        DescriptorBasedChannelRegistrationResult result = new DescriptorBasedChannelRegistrationResult();
        result.setChannel(this._channelNum);
        return result;
    }

    private void _setRegistraionError(DescriptorBasedChannelRegistrationResult ourResult) {
        for (RegistrationRule order : RegistrationRule.values()) {
            double error = _getErrorOfRuleRegistration(order);
            ourResult.setError(order, error);
        }
    }

    private double _getErrorOfRuleRegistration(RegistrationRule order) {
        return _results.get(order).error();
    }

    private void _setAffineTransform(DescriptorBasedChannelRegistrationResult ourResult) {
        for (RegistrationRule order : RegistrationRule.values()) {
            Affine2D transform2d = _convertAlgorithmAffineTransform(_results.get(order));
            ourResult.setAffineTransform(order, transform2d);
        }
    }

    private Affine2D _convertAlgorithmAffineTransform(DescriptorBased2DResult algorithmResult) {
        Affine2D transform2d = null;
        transform2d = new Affine2D();
        transform2d.set(algorithmResult.affineTransform());
        return transform2d;
    }

}