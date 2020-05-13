package fr.fresnel.fourPolar.core.image.orientation;

import java.util.Arrays;

import fr.fresnel.fourPolar.core.exceptions.image.orientation.CannotFormOrientationImage;
import fr.fresnel.fourPolar.core.fourPolar.IOrientationVectorIterator;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

/**
 * A concrete implementation of the {@link IOrientationImage}, which contains
 * angle images as an implementation of the {@link IAngleImage}.
 */
class OrientationImage implements IOrientationImage {
    private final ICapturedImageFileSet _fileSet;
    private final IAngleImage _rhoImage;
    private final IAngleImage _deltaImage;
    private final IAngleImage _etaImage;
    private final int _channel;

    /**
     * This constructor creates the orientation image based on three Float32 images.
     * If the images don't have the same dimension, an exception is raised.
     * 
     * @param fileSet is the file set associated with the images.
     * @param rho     is the rho angle image.
     * @param delta   is the delta angle image.
     * @param eta     is the eta angle image.
     * @throws CannotFormOrientationImage if the images don't have the same
     *                                    dimension, or aren't XYCZT.
     */
    public OrientationImage(ICapturedImageFileSet fileSet, int channel, Image<Float32> rho, Image<Float32> delta,
            Image<Float32> eta) throws CannotFormOrientationImage {
        if (this._hasDuplicateImage(rho, delta, eta)) {
            throw new CannotFormOrientationImage(
                    "Cannot form the orientation image due to duplicate image for angles.");
        }

        if (!this._hasDimensionsEqual(rho, delta, eta)) {
            throw new CannotFormOrientationImage(
                    "Cannot form the orientation image because the given images don't have the same dimension.");
        }

        if (rho.getMetadata().axisOrder() != AxisOrder.XYCZT || delta.getMetadata().axisOrder() != AxisOrder.XYCZT
                || eta.getMetadata().axisOrder() != AxisOrder.XYCZT) {
            throw new CannotFormOrientationImage("Angle images are not XYCZT");
        }

        if (channel < 1) {
            throw new CannotFormOrientationImage("Channel number must be positive");
        }

        _rhoImage = new AngleImage(OrientationAngle.rho, rho);
        _deltaImage = new AngleImage(OrientationAngle.delta, delta);
        _etaImage = new AngleImage(OrientationAngle.eta, eta);
        _fileSet = fileSet;
        _channel = channel;
    }

    @Override
    public IAngleImage getAngleImage(OrientationAngle angle) {
        IAngleImage angleImage = null;

        switch (angle) {
            case rho:
                angleImage = _rhoImage;
                break;

            case delta:
                angleImage = _deltaImage;
                break;

            case eta:
                angleImage = _etaImage;
                break;

            default:
                break;
        }
        return angleImage;
    }

    @Override
    public ICapturedImageFileSet getCapturedSet() {
        return this._fileSet;
    }

    @Override
    public IOrientationVectorIterator getOrientationVectorIterator() {
        return new OrientationVectorIterator(_rhoImage.getImage().getCursor(), _deltaImage.getImage().getCursor(),
                _etaImage.getImage().getCursor());
    }

    private boolean _hasDimensionsEqual(Image<Float32> rho, Image<Float32> delta, Image<Float32> eta) {
        return Arrays.equals(rho.getMetadata().getDim(), delta.getMetadata().getDim())
                && Arrays.equals(rho.getMetadata().getDim(), eta.getMetadata().getDim());
    }

    private boolean _hasDuplicateImage(Image<Float32> rho, Image<Float32> delta, Image<Float32> eta) {
        return rho == delta || rho == eta || delta == eta;
    }

    @Override
    public IOrientationImageRandomAccess getRandomAccess() {
        return new OrientationImageRandomAccess(_rhoImage.getImage().getRandomAccess(),
                _deltaImage.getImage().getRandomAccess(), _etaImage.getImage().getRandomAccess());
    }

    @Override
    public int channel() {
        return this._channel;
    }

}