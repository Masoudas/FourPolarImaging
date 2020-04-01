package fr.fresnel.fourPolar.algorithm.visualization.figures.stickFigure;

import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.util.DPoint;
import fr.fresnel.fourPolar.core.visualization.figures.stickFigure.IAngleStick;
import fr.fresnel.fourPolar.core.visualization.figures.stickFigure.IAngleStickIterator;

/**
 * A concrete implementation of {@link IAngleStick}. Note that the sticks can
 * have negative coordinates in this implementation.
 */
class AngleStick implements IAngleStick {
    private final DPoint _pose;
    private final RGB16 _color;
    private final int _len;
    private final float _slopeAngle;
    private final int _thickness;
    private final IAngleStickIterator _iterator;

    /**
     * Creates a stick representing the dipole based on the following parameters:
     * 
     * @param pose       is the position of the stick in the image coordinates.
     * @param slopeAngle is the angle corresponding this stick. It should be given
     *                   in radian.
     * @param len        is the length of the stick in pixels.
     * @param thickness  is the thickness of the stick in pixels.
     * @param color      is the {@link RGB16} color of the pixel.
     */
    public AngleStick(DPoint pose, float slopeAngle, int len, int thickness, RGB16 color, IAngleStickIterator iterator) {
        this._pose = pose;
        this._color = color;
        this._len = len;
        this._slopeAngle = slopeAngle;
        this._thickness = thickness;
        this._iterator = iterator;
    }

    @Override
    public DPoint getPosition() {
        return _pose;
    }

    @Override
    public RGB16 getColor() {
        return _color;
    }

    @Override
    public int getLength() {
        return _len;
    }

    @Override
    public float getSlopeAngle() {
        return _slopeAngle;
    }

    @Override
    public int getThickness() {
        return _thickness;
    }

    @Override
    public IAngleStickIterator getIterator() {
        return this._iterator;
    }
}