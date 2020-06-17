package fr.fresnel.fourPolar.core.image.orientation;

import java.util.Arrays;
import java.util.Objects;

import fr.fresnel.fourPolar.core.exceptions.image.orientation.CannotFormOrientationImage;
import fr.fresnel.fourPolar.core.fourPolar.IOrientationVectorIterator;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.physics.channel.ChannelUtils;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;

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
    public OrientationImage(ICapturedImageFileSet fileSet, int channel, AngleImage rho, AngleImage delta,
            AngleImage eta) throws CannotFormOrientationImage {
        Objects.requireNonNull(fileSet);
        Objects.requireNonNull(rho);
        Objects.requireNonNull(delta);
        Objects.requireNonNull(eta);

        ChannelUtils.checkChannelNumberIsNonZero(channel);
        this._checkHasNoDuplicateImage(rho, delta, eta);
        this._checkImagesHaveEqualDim(rho, delta, eta);

        _rhoImage = rho;
        _deltaImage = delta;
        _etaImage = eta;
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

    @Override
    public IOrientationImageRandomAccess getRandomAccess() {
        return new OrientationImageRandomAccess(_rhoImage.getImage().getRandomAccess(),
                _deltaImage.getImage().getRandomAccess(), _etaImage.getImage().getRandomAccess());
    }

    @Override
    public int channel() {
        return this._channel;
    }

    private void _checkImagesHaveEqualDim(AngleImage rho, AngleImage delta, AngleImage eta)
            throws CannotFormOrientationImage {
        if (!_angleImagesHaveEqualDim(rho, delta, eta)) {
            throw new CannotFormOrientationImage(
                    "Cannot form the orientation image because the given images don't have the same dimension.");
        }
    }

    private boolean _angleImagesHaveEqualDim(AngleImage rho, AngleImage delta, AngleImage eta) {
        return Arrays.equals(rho.getImage().getMetadata().getDim(), delta.getImage().getMetadata().getDim())
                && Arrays.equals(rho.getImage().getMetadata().getDim(), eta.getImage().getMetadata().getDim());
    }

    private void _checkHasNoDuplicateImage(AngleImage rho, AngleImage delta, AngleImage eta)
            throws CannotFormOrientationImage {
        if (rho == delta || rho == eta || delta == eta || rho.getImage() == delta.getImage()
                || rho.getImage() == eta.getImage() || delta.getImage() == eta.getImage()) {
            throw new CannotFormOrientationImage(
                    "Cannot form the orientation image because the given images don't have the same dimension.");
        }
    }
}