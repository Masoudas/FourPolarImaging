package fr.fresnel.fourPolar.core.image.orientation;

import java.util.Arrays;

import fr.fresnel.fourPolar.core.exceptions.image.orientation.CannotFormOrientationImage;
import fr.fresnel.fourPolar.core.fourPolar.IOrientationVectorIterator;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

/**
 * A concrete implementation of the {@link IOrientationImage}, which contains
 * angle images as an implementation of the {@link IAngleImage}.
 */
public class OrientationImage implements IOrientationImage {
    private final ICapturedImageFileSet _fileSet;
    private final IAngleImage _rhoImage;
    private final IAngleImage _deltaImage;
    private final IAngleImage _etaImage;

    /**
     * Using this constructor, we can create an empty orientation image, where each
     * {@link IAngleImage} has the same dimension as the {@link IPolarizationImage}.
     * 
     * @param fileSet
     * @param factory
     * @param polImage
     */
    public OrientationImage(ICapturedImageFileSet fileSet, ImageFactory factory, IPolarizationImageSet polImage) {
        Float32 pixelType = Float32.zero();
        long[] dimension = polImage.getPolarizationImage(Polarization.pol0).getImage().getMetadata().getDim();

        _rhoImage = new AngleImage(OrientationAngle.rho, factory.create(dimension, pixelType));
        _deltaImage = new AngleImage(OrientationAngle.delta, factory.create(dimension, pixelType));
        _etaImage = new AngleImage(OrientationAngle.eta, factory.create(dimension, pixelType));

        _fileSet = fileSet;
    }

    /**
     * This constructor creates the orientation image based on three Float32 images.
     * If the images don't have the same dimension, an exception is raised.
     * 
     * @param fileSet is the file set associated with the images.
     * @param rho     is the rho angle image.
     * @param delta   is the delta angle image.
     * @param eta     is the eta angle image.
     * @throws CannotFormOrientationImage if the images don't have the same
     *                                    dimension.
     */
    public OrientationImage(ICapturedImageFileSet fileSet, Image<Float32> rho, Image<Float32> delta, Image<Float32> eta)
            throws CannotFormOrientationImage {
        if (this._hasDuplicateImage(rho, delta, eta)) {
            throw new CannotFormOrientationImage(
                    "Cannot form the orientation image due to duplicate image for angles.");
        }

        if (!this._hasDimensionsEqual(rho, delta, eta)) {
            throw new CannotFormOrientationImage(
                    "Cannot form the orientation image because the given images don't have the same dimension.");
        }

        _rhoImage = new AngleImage(OrientationAngle.rho, rho);
        _deltaImage = new AngleImage(OrientationAngle.delta, delta);
        _etaImage = new AngleImage(OrientationAngle.eta, eta);
        _fileSet = fileSet;
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

}