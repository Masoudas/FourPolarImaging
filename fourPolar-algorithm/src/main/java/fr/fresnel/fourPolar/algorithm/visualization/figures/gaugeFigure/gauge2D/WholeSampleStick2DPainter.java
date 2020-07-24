package fr.fresnel.fourPolar.algorithm.visualization.figures.gaugeFigure.gauge2D;

import fr.fresnel.fourPolar.algorithm.util.image.generic.color.GrayScaleToColorConverter;
import fr.fresnel.fourPolar.core.image.generic.IPixelRandomAccess;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.pixel.IPixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.ARGB8;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.color.ColorBlender;
import fr.fresnel.fourPolar.core.image.orientation.IOrientationImageRandomAccess;
import fr.fresnel.fourPolar.core.image.soi.ISoIImage;
import fr.fresnel.fourPolar.core.physics.dipole.IOrientationVector;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationVector;
import fr.fresnel.fourPolar.core.util.image.ImageUtil;
import fr.fresnel.fourPolar.core.util.image.colorMap.ColorMap;
import fr.fresnel.fourPolar.core.util.shape.IShape;
import fr.fresnel.fourPolar.core.util.shape.IShapeIterator;
import fr.fresnel.fourPolar.core.util.shape.ShapeFactory;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.GaugeFigure;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.IGaugeFigure;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.IAngleGaugePainter;

class WholeSampleStick2DPainter implements IAngleGaugePainter {
    final private GaugeFigure _stick2DFigure;

    final private IOrientationImageRandomAccess _orientationRA;
    final private IPixelRandomAccess<UINT16> _soiRA;
    final private ColorMap _colormap;

    /**
     * We generate a single stick, and then rotate and translate it for different
     * dipoles.
     */
    final private IShape _baseStick;

    private final OrientationAngle _slopeAngle;
    private final OrientationAngle _colorAngle;
    private final double _maxColorAngle;

    private final IShape _stickFigureRegion;
    private final IShape _soiImageRegion;

    private final ColorBlender _colorBlender;

    public WholeSampleStick2DPainter(IWholeSampleStick2DPainterBuilder builder) {
        this._soiRA = builder.getSoIImage().getImage().getRandomAccess();

        this._stick2DFigure = builder.getGauageFigure();
        _addSoIToFigureBackground(builder.getSoIImage());

        this._orientationRA = builder.getOrientationImage().getRandomAccess();

        this._colormap = builder.getColorMap();
        this._colorBlender = builder.getColorBlender();

        this._slopeAngle = builder.getSlopeAngle();
        this._colorAngle = builder.getColorAngle();
        this._maxColorAngle = OrientationVector.maxAngle(_colorAngle);

        this._baseStick = this._defineBaseStick(builder.getSticklength(), builder.getStickThickness());

        this._stickFigureRegion = this._getImageBoundaryAsShape(this._stick2DFigure.getImage());
        this._soiImageRegion = this._getImageBoundaryAsShape(builder.getSoIImage().getImage());
    }

    private void _addSoIToFigureBackground(ISoIImage soiImage) {
        GrayScaleToColorConverter.colorUsingMaxEachPlane(soiImage.getImage(), this._stick2DFigure.getImage());
    }

    private IShape _getImageBoundaryAsShape(Image<?> image) {
        return ImageUtil.getBoundaryAsBox(image);
    }

    /**
     * Basic stick complies with the gauge figure in number of dimensions.
     */
    private IShape _defineBaseStick(int len, int thickness) {
        int numAxis = IGaugeFigure.AXIS_ORDER.numAxis;
        long[] stickMin = new long[numAxis];
        long[] stickMax = new long[numAxis];

        stickMin[0] = -len / 2 + 1;
        stickMin[1] = -thickness / 2 + 1;
        stickMax[0] = len / 2;
        stickMax[1] = thickness / 2;

        return ShapeFactory.closedBox(stickMin, stickMax, AxisOrder.XYCZT);
    }

