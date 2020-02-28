package fr.fresnel.fourPolar.core.image.orientation;

import fr.fresnel.fourPolar.core.fourPolar.IOrientationVectorIterator;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.pixel.Pixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.physics.dipole.IOrientationVector;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationVector;

/**
 * A concrete implementation of the {@link IOrientationVectorIterator} to
 * iterate over the {@code OrientationImage}.
 */
class OrientationVectorIterator implements IOrientationVectorIterator {
    final private IPixelCursor<Float32> _rhoCursor;
    final private IPixelCursor<Float32> _deltaCursor;
    final private IPixelCursor<Float32> _etaCursor;

    /**
     * A concrete implementation of the {@link IOrientationVectorIterator} that uses
     * the cursers of {@link IAngleImage} to iterate over an orientation image.
     * 
     * @param image 
     */
    public OrientationVectorIterator(IOrientationImage image) {
        _rhoCursor = image.getAngleImage(OrientationAngle.rho).getImage().getCursor();
        _deltaCursor = image.getAngleImage(OrientationAngle.delta).getImage().getCursor();
        _etaCursor = image.getAngleImage(OrientationAngle.eta).getImage().getCursor();
    }

    @Override
    public boolean hasNext() {
        return this._rhoCursor.hasNext();
    }

    @Override
    public IOrientationVector next() {
        Float32 rho = this._rhoCursor.next().value();
        Float32 delta = this._deltaCursor.next().value();
        Float32 eta = this._etaCursor.next().value();

        return new OrientationVector(rho.get(), delta.get(), eta.get());
    }

    @Override
    public void set(IOrientationVector vector) {
        Float32 rhoAngle = new Float32(vector.getAngle(OrientationAngle.rho));
        Pixel<Float32> rhoPixel = new Pixel<Float32>(rhoAngle);

        Float32 deltaAngle = new Float32(vector.getAngle(OrientationAngle.delta));
        Pixel<Float32> deltaPixel = new Pixel<Float32>(deltaAngle);

        Float32 etaAngle = new Float32(vector.getAngle(OrientationAngle.eta));
        Pixel<Float32> etaPixel = new Pixel<Float32>(etaAngle);

        this._rhoCursor.setPixel(rhoPixel);
        this._deltaCursor.setPixel(deltaPixel);
        this._etaCursor.setPixel(etaPixel);

    }

}