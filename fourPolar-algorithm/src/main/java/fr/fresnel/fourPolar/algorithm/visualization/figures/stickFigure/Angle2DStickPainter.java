package fr.fresnel.fourPolar.algorithm.visualization.figures.stickFigure;

import java.util.Objects;

import fr.fresnel.fourPolar.core.image.generic.IPixelRandomAccess;
import fr.fresnel.fourPolar.core.image.generic.pixel.Pixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.orientation.IOrientationImage;
import fr.fresnel.fourPolar.core.image.orientation.IOrientationImageRandomAccess;
import fr.fresnel.fourPolar.core.image.polarization.soi.ISoIImage;
import fr.fresnel.fourPolar.core.physics.dipole.IOrientationVector;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationVector;
import fr.fresnel.fourPolar.core.util.image.colorMap.ColorMap;
import fr.fresnel.fourPolar.core.util.shape.IShape;
import fr.fresnel.fourPolar.core.util.shape.IShapeIterator;
import fr.fresnel.fourPolar.core.util.shape.ShapeFactory;
import fr.fresnel.fourPolar.core.util.shape.ShapeUtils;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.IGaugeFigure;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.AngleGaugeType;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.IAngleGaugePainter;

class Angle2DStickPainter implements IAngleGaugePainter {
    final private IGaugeFigure _stickFigure;
    final private IPixelRandomAccess<RGB16> _stickFigureRA;

    final private IOrientationImageRandomAccess _orientationRA;
    final private IPixelRandomAccess<UINT16> _soiRA;
    final private ColorMap _colormap;

    /**
     * We generate a single stick, and then transform it for different dipoles.
     */
    final private IShape _stick;

    private final OrientationAngle _slopeAngle;
    private final OrientationAngle _colorAngle;
    private final double _maxColorAngle;

    private final IShape _imageRegion;

    private final long[] _imDimension;

    /**
     * Create an empty stick figure, to be filled using the draw method.
     */
    public Angle2DStickPainter(IGaugeFigure gaugeFigure, IOrientationImage orientationImage, ISoIImage soiImage,
            int len, int thickness, ColorMap colorMap) {
        Objects.requireNonNull(soiImage, "soiImage cannot be null");
        Objects.requireNonNull(orientationImage, "orientationImage cannot be null");
        Objects.requireNonNull(gaugeFigure, "gaugeFigure cannot be null");
        Objects.requireNonNull(colorMap, "colorMap cannot be null");

        if (len < 1) {
            throw new IllegalArgumentException("len must be greater than one");
        }

        if (thickness < 1) {
            throw new IllegalArgumentException("thickness must be greater than one");
        }

        this._stickFigure = gaugeFigure;
        this._soiRA = soiImage.getImage().getRandomAccess();
        this._orientationRA = orientationImage.getRandomAccess();
        this._colormap = colorMap;
        this._stickFigureRA = _stickFigure.getImage().getRandomAccess();
        this._imDimension = soiImage.getImage().getDimensions();

        this._slopeAngle = this._getSlopeAngle(gaugeFigure.getType());
        this._colorAngle = this._getColorAngle(gaugeFigure.getType());
        this._maxColorAngle = OrientationVector.maxAngle(_colorAngle);

        this._stick = _defineBaseStick(len, thickness);
        this._imageRegion = _defineImageRegionAsBoxShape();

    }

    /**
     * Define the image region from pixel zero to dim - 1;
     */
    private IShape _defineImageRegionAsBoxShape() {
        long[] imageMax = _imDimension.clone();
        for (int i = 0; i < imageMax.length; i++) {
            imageMax[i] -= 1;
        }

        return new ShapeFactory().closedBox(new long[_imDimension.length], imageMax);
    }

    private IShape _defineBaseStick(int len, int thickness) {
        long[] stickMin = new long[_imDimension.length];
        long[] stickMax = new long[_imDimension.length];

        stickMin[0] = -thickness / 2;
        stickMin[1] = -len / 2;
        stickMax[0] = thickness / 2;
        stickMax[1] = len / 2;

        return new ShapeFactory().closedBox(stickMin, stickMax);
    }

    @Override
    public void draw(IShape region, UINT16 soiThreshold) {
        if (region.spaceDim() > this._imDimension.length) {
            throw new IllegalArgumentException("The space dimension of the region to draw sticks over cannot"
                    + "be greater than the SoI image dimension.");
        }
        int threshold = soiThreshold.get();
        Pixel<RGB16> pixel = new Pixel<>(RGB16.zero());

        IShapeIterator pixelScalarItr = ShapeUtils.scaleShapeOverHigherDim(region, this._imDimension);
        while (pixelScalarItr.hasNext()) {
            long[] stickCenterPosition = pixelScalarItr.next();

            if (_imageRegion.isInside(stickCenterPosition)) {
                this._soiRA.setPosition(stickCenterPosition);
                final IOrientationVector orientationVector = this._getOrientationVector(stickCenterPosition);
                if (_isSoIAboveThreshold(threshold) && _slopeAndColorAngleExist(orientationVector)) {
                    _drawStick(pixel, orientationVector, stickCenterPosition);
                }
            }

        }

    }

    private void _drawStick(Pixel<RGB16> pixel, IOrientationVector orientationVector, long[] stickCenterPosition) {
        _transformStick(stickCenterPosition, orientationVector);
        this._stick.and(this._imageRegion);
        final RGB16 color = _getStickColor(orientationVector);

        IShapeIterator stickIterator = this._stick.getIterator();
        while (stickIterator.hasNext()) {
            long[] stickPosition = stickIterator.next();
            this._stickFigureRA.setPosition(stickPosition);
            pixel.value().set(color.getR(), color.getG(), color.getB());
            this._stickFigureRA.setPixel(pixel);

        }
    }

    private RGB16 _getStickColor(IOrientationVector orientationVector) {
        final RGB16 color = this._colormap.getColor(0, this._maxColorAngle, orientationVector.getAngle(_colorAngle));
        return color;
    }

    private void _transformStick(long[] position, IOrientationVector orientationVector) {
        this._stick.resetToOriginalShape();
        this._stick.transform(position, 0, Math.PI / 2 + orientationVector.getAngle(_slopeAngle), 0);
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
    public IGaugeFigure getStickFigure() {
        return _stickFigure;
    }

    private OrientationAngle _getSlopeAngle(AngleGaugeType type) {
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

    private OrientationAngle _getColorAngle(AngleGaugeType type) {
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