    @Override
    public void draw(IShape region, UINT16 soiThreshold) {
        if (region.axisOrder() != ISoIImage.AXIS_ORDER) {
            throw new IllegalArgumentException("The region should comply with the soi image axis order.");
        }

        int threshold = soiThreshold.get();
        IPixelRandomAccess<ARGB8> stickFigureRA = _stick2DFigure.getImage().getRandomAccess();

        IShapeIterator iterator = region.getIterator();
        while (iterator.hasNext()) {
            long[] stickCenterPosition = iterator.next();

            if (this._soiImageRegion.isInside(stickCenterPosition)) {
                this._soiRA.setPosition(stickCenterPosition);
                final IOrientationVector orientationVector = this._getOrientationVector(stickCenterPosition);
                if (_isSoIAboveThreshold(threshold) && _slopeAndColorAngleExist(orientationVector)) {
                    _drawStick(orientationVector, stickCenterPosition, stickFigureRA);
                }
            }

        }

    }

    /**
     * Draw the stick for the given orientation vector on the corresponding
     * position.
     */
    private void _drawStick(IOrientationVector orientationVector, long[] stickCenterPosition,
            IPixelRandomAccess<ARGB8> stickFigureRA) {
        ARGB8 stickColor = _getStickColor(orientationVector);

        IShapeIterator stickIterator = _createStickIteratorForThisDipole(orientationVector, stickCenterPosition);
        while (stickIterator.hasNext()) {
            long[] stickPosition = stickIterator.next();
            stickFigureRA.setPosition(stickPosition);

            IPixel<ARGB8> stickPositionPixel = stickFigureRA.getPixel();
            _blendCurrentPixelWithStickColor(stickPositionPixel.value(), stickColor);

            stickFigureRA.setPixel(stickPositionPixel);
        }
    }

    /**
     * As opposed to simply replacing or adding the stick color to the current
     * position, we blend it using a {@link ColorBlender} interface.
     */
    private void _blendCurrentPixelWithStickColor(ARGB8 stickPositionPixel, ARGB8 stickColor) {
        stickPositionPixel.add(stickColor);
        this._colorBlender.blend(stickPositionPixel, stickColor);
    }

    private IShapeIterator _createStickIteratorForThisDipole(IOrientationVector orientationVector,
            long[] stickCenterPosition) {
        IShape transformedStick = _transformStick(stickCenterPosition, orientationVector);
        IShapeIterator stickIterator = this._getPortionOfStickInsideFigureFrame(transformedStick);
        return stickIterator;
    }

    private IShapeIterator _getPortionOfStickInsideFigureFrame(IShape transformedStick) {
        IShape portionInsideFrame = transformedStick.and(this._stickFigureRegion);
        IShapeIterator stickIterator = portionInsideFrame.getIterator();
        return stickIterator;
    }

    /**
     * Translate the stick to the position in the gauge figure that corresponds to
     * the dipole position.
     * 
     * @param position          is the dipole position
     * @param orientationVector is the orientation vector at the position
     */
    private IShape _transformStick(long[] position, IOrientationVector orientationVector) {
        IShape transformedStick = this._baseStick.rotate2D(Math.PI - orientationVector.getAngle(_slopeAngle));
        return transformedStick.translate(position.clone());
    }

    private IOrientationVector _getOrientationVector(long[] stickCenterPosition) {
        this._orientationRA.setPosition(stickCenterPosition);
        return this._orientationRA.getOrientation();
    }

    private boolean _isSoIAboveThreshold(int threshold) {
        return this._soiRA.getPixel().value().get() >= threshold;
    }

    private boolean _slopeAndColorAngleExist(final IOrientationVector orientationVector) {
        return !Double.isNaN(orientationVector.getAngle(_slopeAngle))
                && !Double.isNaN(orientationVector.getAngle(_colorAngle));
    }

    private ARGB8 _getStickColor(IOrientationVector orientationVector) {
        ARGB8 stickColor = this._colormap.getColor(0, this._maxColorAngle, orientationVector.getAngle(_colorAngle));
        stickColor.setAlpha(255);
        return stickColor;
    }

    @Override
    public IGaugeFigure getFigure() {
        return _stick2DFigure;
    }
}