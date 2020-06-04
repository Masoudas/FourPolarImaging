package fr.fresnel.fourPolar.core.visualization.figures.polarization;

import java.util.HashMap;
import java.util.Objects;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.physics.channel.ChannelUtils;
import fr.fresnel.fourPolar.core.preprocess.registration.RegistrationRule;

/**
 * Builds an instance of {@link IPolarizationImageSetComposites}.
 */
public class PolarizationImageSetCompositesBuilder extends IPolarizationImageSetCompositesBuilder {
    private final HashMap<RegistrationRule, Image<RGB16>> _compositeImages;
    private final int _numChannels;

    private int _channel;
    private ICapturedImageFileSet _fileSet;

    public PolarizationImageSetCompositesBuilder(int numChannels) {
        this._compositeImages = new HashMap<>();
        this._numChannels = numChannels;
        this._channel = -1;
    }

    /**
     * Set the composite image of the given registration rule.
     */
    public PolarizationImageSetCompositesBuilder compositeImage(RegistrationRule rule, Image<RGB16> image) {
        Objects.requireNonNull(rule);
        Objects.requireNonNull(image);

        this._compositeImages.put(rule, image);

        return this;
    }

    public PolarizationImageSetCompositesBuilder channel(int channel) {
        ChannelUtils.checkChannel(channel, this._numChannels);

        this._channel = channel;
        return this;
    }

    public PolarizationImageSetCompositesBuilder fileSet(ICapturedImageFileSet fileSet) {
        Objects.requireNonNull(fileSet);

        this._fileSet = fileSet;
        return this;
    }

    public IPolarizationImageSetComposites build() {
        this._checkAllBuildParamsAreGiven();

        PolarizationImageSetComposites composites = new PolarizationImageSetComposites(this);

        this._resetBuilder();

        return composites;
    }

    private void _resetBuilder() {
        this._channel = -1;
        this._fileSet = null;
        this._compositeImages.clear();
    }

    private void _checkAllBuildParamsAreGiven() {
        this._checkAllCompositesAreGiven();
        this._checkChannelIsGiven();
    }

    private void _checkAllCompositesAreGiven() {
        for (RegistrationRule rule : RegistrationRule.values()) {
            if (!this._compositeImages.containsKey(rule))
                throw new IllegalArgumentException("Composite image is not provided for rule " + rule);
        }
    }

    private void _checkChannelIsGiven() {
        if (this._channel < 1) {
            throw new IllegalArgumentException("Channel has not been provided for this composite");
        }
    }

    @Override
    public IPolarizationImageComposite getCompositeImage(RegistrationRule rule) {
        return new PolarizationImageComposite(rule, this._compositeImages.get(rule));
    }

    @Override
    public int getChannel() {
        return this._channel;
    }

    @Override
    public ICapturedImageFileSet getFileSet() {
        return this._fileSet;
    }
}