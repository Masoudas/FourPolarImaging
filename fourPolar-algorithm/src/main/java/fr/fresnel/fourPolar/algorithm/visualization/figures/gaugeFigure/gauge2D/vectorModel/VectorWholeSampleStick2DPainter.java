package fr.fresnel.fourPolar.algorithm.visualization.figures.gaugeFigure.gauge2D.vectorModel;

import java.util.Optional;

import fr.fresnel.fourPolar.core.image.generic.IPixelRandomAccess;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.ARGB8;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.orientation.IOrientationImageRandomAccess;
import fr.fresnel.fourPolar.core.image.soi.ISoIImage;
import fr.fresnel.fourPolar.core.image.vector.Vector;
import fr.fresnel.fourPolar.core.image.vector.filter.FilterComposite;
import fr.fresnel.fourPolar.core.physics.dipole.IOrientationVector;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationVector;
import fr.fresnel.fourPolar.core.shape.ILineShape;
import fr.fresnel.fourPolar.core.shape.IShape;
import fr.fresnel.fourPolar.core.shape.IShapeIterator;
import fr.fresnel.fourPolar.core.shape.ShapeFactory;
import fr.fresnel.fourPolar.core.util.image.generic.ImageUtil;
import fr.fresnel.fourPolar.core.util.image.generic.colorMap.ColorMap;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.IGaugeFigure;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.IAngleGaugePainter;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.vectorFigure.VectorGaugeFigure;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.vectorFigure.animation.OrientationAnimationCreator;

class VectorWholeSampleStick2DPainter implements IAngleGaugePainter {
    private final VectorGaugeFigure _figure;

    private final IShape _soiImageRegion;
    private final IOrientationImageRandomAccess _orientationRA;
    private final IPixelRandomAccess<UINT16> _soiRA;

    private final OrientationAngle _slopeAngle;
    private final OrientationAngle _colorAngle;
    private final double _maxColorAngle;

    private final Optional<OrientationAnimationCreator> _animCreator;
    private final ColorMap _colormap;
    private final long _stick_len;
    private final Optional<FilterComposite> _colorBlender;

    private final Vector _cachedVector;

    public VectorWholeSampleStick2DPainter(IVectorWholeSampleStick2DPainterBuilder builder) {
        this._figure = (VectorGaugeFigure) builder.getGauageFigure();
        _addSoIToFigureBackground(builder.getSoIImage());

        this._orientationRA = builder.getOrientationImage().getRandomAccess();
        this._soiRA = builder.getSoIImage().getImage().getRandomAccess();

        this._soiImageRegion = this._getImageBoundaryAsShape(builder.getSoIImage().getImage());

        this._colormap = builder.getColorMap();
        this._animCreator = builder.getAnimationCreator();

        this._slopeAngle = builder.getSlopeAngle();
        this._colorAngle = builder.getColorAngle();
        this._maxColorAngle = OrientationVector.maxAngle(_colorAngle);
        this._colorBlender = builder.getColorBlender();

        this._stick_len = builder.getSticklength();
        this._cachedVector = _createCachedVector(builder.getStickThickness());
    }

    /**
     * @return a cashed vector that is set to a dummy line shape, but has animation
     *         and thickness set to expected values, to avoid resetting it for each
     *         stick.
     */
    private Vector _createCachedVector(int stick_thickness) {
        ILineShape cachedDummyShape = ShapeFactory.line2DShape(new long[] { 0, 0 }, 0, 0, 0, AxisOrder.NoOrder);
        Vector cachedVector = Vector.createLineVector(cachedDummyShape);
        cachedVector.setStrokeWidth(stick_thickness);

        return cachedVector;
    }

    private void _addSoIToFigureBackground(ISoIImage soiImage) {
        _figure.getVectorImage().setImage(soiImage.getImage(), UINT16.zero());
    }

    private IShape _getImageBoundaryAsShape(Image<?> image) {
        return ImageUtil.getBoundaryAsBox(image);
    }

    @Override
    public void draw(IShape region, UINT16 soiThreshold) {
        if (region.axisOrder() != ISoIImage.AXIS_ORDER) {
            throw new IllegalArgumentException("The region should comply with the soi image axis order.");
        }

        int soi_threshold = soiThreshold.get();
        for (IShapeIterator iterator = region.getIterator(); iterator.hasNext();) {
            long[] regionPosition = iterator.next();
            if (_isPositionInsideSoI(regionPosition)) {
                long[] dipolePosition = regionPosition;

                final IOrientationVector orientationVector = this._getOrientationVector(dipolePosition);
                if (_isSoIAboveThreshold(soi_threshold, dipolePosition)
                        && _slopeAndColorAngleExist(orientationVector)) {
                    _drawStick(orientationVector, regionPosition);
                }
            }
        }
    }

    private boolean _isPositionInsideSoI(long[] regionPosition) {
        return this._soiImageRegion.isInside(regionPosition);
    }

    private IOrientationVector _getOrientationVector(long[] stickCenterPosition) {
        this._orientationRA.setPosition(stickCenterPosition);
        return this._orientationRA.getOrientation();
    }

    private boolean _isSoIAboveThreshold(int threshold, long[] dipolePosition) {
        this._soiRA.setPosition(dipolePosition);
        return this._soiRA.getPixel().value().get() >= threshold;
    }

    private boolean _slopeAndColorAngleExist(final IOrientationVector orientationVector) {
        return !Double.isNaN(orientationVector.getAngle(_slopeAngle))
                && !Double.isNaN(orientationVector.getAngle(_colorAngle));
    }

    /**
     * Draw the stick for the given orientation vector on the corresponding
     * position.
     */
    private void _drawStick(IOrientationVector orientationVector, long[] dipolePosition) {
        Vector dipoleStick = _generateDipoleStickUsingCachedVector(dipolePosition, orientationVector);
        _figure.getVectorImage().addVector(dipoleStick);
    }

    /**
     * Generate a stick that corresponds to the dipole position and the slope angle
     * using the cached vector.
     * 
     * @param position          is the dipole position
     * @param orientationVector is the orientation vector at the position
     * @return the cached vector, set to represent this stick.
     */
    private Vector _generateDipoleStickUsingCachedVector(long[] position, IOrientationVector orientationVector) {
        ARGB8 stickColor = _getStickColor(orientationVector);

        // The thickness parameter is set by vector stroke with, hence irrelevant here.
        ILineShape stickShape = ShapeFactory.line2DShape(position, Math.PI - orientationVector.getAngle(_slopeAngle),
                _stick_len, 1, IGaugeFigure.AXIS_ORDER);

        _cachedVector.setShape(stickShape);
        _cachedVector.setColor(stickColor);

        _colorBlender.ifPresent(blender -> _cachedVector.setFilter(blender));
        _animCreator.ifPresent(animCreator -> _cachedVector.setAnimation(animCreator.create(orientationVector)));
        return _cachedVector;
    }

    private ARGB8 _getStickColor(IOrientationVector orientationVector) {
        ARGB8 stickColor = this._colormap.getColor(0, this._maxColorAngle, orientationVector.getAngle(_colorAngle));
        stickColor.setAlpha(125);
        return stickColor;
    }

    @Override
    public IGaugeFigure getFigure() {
        return _figure;
    }
}