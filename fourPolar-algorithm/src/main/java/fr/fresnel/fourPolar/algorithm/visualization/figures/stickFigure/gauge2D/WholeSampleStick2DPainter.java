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
import fr.fresnel.fourPolar.core.image.soi.ISoIImage;
import fr.fresnel.fourPolar.core.physics.dipole.IOrientationVector;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationVector;
import fr.fresnel.fourPolar.core.util.image.colorMap.ColorMap;
import fr.fresnel.fourPolar.core.util.shape.IShape;
import fr.fresnel.fourPolar.core.util.shape.IShapeIterator;
import fr.fresnel.fourPolar.core.util.shape.ShapeFactory;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.GaugeFigureFactory;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.GaugeFigureType;
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
    private final IShape _soiImageRegion;

    public WholeSampleStick2DPainter(IWholeSampleStick2DPainterBuilder builder) throws ConverterToImgLib2NotFound {
        this._soiRA = builder.getSoIImage().getImage().getRandomAccess();

        this._stick2DFigure = this._createGaugeFigure(builder.getSoIImage(), builder.getAngleGaugeType());

        this._orientationRA = builder.getOrientationImage().getRandomAccess();

        this._colormap = builder.getColorMap();

        this._slopeAngle = getSlopeAngle(this._stick2DFigure.getGaugeType());
        this._colorAngle = getColorAngle(this._stick2DFigure.getGaugeType());
        this._maxColorAngle = OrientationVector.maxAngle(_colorAngle);

        this._stick = this._defineBaseStick(builder.getSticklength(), builder.getStickThickness());

        this._stickFigureRegion = this._getImageBoundaryAsShape(this._stick2DFigure.getImage().getMetadata().getDim(),
                this._stick2DFigure.getImage().getMetadata().axisOrder());

        this._soiImageRegion = this._getImageBoundaryAsShape(builder.getSoIImage().getImage().getMetadata().getDim(),
                builder.getSoIImage().getImage().getMetadata().axisOrder());
    }

    /**
     * Create the gauge figure by creating a color version of SoI.
     */
    private IGaugeFigure _createGaugeFigure(ISoIImage soiImage, AngleGaugeType gaugeType) {
        Image<RGB16> gaugeImage = null;
        try {
            gaugeImage = GrayScaleToColorConverter.useMaxEachPlane(soiImage.getImage());
        } catch (ConverterToImgLib2NotFound e) {
            // We expect this exception to have been caught before the program arrives here!
        }

        return GaugeFigureFactory.create(GaugeFigureType.WholeSample, gaugeType, gaugeImage, soiImage.getFileSet());
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

        return new ShapeFactory().closedBox(stickMin, stickMax, AxisOrder.XYCZT);
    }

    @Override
    public void draw(IShape region, UINT16 soiThreshold) {
        if (region.axisOrder() != ISoIImage.AXIS_ORDER) {
            throw new IllegalArgumentException("The region should comply with the soi image axis order.");
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

        Pixel<RGB16> pixelValue = _getStickColor(orientationVector);

        IShapeIterator stickIterator = this._stick.getIterator();
        while (stickIterator.hasNext()) {
            long[] stickPosition = stickIterator.next();
            stickFigureRA.setPosition(stickPosition);
            stickFigureRA.setPixel(pixelValue);
        }
    }

    /**
     * Translate the stick to the position in the gauge figure that corresponds to
     * the dipole position.
     * 
     * @param position          is the dipole position
     * @param orientationVector is the orientation vector at the position
     */
    private void _transformStick(long[] position, IOrientationVector orientationVector) {
        this._stick.resetToOriginalShape();
        this._stick.rotate2D(-orientationVector.getAngle(_slopeAngle));
        this._stick.translate(position.clone());
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

    private Pixel<RGB16> _getStickColor(IOrientationVector orientationVector) {
        RGB16 pixelColor = this._colormap.getColor(0, this._maxColorAngle, orientationVector.getAngle(_colorAngle));
        return new Pixel<RGB16>(pixelColor);
    }

    @Override
    public IGaugeFigure getFigure() {
        return _stick2DFigure;
    }

    public static OrientationAngle getSlopeAngle(AngleGaugeType type) {
        switch (type) {
            case Rho2D:
                return OrientationAngle.rho;

            case Delta2D:
                return OrientationAngle.rho;

            case Eta2D:
                return OrientationAngle.rho;

            default:
                return null;
        }

    }

    public static OrientationAngle getColorAngle(AngleGaugeType type) {
        switch (type) {
            case Rho2D:
                return OrientationAngle.rho;

            case Delta2D:
                return OrientationAngle.delta;

            case Eta2D:
                return OrientationAngle.eta;

            default:
                return null;
        }

    }
}