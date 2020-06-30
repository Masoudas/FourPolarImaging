package fr.fresnel.fourPolar.core.image.polarization;

import java.util.Arrays;
import java.util.Objects;

import fr.fresnel.fourPolar.core.exceptions.image.polarization.CannotFormPolarizationImageSet;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.physics.channel.ChannelUtils;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

/**
 * Builds an {@link IPolarizationImageSet} for the given channel of sample.
 */
public class PolarizationImageSetBuilder extends IPolarizationImageSetBuilder {
    private IPolarizationImage _pol0 = null;
    private IPolarizationImage _pol45 = null;
    private IPolarizationImage _pol90 = null;
    private IPolarizationImage _pol135 = null;
    private ICapturedImageFileSet _fileSet = null;
    private int _channel = -1;

    private final int _numChannels;

    public PolarizationImageSetBuilder(int numChannels) {
        this._numChannels = numChannels;
    }

    public PolarizationImageSetBuilder pol0(Image<UINT16> image) {
        Objects.requireNonNull(image, "image can't be null");
        this._pol0 = this._createPolarizationImage(image, Polarization.pol0);

        return this;
    }

    public PolarizationImageSetBuilder pol45(Image<UINT16> image) {
        Objects.requireNonNull(image, "image can't be null");
        this._pol45 = this._createPolarizationImage(image, Polarization.pol45);

        return this;
    }

    public PolarizationImageSetBuilder pol90(Image<UINT16> image) {
        Objects.requireNonNull(image, "image can't be null");
        this._pol90 = this._createPolarizationImage(image, Polarization.pol90);

        return this;
    }

    public PolarizationImageSetBuilder pol135(Image<UINT16> image) {
        Objects.requireNonNull(image, "image can't be null");
        this._pol135 = this._createPolarizationImage(image, Polarization.pol135);

        return this;
    }

    public PolarizationImageSetBuilder fileSet(ICapturedImageFileSet fileSet) {
        Objects.requireNonNull(fileSet, "fileSet can't be null");
        this._fileSet = fileSet;

        return this;
    }

    public PolarizationImageSetBuilder channel(int channel) {
        ChannelUtils.checkChannelExists(channel, this._numChannels);
        this._channel = channel;
        
        return this;
    }

    public IPolarizationImageSet build() throws CannotFormPolarizationImageSet {
        try {
            this._checkAllParamsSet();
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new CannotFormPolarizationImageSet(e.getMessage());
        }

        _checkPolImagesAreNotDupliacte();
        _checkPolImagesHaveSameDimension();

        IPolarizationImageSet imageSet = new PolarizationImageSet(this);
        this._resetBuilder();

        return imageSet;
    }

    private void _checkPolImagesHaveSameDimension() throws CannotFormPolarizationImageSet {
        if (!this._polImagesHaveEqualDim(this._pol0, this._pol45, this._pol90, this._pol135)) {
            throw new CannotFormPolarizationImageSet(
                    "Cannot form the polarization image set because the given images don't have the same dimension.");
        }
    }

    private void _checkPolImagesAreNotDupliacte() throws CannotFormPolarizationImageSet {
        if (this._polImagesHaveDuplicate(this._pol0, this._pol45, this._pol90, this._pol135)) {
            throw new CannotFormPolarizationImageSet(
                    "Cannot form the polarization image set due to duplicate image for polarizations.");
        }
    }

    private IPolarizationImage _createPolarizationImage(Image<UINT16> image, Polarization pol) {
        return new PolarizationImage(pol, image);
    }

    /**
     * Checks that all images have the same dimension.
     */
    private boolean _polImagesHaveEqualDim(IPolarizationImage pol0, IPolarizationImage pol45, IPolarizationImage pol90,
            IPolarizationImage pol135) {
        return Arrays.equals(pol0.getImage().getMetadata().getDim(), pol45.getImage().getMetadata().getDim())
                && Arrays.equals(pol0.getImage().getMetadata().getDim(), pol90.getImage().getMetadata().getDim())
                && Arrays.equals(pol0.getImage().getMetadata().getDim(), pol135.getImage().getMetadata().getDim());
    }

    /**
     * checks for duplicate image reference.
     */
    private boolean _polImagesHaveDuplicate(IPolarizationImage pol0, IPolarizationImage pol45, IPolarizationImage pol90,
            IPolarizationImage pol135) {
        return pol0.getImage() == pol45.getImage() || pol0.getImage() == pol90.getImage()
                || pol0.getImage() == pol135.getImage() || pol45.getImage() == pol90.getImage()
                || pol45.getImage() == pol135.getImage() || pol90 == pol135;
    }

    private void _checkAllParamsSet() {
        Objects.requireNonNull(this._pol0, "pol0 image is not set.");
        Objects.requireNonNull(this._pol45, "pol45 image is not set.");
        Objects.requireNonNull(this._pol90, "pol90 image is not set.");
        Objects.requireNonNull(this._pol90, "pol135 image is not set.");
        Objects.requireNonNull(this._fileSet, "fileSet is not set.");

        ChannelUtils.checkChannelExists(this._channel, this._numChannels);
    }

    private void _resetBuilder() {
        this._pol0 = null;
        this._pol45 = null;
        this._pol90 = null;
        this._pol135 = null;
        this._channel = -1;
        this._fileSet = null;
    }

    int getChannel() {
        return _channel;
    }

    /**
     * @return the _fileSet
     */
    ICapturedImageFileSet getFileSet() {
        return _fileSet;
    }

    /**
     * @return the _pol0
     */
    IPolarizationImage getPol0() {
        return _pol0;
    }

    /**
     * @return the _pol45
     */
    IPolarizationImage getPol45() {
        return _pol45;
    }

    /**
     * @return the _pol90
     */
    IPolarizationImage getPol90() {
        return _pol90;
    }

    /**
     * @return the _pol135
     */
    IPolarizationImage getPol135() {
        return _pol135;
    }

    /**
     * @return the _numChannels
     */
    int getNumChannels() {
        return _numChannels;
    }
}