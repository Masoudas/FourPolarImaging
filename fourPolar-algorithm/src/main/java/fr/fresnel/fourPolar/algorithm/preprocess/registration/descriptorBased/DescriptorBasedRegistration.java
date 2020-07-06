package fr.fresnel.fourPolar.algorithm.preprocess.registration.descriptorBased;

import java.util.Hashtable;

import fr.fresnel.fourPolar.algorithm.exceptions.preprocess.registration.ChannelRegistrationFailure;
import fr.fresnel.fourPolar.algorithm.preprocess.registration.IChannelRegistrator;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.ImageJ1Model.ImageToImageJ1Conveter;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.preprocess.registration.IChannelRegistrationResult;
import fr.fresnel.fourPolar.core.preprocess.registration.RegistrationRule;
import fr.fresnel.fourPolar.core.util.image.metadata.MetadataUtil;
import ij.ImagePlus;
import registration.descriptorBased.headless.HeadLess_Descriptor_based_registration;
import registration.descriptorBased.headless.RegistrationParams;
import registration.descriptorBased.result.DescriptorBased2DResult;
import registration.descriptorBased.result.DescriptorBased2DResult.FailureCause;

/**
 * Register the given polarization image set using the descriptor based
 * registration algorithm. To run the algorithm headless, the original library
 * {@linkplain https://github.com/fiji/Descriptor_based_registration} has been
 * refactord in
 * {@linkplain https://github.com/Masoudas/HeadlessDescriptoBasedRegistration},
 * and is compiled as a jar in this project.
 */
public class DescriptorBasedRegistration implements IChannelRegistrator {
    /**
     * Registration method.
     */
    public static String METHOD_DESCRIPTION = "Descriptor based 2D registration";

    /**
     * Three sets of dog sigma values, starting from rather large (row one) to
     * rather small (row three) bead detection. The larger the detector, the
     * (relatively) lesser the chances of detecting noise as FP
     */
    private static double[][] DOG_SIGMA_CHOICES = { { 2.23, 2.65 }, { 2.04, 2.42 }, { 1.787, 2.21 } };

    /**
     * Three choices for intensity thresholding the image. Note that the smaller the
     * threshold, the greater the chance of detecting feature points, but noise as
     * well.
     */
    private static double[] THRESHOLD_CHOICES = { 0.04, 0.02, 0.01 };

    /**
     * Number of tries for registering the given image.
     */
    private static int _ITERATION_MAX = 3;

    private final HeadLess_Descriptor_based_registration _registrator;

    public DescriptorBasedRegistration() {
        this._registrator = new HeadLess_Descriptor_based_registration();
    }

    @Override
    public IChannelRegistrationResult register(IPolarizationImageSet polarizationImageSet)
            throws ChannelRegistrationFailure {
        this._checkPolarizationImageIsQuasiPlanar(polarizationImageSet);

        Hashtable<RegistrationRule, DescriptorBased2DResult> channelResult = this
                ._registerChannel(polarizationImageSet);

        if (!this._allRulesSuccessfullyRegistered(channelResult)) {
            this._buildAndThrowRegistrationFailureException(channelResult);
        }

        return this._createChannelRegistrationResult(channelResult, polarizationImageSet.channel());
    }

    private IChannelRegistrationResult _createChannelRegistrationResult(
            Hashtable<RegistrationRule, DescriptorBased2DResult> channelResult, int channel) {
        DescriptorBased2DResultConverter resultConverter = new DescriptorBased2DResultConverter(channel);
        for (RegistrationRule rule : RegistrationRule.values()) {
            resultConverter.set(rule, channelResult.get(rule));
        }

        return resultConverter.convert();
    }

    private boolean _allRulesSuccessfullyRegistered(Hashtable<RegistrationRule, DescriptorBased2DResult> channelResult)
            throws ChannelRegistrationFailure {
        boolean registrationSuccessful = true;
        for (RegistrationRule rule : RegistrationRule.values()) {
            registrationSuccessful &= channelResult.get(rule).isSuccessful();
        }

        return registrationSuccessful;

    }

    private void _buildAndThrowRegistrationFailureException(
            Hashtable<RegistrationRule, DescriptorBased2DResult> channelResult) throws ChannelRegistrationFailure {
        ChannelRegistrationFailure.Builder exceptionBuilder = new ChannelRegistrationFailure.Builder();

        for (RegistrationRule rule : RegistrationRule.values()) {
            String failureDescription = DescriptorBased2DResultConverter
                    .convertFailureCauseToString(channelResult.get(rule).description());
            exceptionBuilder.addRuleFailure(rule, failureDescription);
        }

        throw exceptionBuilder.buildException();
    }

