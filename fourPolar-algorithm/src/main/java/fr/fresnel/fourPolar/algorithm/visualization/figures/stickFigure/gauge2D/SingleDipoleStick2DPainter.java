package fr.fresnel.fourPolar.algorithm.visualization.figures.stickFigure.gauge2D;

import java.util.Arrays;

import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
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
import fr.fresnel.fourPolar.core.util.shape.ShapeType;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.IGaugeFigure;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.IAngleGaugePainter;

class SingleDipoleStick2DPainter implements IAngleGaugePainter {
    final private IGaugeFigure _dipoleFigure;
    final private IOrientationImageRandomAccess _orientationRA;
    final private IPixelRandomAccess<UINT16> _soiRA;

    final private ColorMap _colormap;

    /**
     * We generate a single stick, and then rotate it to represent the unique
     * dipole.
     */
    final private IShape _stick;

    private final OrientationAngle _slopeAngle;
    private final OrientationAngle _colorAngle;
    private final double _maxColorAngle;

    private final IShape _orientationImageBoundary;

    public SingleDipoleStick2DPainter(SingleDipoleStick2DPainterBuilder builder) {
        this._dipoleFigure = builder.getGaugeFigure();
        this._orientationRA = builder.getOrientationImage().getRandomAccess();
        this._soiRA = builder.getSoIImage().getImage().getRandomAccess();

        this._colormap = builder.getColorMap();

        this._slopeAngle = WholeSampleStick2DPainter.getSlopeAngle(this._dipoleFigure.getGaugeType());
        this._colorAngle = WholeSampleStick2DPainter.getColorAngle(this._dipoleFigure.getGaugeType());
        this._maxColorAngle = OrientationVector.maxAngle(_colorAngle);

        this._stick = this._defineBaseStick(builder.getSticklength(), builder.getStickThickness());

        this._orientationImageBoundary = this._defineOrientationImageBoundaryAsBoxShape(
                builder.getOrientationImage().getAngleImage(OrientationAngle.rho).getImage().getMetadata().getDim(),
                builder.getOrientationImage().getAngleImage(OrientationAngle.rho).getImage().getMetadata().axisOrder());

    }

    private IShape _defineBaseStick(int len, int thickness) {
        long[] stickMin = new long[2];
        long[] stickMax = new long[2];

        stickMin[0] = -thickness / 2 + 1;
        stickMin[1] = -len / 2 + 1;
        stickMax[0] = thickness / 2;
        stickMax[1] = len / 2;

        return new ShapeFactory().closedBox(stickMin, stickMax, AxisOrder.XY);
    }

    /**
     * Define the image region from pixel zero to dim - 1;
     */
    private IShape _defineOrientationImageBoundaryAsBoxShape(long[] imDimension, AxisOrder axisOrder) {
        long[] imageMax = null;
        long[] imageMin = null;

        if (imDimension.length == 1) {
            imageMax = new long[] { imDimension[0] - 1, 0 };
            imageMin = new long[2];
        } else {
            imageMax = Arrays.stream(imDimension).map((x) -> x - 1).toArray();
            imageMin = new long[imDimension.length];
        }

        return new ShapeFactory().closedBox(imageMin, imageMax, axisOrder);
    }

    @Override
    public void draw(IShape region, UINT16 soiThreshold) throws IllegalArgumentException {
        long[] dipolePosition = region.getIterator().next();

        if (region.getType() != ShapeType.Point) {
            throw new IllegalArgumentException("Only point shape can be used to localize a dipole.");
        }

        if (region.axisOrder() != this._orientationImageBoundary.axisOrder()) {
            throw new IllegalArgumentException(
                    "The given point should be defined over the same axis order as orientation image.");
        }

        if (!this._orientationImageBoundary.isInside(dipolePosition)) {
            throw new IllegalArgumentException(
                    "The dipole coordinate for drawing the stick must be inside the orientation image");
        }

        this._paintStickFigureBlack();

        int threshold = soiThreshold.get();
        IPixelRandomAccess<RGB16> stickFigureRA = this._dipoleFigure.getImage().getRandomAccess();

        this._soiRA.setPosition(dipolePosition);
        final IOrientationVector orientationVector = this._getOrientationVector(dipolePosition);

        if (_isSoIAboveThreshold(threshold) && _slopeAndColorAngleExist(orientationVector)) {
            _transformStick(dipolePosition, orientationVector);

            final RGB16 pixelColor = _getStickColor(orientationVector);
            Pixel<RGB16> pixelValue = new Pixel<RGB16>(pixelColor); // Use the same pixel for every pixel of stick.

            IShapeIterator stickIterator = this._stick.getIterator();
            while (stickIterator.hasNext()) {
                long[] stickPosition = stickIterator.next();
                stickFigureRA.setPosition(stickPosition);
                stickFigureRA.setPixel(pixelValue);

            }
        }
    }

    @Override
    public IGaugeFigure getFigure() {
        return this._dipoleFigure;
    }

    private RGB16 _getStickColor(IOrientationVector orientationVector) {
        return this._colormap.getColor(0, this._maxColorAngle, orientationVector.getAngle(_colorAngle));
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

    private void _transformStick(long[] position, IOrientationVector orientationVector) {
        this._stick.resetToOriginalShape();

        this._stick.rotate2D(Math.PI / 2 + orientationVector.getAngle(_slopeAngle));

        // Move the stick to the center of figure
        long stickCenter = this._dipoleFigure.getImage().getMetadata().getDim()[0];
        this._stick.translate(new long[] { stickCenter / 2, stickCenter / 2 });
    }

    /**
     * Set all pixels of the stick figure black, to ensure that in repetitive use of
     * the painter, the background remains
     */
    private void _paintStickFigureBlack() {
        IPixelCursor<RGB16> cursor = this._dipoleFigure.getImage().getCursor();

        Pixel<RGB16> pixel = new Pixel<RGB16>(new RGB16(0, 0, 0));
        while (cursor.hasNext()) {
            cursor.next();
            cursor.setPixel(pixel);
        }
    }

}