package fr.fresnel.fourPolar.core.image.polarization;

import java.util.Arrays;
import java.util.Objects;

import fr.fresnel.fourPolar.core.exceptions.image.polarization.CannotFormPolarizationImageSet;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.physics.channel.Channel;

/**
 * Builds an {@link IPolarizationImageSet} for the given channel of sample.
 */
public class PolarizationImageSetBuilder {
    private Image<UINT16> _pol0 = null;
    private Image<UINT16> _pol45 = null;
    private Image<UINT16> _pol90 = null;
    private Image<UINT16> _pol135 = null;
    private ICapturedImageFileSet _fileSet = null;
    private int _channel = -1;

    private final int _numChannels;

    public PolarizationImageSetBuilder(int numChannels) {
        this._numChannels = numChannels;
    }

    public PolarizationImageSetBuilder pol0(Image<UINT16> image) {
        Objects.requireNonNull(image, "image can't be null");

        if (this._pol0 != null) {
            throw new IllegalArgumentException("pol0 image already set");
        }

        this._checkImage(image);

        this._pol0 = image;
        return this;
    }

    public PolarizationImageSetBuilder pol45(Image<UINT16> image) {
        Objects.requireNonNull(image, "image can't be null");

        if (this._pol45 != null) {
            throw new IllegalArgumentException("pol45 image already set");
        }

        this._checkImage(image);

        this._pol45 = image;
        return this;
    }

    public PolarizationImageSetBuilder pol90(Image<UINT16> image) {
        Objects.requireNonNull(image, "image can't be null");

        if (this._pol90 != null) {
            throw new IllegalArgumentException("pol90 image already set");
        }

        this._checkImage(image);

        this._pol90 = image;
        return this;
    }

    public PolarizationImageSetBuilder pol135(Image<UINT16> image) {
        Objects.requireNonNull(image, "image can't be null");

        if (this._pol135 != null) {
            throw new IllegalArgumentException("pol135 image already set");
        }

        this._checkImage(image);

        this._pol135 = image;
        return this;
    }

    public PolarizationImageSetBuilder fileSet(ICapturedImageFileSet fileSet) {
        Objects.requireNonNull(fileSet, "fileSet can't be null");

        if (this._fileSet != null) {
            throw new IllegalArgumentException("fileSet already set");
        }

        this._fileSet = fileSet;
        return this;
    }

    public PolarizationImageSetBuilder channel(int channel) {
        Channel.checkChannel(channel, this._numChannels);

        if (this._channel != -1) {
            throw new IllegalArgumentException("channel already set");
        }

        this._channel = channel;
        return this;
    }

    public IPolarizationImageSet build() throws CannotFormPolarizationImageSet {
        if (this._hasDuplicateImage(this._pol0, this._pol45, this._pol90, this._pol135)) {
            throw new CannotFormPolarizationImageSet(
                    "Cannot form the polarization image set due to duplicate image for polarizations.");
        }
        if (!this._hasEqualDimensions(this._pol0, this._pol45, this._pol90, this._pol135)) {
            throw new CannotFormPolarizationImageSet(
                    "Cannot form the polarization image set because the given images don't have the same dimension.");
        }

        try {
            this._checkAllParamsSet();
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new CannotFormPolarizationImageSet(e.getMessage());
        }
        
        IPolarizationImageSet imageSet = new PolarizationImageSet(this);
        this._resetBuilder();

        return imageSet;
    }

    private void _checkImage(Image<UINT16> image) {
        IMetadata metadata = image.getMetadata();

        if (metadata.axisOrder() != AxisOrder.XYCZT) {
            throw new IllegalArgumentException("polarization image must be XYCZT.");
        }

        if (metadata.numChannels() != 1) {
            throw new IllegalArgumentException("polarization image must have only one channel.");
        }

    }

    /**
     * Checks that all images have the same dimension.
     */
    private boolean _hasEqualDimensions(Image<UINT16> pol0, Image<UINT16> pol45, Image<UINT16> pol90,
            Image<UINT16> pol135) {
        return Arrays.equals(pol0.getMetadata().getDim(), pol45.getMetadata().getDim())
                && Arrays.equals(pol0.getMetadata().getDim(), pol90.getMetadata().getDim())
                && Arrays.equals(pol0.getMetadata().getDim(), pol135.getMetadata().getDim());
    }

    /**
     * checks for duplicate image reference.
     */
    private boolean _hasDuplicateImage(Image<UINT16> pol0, Image<UINT16> pol45, Image<UINT16> pol90,
            Image<UINT16> pol135) {
        return pol0 == pol45 || pol0 == pol90 || pol0 == pol135 || pol45 == pol90 || pol45 == pol135 || pol90 == pol135;
    }

    private void _checkAllParamsSet() {
        Objects.requireNonNull(this._pol0, "pol0 image is not set.");
        Objects.requireNonNull(this._pol45, "pol45 image is not set.");
        Objects.requireNonNull(this._pol90, "pol90 image is not set.");
        Objects.requireNonNull(this._pol90, "pol135 image is not set.");
        Objects.requireNonNull(this._fileSet, "fileSet is not set.");

        Channel.checkChannel(this._channel, this._numChannels);
    }

    private void _resetBuilder() {
        this._pol0 = null;
        this._pol45 = null;
        this._pol90 = null;
        this._pol135 = null;
        this._channel = -1;
        this._fileSet = null;
    }

    public int getChannel() {
        return _channel;
    }

    /**
     * @return the _fileSet
     */
    public ICapturedImageFileSet getFileSet() {
        return _fileSet;
    }

    /**
     * @return the _pol0
     */
    public Image<UINT16> getPol0() {
        return _pol0;
    }

    /**
     * @return the _pol45
     */
    public Image<UINT16> getPol45() {
        return _pol45;
    }

    /**
     * @return the _pol90
     */
    public Image<UINT16> getPol90() {
        return _pol90;
    }

    /**
     * @return the _pol135
     */
    public Image<UINT16> getPol135() {
        return _pol135;
    }

    /**
     * @return the _numChannels
     */
    public int getNumChannels() {
        return _numChannels;
    }
}