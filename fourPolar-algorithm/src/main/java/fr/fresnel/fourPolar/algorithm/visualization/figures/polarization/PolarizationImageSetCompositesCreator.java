package fr.fresnel.fourPolar.algorithm.visualization.figures.polarization;

import java.util.HashMap;

import fr.fresnel.fourPolar.algorithm.util.image.color.GrayScaleToColorConverter;
import fr.fresnel.fourPolar.algorithm.util.image.color.GrayScaleToColorConverter.Color;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.IPixelRandomAccess;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.IPixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelTypes;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.preprocess.registration.RegistrationRule;
import fr.fresnel.fourPolar.core.visualization.figures.polarization.IPolarizationImageSetComposites;
import fr.fresnel.fourPolar.core.visualization.figures.polarization.PolarizationImageSetCompositesBuilder;

/**
 * Using this class, we can create a composite figure
 * {@see IRegistrationCompositeFigures} for a given
 * {@link IPolarizationImageSet} that is not realigned. To avoid any unambiguity
 * that the set should not have been realigned, we demand the realigner.
 */
public class PolarizationImageSetCompositesCreator implements IPolarizationImageSetCompositesCreater {
    private final Color _baseImageColor;
    private final Color _registeredImageColor;
    private final PolarizationImageSetCompositesBuilder _builder;

    /**
     * Define compisition image creator by assigning colors to base and registered
     * images.
     * 
     * @param numChannels          is the total number of channels.
     * @param baseImageColor       is the color to be used for the base image of
     *                             every rule.
     * @param registeredImageColor is the color to be used for the registered image
     *                             of every rule.
     */
    public PolarizationImageSetCompositesCreator(int numChannels, Color baseImageColor, Color registeredImageColor) {
        this._baseImageColor = baseImageColor;
        this._registeredImageColor = registeredImageColor;
        this._builder = new PolarizationImageSetCompositesBuilder(numChannels);
    }

    private Image<RGB16> createRuleCompositeImage(Image<UINT16> imageBase, Image<UINT16> registeredImage) {
        IPixelRandomAccess<RGB16> ra1 = GrayScaleToColorConverter.createMonochromeView(imageBase, this._baseImageColor);
        IPixelRandomAccess<RGB16> ra2 = GrayScaleToColorConverter.createMonochromeView(registeredImage,
                this._registeredImageColor);
        Image<RGB16> compositeImage = this._createImageForCompositeFromBaseImage(imageBase);
        this._mergeMonochromeViews(ra1, ra2, compositeImage.getCursor());
        return compositeImage;
    }

    private Image<RGB16> _createImageForCompositeFromBaseImage(Image<UINT16> imageBase) {
        IMetadata metadata = new Metadata.MetadataBuilder(imageBase.getMetadata()).bitPerPixel(PixelTypes.RGB_16)
                .build();

        return imageBase.getFactory().create(metadata, RGB16.zero());
    }

    private void _mergeMonochromeViews(IPixelRandomAccess<RGB16> ra1, IPixelRandomAccess<RGB16> ra2,
            IPixelCursor<RGB16> compositeCursor) {
        for (; compositeCursor.hasNext();) {
            IPixel<RGB16> pixel = compositeCursor.next();
            final long[] position = compositeCursor.localize();

            ra1.setPosition(position);
            ra2.setPosition(position);

            RGB16 pixelColor = ra1.getPixel().value();
            pixelColor.add(ra2.getPixel().value());

            compositeCursor.setPixel(pixel);
        }
    }

    private Image<UINT16> _getPolarizationImage(IPolarizationImageSet polImageSet, Polarization polarization) {
        return polImageSet.getPolarizationImage(polarization).getImage();
    }

    @Override
    public IPolarizationImageSetComposites create(IPolarizationImageSet polImageSet) {
        HashMap<RegistrationRule, Image<RGB16>> compositeImages = this._createCompositeImages(polImageSet);
        int channel = polImageSet.channel();
        ICapturedImageFileSet fileSet = polImageSet.getFileSet();

        return this._buildPolarizationImageSetComposites(compositeImages, channel, fileSet);
    }

    private HashMap<RegistrationRule, Image<RGB16>> _createCompositeImages(IPolarizationImageSet polImageSet) {
        HashMap<RegistrationRule, Image<RGB16>> compositeImages = new HashMap<>();

        for (RegistrationRule rule : RegistrationRule.values()) {
            Image<UINT16> baseImage = this._getPolarizationImage(polImageSet, rule.getBaseImagePolarization());
            Image<UINT16> registeredImage = this._getPolarizationImage(polImageSet,
                    rule.getToRegisterImagePolarization());

            Image<RGB16> compositeImage = this.createRuleCompositeImage(baseImage, registeredImage);
            compositeImages.put(rule, compositeImage);
        }
        return compositeImages;
    }

    private IPolarizationImageSetComposites _buildPolarizationImageSetComposites(
            HashMap<RegistrationRule, Image<RGB16>> compositeImages, int channel, ICapturedImageFileSet fileSet) {
        this._builder.fileSet(fileSet).channel(channel);

        for (RegistrationRule rule : RegistrationRule.values()) {
            this._builder.compositeImage(rule, compositeImages.get(rule));
        }

        return this._builder.build();
    }

}