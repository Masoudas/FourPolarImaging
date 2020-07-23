package fr.fresnel.fourPolar.algorithm.visualization.figures.gaugeFigure.gauge2D;

import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.IPixelRandomAccess;
import fr.fresnel.fourPolar.core.image.generic.pixel.Pixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.ARGB8;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.orientation.IOrientationImage;
import fr.fresnel.fourPolar.core.image.orientation.IOrientationImageRandomAccess;
import fr.fresnel.fourPolar.core.image.soi.ISoIImage;
import fr.fresnel.fourPolar.core.physics.dipole.IOrientationVector;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationVector;
import fr.fresnel.fourPolar.core.util.image.ImageUtil;
import fr.fresnel.fourPolar.core.util.image.colorMap.ColorMap;
import fr.fresnel.fourPolar.core.util.shape.IPointShape;
import fr.fresnel.fourPolar.core.util.shape.IShape;
import fr.fresnel.fourPolar.core.util.shape.IShapeIterator;
import fr.fresnel.fourPolar.core.util.shape.ShapeFactory;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.GaugeFigure;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.IGaugeFigure;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.IAngleGaugePainter;

/**
 * Paints the dipole that represents the in plane orientation of a dipole. Note
 * that to accommodate {@link IGaugeFigure#AXIS_ORDER}, the dipole has as many
 * dimensions of the said axis order, even though only the xy direction is used.
 */
class SingleDipoleInPlaneStickPainter implements IAngleGaugePainter {
    // TODO : We should show the stick on the original image rather creating a
    // separate image for it!
    private static final int FIGURE_DIM = IGaugeFigure.AXIS_ORDER.numAxis;

    final private GaugeFigure _dipoleFigure;
    final private IOrientationImageRandomAccess _orientationRA;

    final private ColorMap _colormap;

    /**
     * We generate a single stick, and then rotate it to represent the unique
     * dipole.
     */
    private final IShape _baseStick;

    private final OrientationAngle _slopeAngle;
    private final OrientationAngle _colorAngle;
    private final double _maxColorAngle;

    private final IShape _orientationImageBoundary;

    private final long[] _centerOfFigure;

    public SingleDipoleInPlaneStickPainter(ISingleDipoleStick2DPainterBuilder builder) {
        this._dipoleFigure = (GaugeFigure) builder.getGaugeFigure();

        this._orientationRA = builder.getOrientationImage().getRandomAccess();

        this._colormap = builder.getColorMap();

        this._slopeAngle = builder.getSlopeAngle();
        this._colorAngle = builder.getColorAngle();
        this._maxColorAngle = OrientationVector.maxAngle(_colorAngle);

        this._baseStick = this._defineBaseStick(builder.getSticklength(), builder.getStickThickness());

        this._orientationImageBoundary = this._getOrientationImageBoundary(builder.getOrientationImage());

        this._centerOfFigure = this._getCenterOfGaugeFigure();
    }

    /**
     * Base stick complies with the {@link FIGURE_DIM}
     */
    private IShape _defineBaseStick(int len, int thickness) {
        return ShapeFactory.line2DShape(new long[FIGURE_DIM], 0, len, thickness, GaugeFigure.AXIS_ORDER);
    }

    /**
     * Define the image region from pixel zero to dim - 1;
     */
    private IShape _getOrientationImageBoundary(IOrientationImage orientationImage) {
        return ImageUtil.getBoundaryAsBox(orientationImage.getAngleImage(OrientationAngle.rho).getImage());
    }

    /**
     * Each time the base stick is rotated, it must be moved to the center of the
     * dipole position.
     * 
     * @return
     */
    private long[] _getCenterOfGaugeFigure() {
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
        if (_slopeAndColorAngleExist(orientationVector)) {
            // Use the same pixel for every pixel of stick.
            final Pixel<ARGB8> pixelColor = this._getStickColor(orientationVector);
            IPixelRandomAccess<ARGB8> stickFigureRA = this._dipoleFigure.getImage().getRandomAccess();

            IShape transformedStick = _transformBaseStick(orientationVector);
            for (IShapeIterator stickIterator = transformedStick.getIterator(); stickIterator.hasNext();) {
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

    private Pixel<ARGB8> _getStickColor(IOrientationVector orientationVector) {
        ARGB8 pixelColor = this._colormap.getColor(0, this._maxColorAngle, orientationVector.getAngle(_colorAngle));
        pixelColor.setAlpha(255);
        return new Pixel<ARGB8>(pixelColor);
    }

    private boolean _slopeAndColorAngleExist(final IOrientationVector orientationVector) {
        return !Double.isNaN(orientationVector.getAngle(_slopeAngle))
                && !Double.isNaN(orientationVector.getAngle(_colorAngle));
    }

    private IShape _transformBaseStick(IOrientationVector orientationVector) {
        IShape transformedShape = this._baseStick.rotate2D(Math.PI - orientationVector.getAngle(_slopeAngle));
        return transformedShape.translate(this._centerOfFigure);
    }

    /**
     * Set all pixels of the stick figure black, to ensure that in repetitive use of
     * the painter, the background remains constant.
     */
    private void _paintStickFigureBlack() {
        IPixelCursor<ARGB8> cursor = this._dipoleFigure.getImage().getCursor();

        Pixel<ARGB8> pixel = new Pixel<ARGB8>(new ARGB8(0, 0, 0));
        while (cursor.hasNext()) {
            cursor.next();
            cursor.setPixel(pixel);
        }
    }

    private IOrientationVector _getOrientationOfThePosition(long[] dipolePosition) {
        this._orientationRA.setPosition(dipolePosition);
        return this._orientationRA.getOrientation();
    }

}