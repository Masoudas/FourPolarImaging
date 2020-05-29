package fr.fresnel.fourPolar.algorithm.visualization.figures.stickFigure.gauge2D;

import java.util.Arrays;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.IPixelRandomAccess;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.Pixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.orientation.IOrientationImageRandomAccess;
import fr.fresnel.fourPolar.core.image.soi.ISoIImage;
import fr.fresnel.fourPolar.core.physics.dipole.IOrientationVector;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationVector;
import fr.fresnel.fourPolar.core.util.image.colorMap.ColorMap;
import fr.fresnel.fourPolar.core.util.shape.IPointShape;
import fr.fresnel.fourPolar.core.util.shape.IShape;
import fr.fresnel.fourPolar.core.util.shape.IShapeIterator;
import fr.fresnel.fourPolar.core.util.shape.ShapeFactory;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.GaugeFigureFactory;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.GaugeFigureType;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.IGaugeFigure;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.AngleGaugeType;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.IAngleGaugePainter;

/**
 * Paints the dipole that represents the in plane orientation of a dipole. Note
 * that to accommodate {@link IGaugeFigure#AXIS_ORDER}, the dipole has as many
 * dimensions of the said axis order, even though only the xy direction is used.
 */
class SingleDipoleInPlaneStickPainter implements IAngleGaugePainter {
    private static final int FIGURE_DIM = IGaugeFigure.AXIS_ORDER.numAxis;

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

    private final long[] _stickTranslation;

    public SingleDipoleInPlaneStickPainter(ISingleDipoleStick2DPainterBuilder builder) {
        this._dipoleFigure = this._createDipoleFigure(builder.getSticklength(), builder.getSoIImage(),
                builder.getAngleGaugeType());
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

        this._stickTranslation = this._setStickTranslationToDipoleCenterPosition();
    }

    /**
     * Create a XYCZT gauge figure, where (X,Y) = (lenStick, lenStick) and (C,Z,T) =
     * (1,1,1). This is to make the gauge figure consistent with all the other gauge
     * figures. We give the figure a 10 percent margin to make sure that the stick
     * falls inside after translation and rotation
     * 
     */
    private IGaugeFigure _createDipoleFigure(int stickLength, ISoIImage soiImage, AngleGaugeType angleGaugeType) {
        long[] dim = new long[IGaugeFigure.AXIS_ORDER.numAxis];
        dim[0] = (long) (stickLength * 1.1);
        dim[1] = dim[0];

        IMetadata gaugeFigMetadata = new Metadata.MetadataBuilder(dim).axisOrder(AxisOrder.XYCZT).build();
        Image<RGB16> gaugeImage = soiImage.getImage().getFactory().create(gaugeFigMetadata, RGB16.zero());
        return GaugeFigureFactory.create(GaugeFigureType.SingleDipole, angleGaugeType, gaugeImage,
                soiImage.getFileSet());
    }

    /**
     * Base stick complies with the {@link FIGURE_DIM}
     */
    private IShape _defineBaseStick(int len, int thickness) {
        long[] stickMin = new long[FIGURE_DIM];
        long[] stickMax = new long[FIGURE_DIM];

        // Base stick is at the origin of the xy plane.
        stickMin[0] = -thickness / 2 + 1;
        stickMin[1] = -len / 2 + 1;
        stickMax[0] = thickness / 2;
        stickMax[1] = len / 2;

        return new ShapeFactory().closedBox(stickMin, stickMax, AxisOrder.XYCZT);
    }

    /**
     * Define the image region from pixel zero to dim - 1;
     */
    private IShape _defineOrientationImageBoundaryAsBoxShape(long[] imDimension, AxisOrder axisOrder) {
        long[] imageMax = null;
        long[] imageMin = null;

        imageMax = Arrays.stream(imDimension).map((x) -> x - 1).toArray();
        imageMin = new long[imDimension.length];

        return new ShapeFactory().closedBox(imageMin, imageMax, axisOrder);
    }

    /**
     * Each time the base stick is rotated, it must be moved to the center of the
     * dipole position.
     * 
     * @return
     */
    private long[] _setStickTranslationToDipoleCenterPosition() {
        long[] centerOfDipoleFigure = new long[FIGURE_DIM];
        centerOfDipoleFigure[0] = this._dipoleFigure.getImage().getMetadata().getDim()[0] / 2;
        centerOfDipoleFigure[1] = this._dipoleFigure.getImage().getMetadata().getDim()[1] / 2;
        return centerOfDipoleFigure;
    }

    @Override
    public void draw(IShape region, UINT16 soiThreshold) throws IllegalArgumentException {
        long[] dipolePosition = region.getIterator().next();

        if (!(region instanceof IPointShape)) {
            throw new IllegalArgumentException("Only point shape can be used to localize a dipole.");
        }

        if (region.axisOrder() != ISoIImage.AXIS_ORDER) {
            throw new IllegalArgumentException("The axis order of the point must be the same as soi image axis order.");
        }

        if (!this._orientationImageBoundary.isInside(dipolePosition)) {
            throw new IllegalArgumentException(
                    "The dipole coordinate for drawing the stick must be inside the orientation image");
        }

        this._paintStickFigureBlack();

        final IOrientationVector orientationVector = this._getOrientationOfThePosition(dipolePosition);
        if (_isSoIAboveThreshold(soiThreshold.get()) && _slopeAndColorAngleExist(orientationVector)) {
            _transformStick(dipolePosition, orientationVector);

            // Use the same pixel for every pixel of stick.
            final Pixel<RGB16> pixelColor = this._getStickColor(orientationVector);
            IPixelRandomAccess<RGB16> stickFigureRA = this._dipoleFigure.getImage().getRandomAccess();

            for (IShapeIterator stickIterator = this._stick.getIterator(); stickIterator.hasNext();) {
                long[] stickPosition = stickIterator.next();
                stickFigureRA.setPosition(stickPosition);
                stickFigureRA.setPixel(pixelColor);
            }
        }
    }

    @Override
    public IGaugeFigure getFigure() {
        return this._dipoleFigure;
    }

    private Pixel<RGB16> _getStickColor(IOrientationVector orientationVector) {
        RGB16 pixelColor = this._colormap.getColor(0, this._maxColorAngle, orientationVector.getAngle(_colorAngle));
        return new Pixel<RGB16>(pixelColor);
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
        this._stick.translate(this._stickTranslation);
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

    private IOrientationVector _getOrientationOfThePosition(long[] dipolePosition) {
        this._soiRA.setPosition(dipolePosition);
        return this._getOrientationVector(dipolePosition);
    }

}