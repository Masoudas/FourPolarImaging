package fr.fresnel.fourPolar.algorithm.preprocess.realignment;

import fr.fresnel.fourPolar.algorithm.util.image.transform.ImageAffineTransformer;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.preprocess.registration.IChannelRegistrationResult;
import fr.fresnel.fourPolar.core.preprocess.registration.RegistrationRule;
import fr.fresnel.fourPolar.core.util.transform.Affine2D;
import javassist.tools.reflect.CannotCreateException;

/**
 * A concrete implementation of {@link IChannelRealigner.}
 */
public class ChannelRealigner implements IChannelRealigner {
    private final IChannelRegistrationResult _channelRegistrationResult;

    /**
     * Create an instance for the desired channel, based on its registration result.
     * 
     * @return
     * @throws CannotCreateException
     */
    public static IChannelRealigner create(final IChannelRegistrationResult channelRegistrationResult) {
        return new ChannelRealigner(channelRegistrationResult);
    }

    /**
     * Realign polarization images using ImgLib2 library.
     */
    private ChannelRealigner(final IChannelRegistrationResult channelRegistrationResult) {
        this._channelRegistrationResult = channelRegistrationResult;
    }

    @Override
    public void realign(final IPolarizationImageSet imageSet) {
        for (RegistrationRule rule : RegistrationRule.values()) {
            this._realingImage(_getPolImage(imageSet, rule), _getAffineTransform(rule));
        }
    }

    private void _realingImage(Image<UINT16> image, Affine2D affine2D) {
        ImageAffineTransformer.apply2DNearestNeighborInterplation(image, affine2D);
    }

    private Image<UINT16> _getPolImage(IPolarizationImageSet imageSet, RegistrationRule rule) {
        Polarization toRegisterPol = rule.getToRegisterImagePolarization();
        return imageSet.getPolarizationImage(toRegisterPol).getImage();
    }

    private Affine2D _getAffineTransform(RegistrationRule rule) {
        return this._channelRegistrationResult.getAffineTransform(rule);
    }
}