package fr.fresnel.fourPolar.core.visualization.figures.polarization;

import java.util.HashMap;
import java.util.Objects;

import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.physics.channel.ChannelUtils;
import fr.fresnel.fourPolar.core.preprocess.registration.RegistrationRule;

public class PolarizationImageSetComposites implements IPolarizationImageSetComposites {
    private final int _channel;
    private final HashMap<RegistrationRule, Image<RGB16>> _compositeImages;

    public PolarizationImageSetComposites(int channel) {
        ChannelUtils.checkChannelNumberIsNonZero(channel);

        this._channel = channel;
        this._compositeImages = new HashMap<>();
    }

    @Override
    public int channel() {
        return this._channel;
    }

    @Override
    public void setCompositeImage(RegistrationRule rule, Image<RGB16> compositeImage) {
        Objects.requireNonNull(rule);
        Objects.requireNonNull(compositeImage);

        this._compositeImages.put(rule, compositeImage);
    }

    @Override
    public Image<RGB16> getCompositeImage(RegistrationRule rule) {
        return this._compositeImages.get(rule);
    }

}