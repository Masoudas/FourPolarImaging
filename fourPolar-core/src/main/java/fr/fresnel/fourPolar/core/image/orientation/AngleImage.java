package fr.fresnel.fourPolar.core.image.orientation;

import java.util.Objects;

import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;

/**
 * A concrete implementation of the {@link IAngleImage}.
 */
class AngleImage implements IAngleImage {
    private final Image<Float32> _image;
    private final OrientationAngle _angle;

    /**
     * Create angle image from an image interface.
     * 
     * @param angle is the orientation image
     * @param image must have same axis order as {@link IAngleImage#AXIS_ORDER}.
     * 
     * @throws IllegalArgumentException if axis order does not follow, or that image
     *                                  has more than one channel.
     */
    public AngleImage(OrientationAngle angle, Image<Float32> image) {
        Objects.requireNonNull(angle, "angle can't be null");
        Objects.requireNonNull(image, "image can't be null");
        this._checkAxisOrder(image);
        this._checkImageHasOnlyOneChannel(image);

        this._image = image;
        this._angle = angle;
    }

    private void _checkAxisOrder(Image<Float32> image) {
        if (image.getMetadata().axisOrder() != AXIS_ORDER) {
            throw new IllegalArgumentException("Axis order of provided image should be the same as angle image.");
        }

    }

    private void _checkImageHasOnlyOneChannel(Image<Float32> image) {
        if (image.getMetadata().numChannels() != 1) {
            throw new IllegalArgumentException("Angle image should have only one channel.");
        }
    }

    @Override
    public Image<Float32> getImage() {
        return this._image;
    }

    @Override
    public OrientationAngle getOrientationAngle() {
        return this._angle;
    }

}