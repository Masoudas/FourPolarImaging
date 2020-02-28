package fr.fresnel.fourPolar.core.image.orientation;

import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;

/**
 * A concrete implementation of the {@link IAngleImage}.
 */
public class AngleImage implements IAngleImage {
    private final Image<Float32> _image;
    private final OrientationAngle _angle;

    public AngleImage(OrientationAngle angle, Image<Float32> image) {
        this._image = image;
        this._angle = angle;
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