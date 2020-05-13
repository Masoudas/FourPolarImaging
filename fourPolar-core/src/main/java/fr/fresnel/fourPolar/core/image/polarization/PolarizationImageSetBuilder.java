package fr.fresnel.fourPolar.core.image.polarization;

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

    private void _resetBuilder() {
        this._pol0 = null;
        this._pol45 = null;
        this._pol90 = null;
        this._pol135 = null;
        this._channel = -1;

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