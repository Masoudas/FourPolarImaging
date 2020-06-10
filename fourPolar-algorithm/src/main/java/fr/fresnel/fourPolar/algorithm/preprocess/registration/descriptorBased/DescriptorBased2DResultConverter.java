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
        DescriptorBasedChannelRegistrationResult result = this._createResultForChannel();
        this._setIsSuccesful(result);
        this._setRegistraionError(result);
        this._setAffineTransform(result);
        this._setFailureDescription(result);

        return result;

    }

    private DescriptorBasedChannelRegistrationResult _createResultForChannel() {
        DescriptorBasedChannelRegistrationResult result = new DescriptorBasedChannelRegistrationResult();
        result.setChannel(this._channelNum);
        return result;
    }

    private void _setIsSuccesful(DescriptorBasedChannelRegistrationResult ourResult) {
        for (RegistrationRule order : RegistrationRule.values()) {
            ourResult.setIsSuccessfulRegistration(order, _results.get(order).isSuccessful());
        }

    }

    private void _setRegistraionError(DescriptorBasedChannelRegistrationResult ourResult) {
        for (RegistrationRule order : RegistrationRule.values()) {
            ourResult.setError(order, _results.get(order).error());
        }
    }

    /**
     * If Descriptior based algorithm has been successful, set our matrix to the
     * affine of the algorithm. Otherwise, return a null, which will later be
     * returned as an empty optional in {@link IChannelRegistrationResult}.
     * 
     */
    private void _setAffineTransform(DescriptorBasedChannelRegistrationResult ourResult) {
        for (RegistrationRule order : RegistrationRule.values()) {
            if (_results.get(order).isSuccessful()) {
                Affine2D transform2d = _convertAlgorithmAffineTransform(_results.get(order));
                ourResult.setAffineTransform(order, transform2d);
            }
        }

    }

    private Affine2D _convertAlgorithmAffineTransform(DescriptorBased2DResult algorithmResult) {
        Affine2D transform2d = null;
        transform2d = new Affine2D();
        transform2d.set(algorithmResult.affineTransform());
        return transform2d;
    }

    /**
     * If Descriptior based algorithm has been successful, return null, which will
     * later be returned as an empty optional in {@link IChannelRegistrationResult}.
     * Otherwise, return the description.
     */
    private void _setFailureDescription(DescriptorBasedChannelRegistrationResult ourResult) {
        for (RegistrationRule order : RegistrationRule.values()) {
            if (!_results.get(order).isSuccessful()) {
                String description = this._convertDescription(_results.get(order).description());
                ourResult.setDescription(order, description);
            }
        }
    }

    /**
     * Convert from algorithm {@link FailureCause} to our string description.
     * 
     * @param failureCause
     * @return
     */
    private String _convertDescription(FailureCause failureCause) {
        if (failureCause == FailureCause.NOT_ENOUGH_FP) {
            return DescriptorBasedChannelRegistrationResult._NOT_ENOUGH_FP_DESCRIPTION;
        } else if (failureCause == FailureCause.NO_INLIER_AFTER_RANSAC) {
            return DescriptorBasedChannelRegistrationResult._NO_TRANSFORMATION_DESCRIPTION;
        } else if (failureCause == FailureCause.NO_INVERTIBLE_TRANSFORMATION) {
            return DescriptorBasedChannelRegistrationResult._NO_TRANSFORMATION_DESCRIPTION;
        } else {
            return null;
        }

    }
}