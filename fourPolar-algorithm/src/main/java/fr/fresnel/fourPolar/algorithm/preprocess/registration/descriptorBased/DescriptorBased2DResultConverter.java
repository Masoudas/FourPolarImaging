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
    private Hashtable<RegistrationRule, DescriptorBased2DResult> _results;
    private final int _channelNum;

    public DescriptorBased2DResultConverter(int channelNum) {
        _results = new Hashtable<>();
        this._channelNum = channelNum;
    }

    public DescriptorBased2DResultConverter set(RegistrationRule order, DescriptorBased2DResult result) {
        _results.put(order, result);
        return this;
    }

    public IChannelRegistrationResult convert() {
        DescriptorBasedChannelRegistrationResult result = new DescriptorBasedChannelRegistrationResult();
        result.setChannel(this._channelNum);
        for (RegistrationRule order : RegistrationRule.values()) {
            DescriptorBased2DResult polResult = _results.get(order);

            result.setIsSuccessfulRegistration(order, polResult.isSuccessful());
            if (polResult.isSuccessful()) {
                result.setError(order, polResult.error());
                result.setAffineTransform(order, this._getAffineTransform(polResult));
            } else {
                result.setDescription(order, this._getFailureDescription(polResult));
                result.setError(order, -1);
                result.setAffineTransform(order, null);
            }
        }

        return result;
    }

    private Affine2D _getAffineTransform(DescriptorBased2DResult result) {
        Affine2D transform2d = new Affine2D();
        transform2d.set(result.affineTransform());
        return transform2d;
    }

    private String _getFailureDescription(DescriptorBased2DResult result) {
        if (result.description() == null) {
            return "";
        } else if (result.description() == FailureCause.NOT_ENOUGH_FP) {
            return DescriptorBasedChannelRegistrationResult._NOT_ENOUGH_FP_DESCRIPTION;
        } else if (result.description() == FailureCause.NO_INLIER_AFTER_RANSAC) {
            return DescriptorBasedChannelRegistrationResult._NO_TRANSFORMATION_DESCRIPTION;
        } else if (result.description() == FailureCause.NO_INVERTIBLE_TRANSFORMATION) {
            return DescriptorBasedChannelRegistrationResult._NO_TRANSFORMATION_DESCRIPTION;
        } else {
            return "";
        }
    }
}