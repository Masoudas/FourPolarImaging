package fr.fresnel.fourPolar.algorithm.visualization.figures.stickFigure.gauge2D;

import java.util.Arrays;

import fr.fresnel.fourPolar.algorithm.util.image.converters.GrayScaleToColorConverter;
import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.ConverterToImgLib2NotFound;
import fr.fresnel.fourPolar.core.image.generic.IPixelRandomAccess;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.pixel.Pixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.orientation.IOrientationImageRandomAccess;
import fr.fresnel.fourPolar.core.physics.dipole.IOrientationVector;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationVector;
import fr.fresnel.fourPolar.core.util.image.colorMap.ColorMap;
import fr.fresnel.fourPolar.core.util.shape.IShape;
import fr.fresnel.fourPolar.core.util.shape.IShapeIterator;
import fr.fresnel.fourPolar.core.util.shape.ShapeFactory;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.IGaugeFigure;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.AngleGaugeType;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.IAngleGaugePainter;

class WholeSampleStick2DPainter implements IAngleGaugePainter {
    final private IGaugeFigure _stick2DFigure;

    final private IOrientationImageRandomAccess _orientationRA;
    final private IPixelRandomAccess<UINT16> _soiRA;
    final private ColorMap _colormap;

    /**
     * We generate a single stick, and then rotate and translate it for different
     * dipoles.
     */
    final private IShape _stick;

    private final OrientationAngle _slopeAngle;
    private final OrientationAngle _colorAngle;
    private final double _maxColorAngle;

    private final IShape _stickFigureRegion;

    public WholeSampleStick2DPainter(WholeSampleStick2DPainterBuilder builder) throws ConverterToImgLib2NotFound {
        this._soiRA = builder.getSoIImage().getImage().getRandomAccess();

        this._stick2DFigure = builder.getGaugeFigure();

        this._orientationRA = builder.getOrientationImage().getRandomAccess();

        this._colormap = builder.getColorMap();

        this._slopeAngle = getSlopeAngle(this._stick2DFigure.getGaugeType());
        this._colorAngle = getColorAngle(this._stick2DFigure.getGaugeType());
        this._maxColorAngle = OrientationVector.maxAngle(_colorAngle);

        this._stick = this._defineBaseStick(builder.getSticklength(), builder.getStickThickness(),
                this._stick2DFigure.getImage().getMetadata().axisOrder());

        this._stickFigureRegion = this._defineGaugeImageBoundaryAsBoxShape(
                this._stick2DFigure.getImage().getDimensions(),
                this._stick2DFigure.getImage().getMetadata().axisOrder());

        this._fillGaugeFigureWithSoI(builder.getSoIImage().getImage(), this._stick2DFigure.getImage());
    }

    /**
     * Place the soi of each dipole in the corresponding position in the gauge
     * figure.
     * 
     * @throws ConverterToImgLib2NotFound
     */
    private void _fillGaugeFigureWithSoI(Image<UINT16> soiImage, Image<RGB16> gaugeFigure)
            throws ConverterToImgLib2NotFound {
        GrayScaleToColorConverter.useMaxEachPlane(soiImage, gaugeFigure);
    }

    /**
     * Define the image region from pixel zero to dim - 1;
     */
    private IShape _defineGaugeImageBoundaryAsBoxShape(long[] imDimension, AxisOrder axisOrder) {
        long[] imageMax = Arrays.stream(imDimension).map((x) -> x - 1).toArray();
        long[] imageMin = new long[imDimension.length];

        return new ShapeFactory().closedBox(imageMin, imageMax, axisOrder);
    }

    private IShape _defineBaseStick(int len, int thickness, AxisOrder axisOrder) {
        long[] stickMin = new long[AxisOrder.getNumDefinedAxis(axisOrder)];
        long[] stickMax = new long[AxisOrder.getNumDefinedAxis(axisOrder)];

        stickMin[0] = -len / 2 + 1;
        stickMin[1] = -thickness / 2 + 1;
        stickMax[0] = len / 2;
        stickMax[1] = thickness / 2;

        return new ShapeFactory().closedBox(stickMin, stickMax, axisOrder);
    }

    @Override
    public void draw(IShape region, UINT16 soiThreshold) {
        AxisOrder stickFigureAxis = this._stick2DFigure.getImage().getMetadata().axisOrder();
        if (region.axisOrder() != stickFigureAxis) {
            throw new IllegalArgumentException(
                "The region should be defined over the same axis order as orientation image.");
        }

        int threshold = soiThreshold.get();
        IPixelRandomAccess<RGB16> stickFigureRA = _stick2DFigure.getImage().getRandomAccess();

        IShapeIterator iterator = region.getIterator();
        while (iterator.hasNext()) {
            long[] stickCenterPosition = iterator.next();

            if (_stickFigureRegion.isInside(stickCenterPosition)) {
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
            IPixelRandomAccess<RGB16> stickFigureRA) {
        _transformStick(stickCenterPosition, orientationVector);
        this._stick.and(this._stickFigureRegion);

        final RGB16 pixelColor = _getStickColor(orientationVector);
        Pixel<RGB16> pixelValue = new Pixel<RGB16>(pixelColor); // Use the same pixel for every pixel of stick.

        IShapeIterator stickIterator = this._stick.getIterator();
        while (stickIterator.hasNext()) {
            long[] stickPosition = stickIterator.next();
            stickFigureRA.setPosition(stickPosition);
            stickFigureRA.setPixel(pixelValue);
        }
    }

    private RGB16 _getStickColor(IOrientationVector orientationVector) {
        return this._colormap.getColor(0, this._maxColorAngle, orientationVector.getAngle(_colorAngle));
    }

    private void _transformStick(long[] position, IOrientationVector orientationVector) {
        this._stick.resetToOriginalShape();
        this._stick.rotate2D(-orientationVector.getAngle(_slopeAngle));
        this._stick.translate(position);
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

    @Override
    public IGaugeFigure getFigure() {
        return _stick2DFigure;
    }

    public static OrientationAngle getSlopeAngle(AngleGaugeType type) {
        OrientationAngle angle = null;
        switch (type) {
            case Rho2D:
                angle = OrientationAngle.rho;
                break;

            case Delta2D:
                angle = OrientationAngle.rho;
                break;

            case Eta2D:
                angle = OrientationAngle.rho;
                break;

            default:
                break;
        }

        return angle;
    }

    public static OrientationAngle getColorAngle(AngleGaugeType type) {
        OrientationAngle angle = null;
        switch (type) {
            case Rho2D:
                angle = OrientationAngle.rho;
                break;

            case Delta2D:
                angle = OrientationAngle.delta;
                break;

            case Eta2D:
                angle = OrientationAngle.eta;
                break;

            default:
                break;
        }

        return angle;
    }
}