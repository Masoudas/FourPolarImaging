package fr.fresnel.fourPolar.core.image.orientation;

import fr.fresnel.fourPolar.core.fourPolar.IOrientationVectorIterator;
import fr.fresnel.fourPolar.core.image.captured.fileSet.ICapturedImageFileSet;
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
        Float32 pixelType = new Float32();
        long[] dimension = polImage.getPolarizationImage(Polarization.pol0).getImage().getDimensions();

        _rhoImage = new AngleImage(OrientationAngle.rho, factory.create(dimension, pixelType));
        _deltaImage = new AngleImage(OrientationAngle.delta, factory.create(dimension, pixelType));
        _etaImage = new AngleImage(OrientationAngle.eta, factory.create(dimension, pixelType));

        _fileSet = fileSet;
    }

    public OrientationImage(ICapturedImageFileSet fileSet, IAngleImage rho, IAngleImage delta, IAngleImage eta) {
        _rhoImage = rho;
        _deltaImage = delta;
        _etaImage = eta;

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
        return new OrientationVectorIterator(this);
    }

    
}