    /**
     * Register all rules of this channel.
     */
    private Hashtable<RegistrationRule, DescriptorBased2DResult> _registerChannel(
            IPolarizationImageSet polarizationImageSet) {
        Hashtable<RegistrationRule, DescriptorBased2DResult> ruleRegistrationResult = new Hashtable<>();
        for (RegistrationRule rule : RegistrationRule.values()) {
            ruleRegistrationResult.put(rule, this._registerRule(polarizationImageSet, rule));
        }
        return ruleRegistrationResult;
    }

    private DescriptorBased2DResult _registerRule(IPolarizationImageSet polarizationImageSet, RegistrationRule rule) {
        Image<UINT16> baseImage = this._getPolarizationImage(polarizationImageSet, rule.getBaseImagePolarization());
        Image<UINT16> toRegisterImage = this._getPolarizationImage(polarizationImageSet,
                rule.getToRegisterImagePolarization());

        return _registerToBasePolarization(baseImage, toRegisterImage);
    }

    private Image<UINT16> _getPolarizationImage(IPolarizationImageSet polarizationImageSet, Polarization pol) {
        return polarizationImageSet.getPolarizationImage(pol).getImage();
    }

    private DescriptorBased2DResult _registerToBasePolarization(Image<UINT16> basePolarization,
            Image<UINT16> toRegisterPolarization) {
        ImagePlus imageBase = _wrapToImageJ1(basePolarization);
        ImagePlus imageToRegister = _wrapToImageJ1(toRegisterPolarization);

        RegistrationParams registParams = _createRegistrationInitialCondition();
        DescriptorBased2DResult result = null;
        for (int itr = 1; itr <= _ITERATION_MAX & !_isRegistrationSatisfactory(result); itr++) {
            registParams = this._upadteRegistrationParameters(result, registParams, itr);
            result = this._registrator.register(imageToRegister, imageBase, registParams);
        }

        return result;
    }

    /**
     * Wraps the given image interface to ImageJ1 interface.
     * 
     * @return
     */
    private ImagePlus _wrapToImageJ1(Image<UINT16> image) {
        return ImageToImageJ1Conveter.convertToImgPlus(image, UINT16.zero());
    }

    /**
     * Start registration from a default set of parameters, which will be updated in
     * each iteration.
     */
    private RegistrationParams _createRegistrationInitialCondition() {
        return new RegistrationParams();
    }

    /**
     * Return false if result is null. Otherwise, if registration is successful and
     * error is less than one pixel, returns true. Otherwise returns false. Note
     * that this method only determines the termination condition of the
     * registration. It is possible that registration is qualitatively successful,
     * even though this condition is never satisfied.
     * 
     * @param result
     */
    private boolean _isRegistrationSatisfactory(DescriptorBased2DResult result) {
        if (result == null) {
            return false;
        }

        if (result.isSuccessful() && result.error() < 1) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * Update the registration parameters based on the params of the previous
     * iteration. For first iteration, return the default parameter set. For next
     * iterations, update either detection threshold related params or RANSAC
     * related params.
     * 
     * @param previous_itr
     * @return
     */
    private RegistrationParams _upadteRegistrationParameters(DescriptorBased2DResult result,
            RegistrationParams previous_itr, int itr) {
        if (itr == 1) {
            return previous_itr;
        }

        RegistrationParams updated = new RegistrationParams(previous_itr);
        if (result.description() == FailureCause.NO_INLIER_AFTER_RANSAC) {
            updated.numNeighbors(previous_itr.getNumNeighbors() + 1).redundancy(previous_itr.getRedundancy() + 1);
        } else if (result.description() == FailureCause.NOT_ENOUGH_FP) {
            updated.sigma1(DOG_SIGMA_CHOICES[itr - 1][0]).sigma2(DOG_SIGMA_CHOICES[itr - 1][1])
                    .detectionThreshold(THRESHOLD_CHOICES[itr - 1]);
        }

        return updated;
    }

    private void _checkPolarizationImageIsQuasiPlanar(IPolarizationImageSet polarizationImageSet) {
        IMetadata metadata = polarizationImageSet.getPolarizationImage(Polarization.pol0).getImage().getMetadata();
        if (!MetadataUtil.isImageQuasiPlanar(metadata)) {
            throw new IllegalArgumentException("Polarization (bead) image must be planar to be registered.");
        }
    }

}