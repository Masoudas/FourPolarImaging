package fr.fresnel.fourPolar.core.visualization.figures.polarization;

import java.util.HashMap;
import java.util.Objects;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.physics.channel.ChannelUtils;
import fr.fresnel.fourPolar.core.preprocess.registration.RegistrationRule;

/**
 * A concrete implementation of {@link IPolarizationImageSetComposites}.
 */
public class PolarizationImageSetComposites implements IPolarizationImageSetComposites {
    private final int _channel;
    private final HashMap<RegistrationRule, IPolarizationImageComposite> _compositeImages;
    private final ICapturedImageFileSet _fileSet;

    public PolarizationImageSetComposites(IPolarizationImageSetCompositesBuilder builder) {
        this._channel = builder.getChannel();
        this._compositeImages = this._setPolarizationComposites(builder);
        this._fileSet = builder.getFileSet();
    }

    private HashMap<RegistrationRule, IPolarizationImageComposite> _setPolarizationComposites(
            IPolarizationImageSetCompositesBuilder builder) {
        HashMap<RegistrationRule, IPolarizationImageComposite> compositeImages = new HashMap<>();
        for (RegistrationRule rule : RegistrationRule.values()) {
            this._compositeImages.put(rule, builder.getCompositeImage(rule));
        }

        return compositeImages;
    }

    @Override
    public int channel() {
        return this._channel;
    }


    @Override
    public IPolarizationImageComposite getCompositeImage(RegistrationRule rule) {
        return this._compositeImages.get(rule);
    }

    @Override
    public ICapturedImageFileSet getFileSet() {
        return this._fileSet;
    }

}