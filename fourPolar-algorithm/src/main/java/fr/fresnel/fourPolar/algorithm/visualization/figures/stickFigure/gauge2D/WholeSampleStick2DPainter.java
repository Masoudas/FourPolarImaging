package fr.fresnel.fourPolar.algorithm.visualization.figures.stickFigure.gauge2D;

import java.util.Arrays;

import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.ConverterToImgLib2NotFound;
import fr.fresnel.fourPolar.core.image.generic.IPixelRandomAccess;
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
    private static final int FIG_C_AXIS = IGaugeFigure.AXIS_ORDER.c_axis;
    private static final int FIG_T_AXIS = IGaugeFigure.AXIS_ORDER.t_axis;
    private static final int FIG_Z_AXIS = IGaugeFigure.AXIS_ORDER.z_axis;

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
    private final IShape _soiImageRegion;

    public WholeSampleStick2DPainter(WholeSampleStick2DPainterBuilder builder) throws ConverterToImgLib2NotFound {
        this._soiRA = builder.getSoIImage().getImage().getRandomAccess();

        this._stick2DFigure = builder.getGaugeFigure();

        this._orientationRA = builder.getOrientationImage().getRandomAccess();

        this._colormap = builder.getColorMap();

        this._slopeAngle = getSlopeAngle(this._stick2DFigure.getGaugeType());
        this._colorAngle = getColorAngle(this._stick2DFigure.getGaugeType());
        this._maxColorAngle = OrientationVector.maxAngle(_colorAngle);

        this._stick = this._defineBaseStick(builder.getSticklength(), builder.getStickThickness());

        this._stickFigureRegion = this._getImageBoundaryAsShape(
                this._stick2DFigure.getImage().getMetadata().getDim(),
                this._stick2DFigure.getImage().getMetadata().axisOrder());
        
        this._soiImageRegion = this._getImageBoundaryAsShape(
            builder.getSoIImage().getImage().getMetadata().getDim(),
            builder.getSoIImage().getImage().getMetadata().axisOrder());
    }

    /**
     * Define the image region from pixel zero to dim - 1;
     */
    private IShape _getImageBoundaryAsShape(long[] imDimension, AxisOrder axisOrder) {
        long[] imageMax = Arrays.stream(imDimension).map((x) -> x - 1).toArray();
        long[] imageMin = new long[imDimension.length];

        return new ShapeFactory().closedBox(imageMin, imageMax, axisOrder);
    }

    /**
     * Basic stick complies with the gauge figure, which is an XYZT image.
     */
    private IShape _defineBaseStick(int len, int thickness) {
        int numAxis = AxisOrder.getNumDefinedAxis(GAUGE_FIGURE_AXIS_ORDER);
        long[] stickMin = new long[numAxis];
        long[] stickMax = new long[numAxis];

        stickMin[0] = -len / 2 + 1;
        stickMin[1] = -thickness / 2 + 1;
        stickMax[0] = len / 2;
        stickMax[1] = thickness / 2;

        return new ShapeFactory().closedBox(stickMin, stickMax, AxisOrder.XYZT);
    }

    @Override
    public void draw(IShape region, UINT16 soiThreshold) {
        if (region.axisOrder() != AxisOrder.XYCZT) {
            throw new IllegalArgumentException("The region should be XYCZT.");
        }

        int threshold = soiThreshold.get();
        IPixelRandomAccess<RGB16> stickFigureRA = _stick2DFigure.getImage().getRandomAccess();

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

    /**
     * To accommodate for the mismatch between XYCZT and XYZT, translation is
     * redefined.
     * 
     * @param position          is the dipole position
     * @param orientationVector is the orientation vector at the position
     */
    private void _transformStick(long[] position, IOrientationVector orientationVector) {
        long[] translation = { position[0], position[1], position[3], position[4] };
        this._stick.resetToOriginalShape();
        this._stick.rotate2D(-orientationVector.getAngle(_slopeAngle));
        this._stick.translate(translation);
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