package fr.fresnel.fourPolar.algorithm.preprocess.registration.descriptorBased;

import java.util.Hashtable;

import fr.fresnel.fourPolar.core.preprocess.registration.IChannelRegistrationResult;
import fr.fresnel.fourPolar.core.preprocess.registration.RegistrationOrder;
import fr.fresnel.fourPolar.core.util.transform.AffineTransform2D;
import registration.descriptorBased.result.DescriptorBased2DResult;
import registration.descriptorBased.result.DescriptorBased2DResult.FailureCause;

/**
 * Wraps a {@link DescriptorBased2DResult} of the three polarizations into an
 * {@link DescriptorBasedChannelRegistrationResult}.
 */
class DescriptorBased2DResultConverter {
    Hashtable<RegistrationOrder, DescriptorBased2DResult> _results;

    public DescriptorBased2DResultConverter() {
        _results = new Hashtable<>();
    }

    public DescriptorBased2DResultConverter set(RegistrationOrder order, DescriptorBased2DResult result) {
        _results.put(order, result);
        return this;
    }

    public IChannelRegistrationResult convert() {
        DescriptorBasedChannelRegistrationResult result = new DescriptorBasedChannelRegistrationResult();

        for (RegistrationOrder order : RegistrationOrder.values()) {
            DescriptorBased2DResult polResult = _results.get(order);

            result.setIsSuccessfulRegistration(order, polResult.isSuccessful());
            result.setDescription(order, this._getDescription(polResult));

            if (polResult.isSuccessful()) {
                result.setError(order, polResult.error());
                result.setAffineTransform(order, this._getAffineTransform(polResult));
            } else {
                result.setError(order, -1);
                result.setAffineTransform(order, null);
            }
        }

        return result;
    }

    public AffineTransform2D _getAffineTransform(DescriptorBased2DResult result) {
        AffineTransform2D transform2d = new AffineTransform2D();
        transform2d.set(result.affineTransform());
        return transform2d;
    }

    public String _getDescription(DescriptorBased2DResult result) {
        if (result.description() == null) {
            return "";
        } else if (result.description() == FailureCause.NOT_ENOUGH_FP) {
            return DescriptorBasedChannelRegistrationResult._NOT_ENOUGH_FP_DESCRIPTION;
        } else if ((result.description() == FailureCause.NO_INLIER_AFTER_RANSAC)) {
            return DescriptorBasedChannelRegistrationResult._NO_TRANSFORMATION_DESCRIPTION;
        } else {
            return "";
        }
    }
}