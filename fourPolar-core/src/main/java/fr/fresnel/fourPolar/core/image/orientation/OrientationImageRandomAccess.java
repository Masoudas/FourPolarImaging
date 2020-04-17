package fr.fresnel.fourPolar.core.image.orientation;

import fr.fresnel.fourPolar.core.image.generic.IPixelRandomAccess;
import fr.fresnel.fourPolar.core.image.generic.pixel.Pixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.physics.dipole.IOrientationVector;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationVector;

/**
 * An implementation of random access that uses RA of each angle image.
 */
class OrientationImageRandomAccess implements IOrientationImageRandomAccess {
    private final IOrientationVector _orientationVector;
    private final IPixelRandomAccess<Float32> _rhoRA;
    private final IPixelRandomAccess<Float32> _deltaRA;
    private final IPixelRandomAccess<Float32> _etaRA;

    private final Pixel<Float32> _rhoPixel = new Pixel<Float32>(Float32.zero());
    private final Pixel<Float32> _deltaPixel = new Pixel<Float32>(Float32.zero());
    private final Pixel<Float32> _etaPixel = new Pixel<Float32>(Float32.zero());

    public OrientationImageRandomAccess(IPixelRandomAccess<Float32> rhoRA, IPixelRandomAccess<Float32> deltaRA,
            IPixelRandomAccess<Float32> etaRA) {
        this._rhoRA = rhoRA;
        this._deltaRA = deltaRA;
        this._etaRA = etaRA;

        this._orientationVector = new OrientationVector(0, 0, 0);
    }

    @Override
    public void setPosition(long[] position) {
        this._deltaRA.setPosition(position);
        this._rhoRA.setPosition(position);
        this._etaRA.setPosition(position);
    }

    @Override
    public void setOrientation(IOrientationVector orientationVector) throws ArrayIndexOutOfBoundsException {
        this._rhoPixel.value().set((float) orientationVector.getAngle(OrientationAngle.rho));
        this._deltaPixel.value().set((float) orientationVector.getAngle(OrientationAngle.delta));
        this._etaPixel.value().set((float) orientationVector.getAngle(OrientationAngle.eta));

        this._rhoRA.setPixel(this._rhoPixel);
        this._deltaRA.setPixel(this._deltaPixel);
        this._etaRA.setPixel(this._etaPixel);
    }

    @Override
    public IOrientationVector getOrientation() throws ArrayIndexOutOfBoundsException {
        float rho = this._rhoRA.getPixel().value().get();
        float delta = this._deltaRA.getPixel().value().get();
        float eta = this._etaRA.getPixel().value().get();
        this._orientationVector.setAngles(rho, delta, eta);
        return this._orientationVector;
    }

}