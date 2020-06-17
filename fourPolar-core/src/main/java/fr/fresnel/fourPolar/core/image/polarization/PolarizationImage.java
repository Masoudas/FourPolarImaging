package fr.fresnel.fourPolar.core.image.polarization;

import java.util.Objects;

import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

/**
 * A concrete implementation of the {@link IPolarizationImage}
 */
class PolarizationImage implements IPolarizationImage {
    final private Polarization _pol;
    final private Image<UINT16> _image;

    /**
     * A concrete implementation of the {@link IPolarizationImage}
     * 
     * @param pol   is the polarization of this image.
     * @param image is the image interface of this polarization image.
     * 
     * @throws IllegalArgumentException if image does not the same
     *                                  {@link IPolarizationImage#AXIS_ORDER} or has
     *                                  more than one channel.
     */
    public PolarizationImage(Polarization pol, Image<UINT16> image) {
        Objects.requireNonNull(pol, "polarization can't be null.");
        Objects.requireNonNull(image, "image can't be null.");

        this._checkAxisOrder(image);
        this._checkImageHasOnlyOneChannel(image);

        this._image = image;
        this._pol = pol;
    }

    private void _checkAxisOrder(Image<UINT16> image) {
        if (image.getMetadata().axisOrder() != AXIS_ORDER) {
            throw new IllegalArgumentException(
                    "Axis order of provided image should be the same as polarization image.");
        }

    }

    private void _checkImageHasOnlyOneChannel(Image<UINT16> image) {
        if (image.getMetadata().numChannels() != 1) {
            throw new IllegalArgumentException("polarization image should have only one channel.");
        }
    }

    @Override
    public Polarization getPolarization() {
        return this._pol;
    }

    @Override
    public Image<UINT16> getImage() {
        return this._image;
    }

}