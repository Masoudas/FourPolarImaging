package fr.fresnel.fourPolar.algorithm.visualization.figures.registration;

import fr.fresnel.fourPolar.algorithm.util.image.color.GrayScaleToColorConverter;
import fr.fresnel.fourPolar.algorithm.util.image.color.GrayScaleToColorConverter.Color;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.preprocess.registration.RegistrationRule;
import fr.fresnel.fourPolar.core.visualization.figures.registration.IRegistrationCompositeFigures;

/**
 * Using this class, we can create a composite figure
 * {@see IRegistrationCompositeFigures} for a given
 * {@link IPolarizationImageSet} that is not realigned. To avoid any unambiguity
 * that the set should not have been realigned, we demand the realigner.
 */
public class RegistrationCompositeFigureCreator {
    private final Color _baseImageColor;
    private final Color _registeredImageColor;
    private final IRegistrationCompositeFigures _compositeFigures;

    /**
     * Define compisition image creator by assigning colors to base and registered
     * images.
     * 
     * @param baseImageColor       is the color to be used for the base image of
     *                             every rule.
     * @param registeredImageColor is the color to be used for the registered image
     *                             of every rule.
     */
    public RegistrationCompositeFigureCreator(IRegistrationCompositeFigures compositeFigures, Color baseImageColor,
            Color registeredImageColor) {
        _baseImageColor = baseImageColor;
        _registeredImageColor = registeredImageColor;
        _compositeFigures = compositeFigures;
    }

    /**
     * Fill the registration composite figures with the composite images.
     * 
     * @param baseImage      is the base image for the registration rule.
     * @param realignedImage is the realigned image of the registration rule.
     * @param rule           is the registration rule.
     */
    public void createComposite(Image<UINT16> baseImage, Image<UINT16> realignedImage, RegistrationRule rule) {
        Image<RGB16> compositeImage = GrayScaleToColorConverter.mergeAsMonoColor(baseImage, this._baseImageColor,
                realignedImage, this._registeredImageColor);
        this._compositeFigures.setCompositeImage(rule, compositeImage);
    }
}