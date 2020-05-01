package fr.fresnel.fourPolar.algorithm.visualization.figures.stickFigure.gauge3D;

import java.util.Arrays;

import fr.fresnel.fourPolar.core.physics.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.IPixelRandomAccess;
import fr.fresnel.fourPolar.core.image.generic.Image;
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
import fr.fresnel.fourPolar.core.util.shape.Rotation3DOrder;
import fr.fresnel.fourPolar.core.util.shape.ShapeFactory;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.IGaugeFigure;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.IAngleGaugePainter;

/**
 * Class that fills the proper gauge with 3D sticks. See
 * {@link WholeSampleStick3DPainterBuilder}.
 */
class WholeSampleStick3DPainter implements IAngleGaugePainter {
    final private long[] _soiImageDim;
    final private IGaugeFigure _stick3DFigure;
    final private IPixelRandomAccess<RGB16> _stick3DFigureRA;
    final private IOrientationImageRandomAccess _orientationRA;
    final private AxisOrder _soiImageAxisOrder;
    final private IPixelRandomAccess<UINT16> _soiRA;
    final private ColorMap _colormap;
    final private IShape _soiImageBoundary;
    final private IShape _stick3DFigureBoundary;
    final private int _stickLength;

    /**
     * We generate a single stick, and then rotate and translate it for different
     * dipoles.
     */
    final private IShape _stick;

    /**
     * Create the 3D painter using the builder parameters.
     */
    public WholeSampleStick3DPainter(WholeSampleStick3DPainterBuilder builder) {
        this._soiImageDim = builder.getSoIImage().getImage().getDimensions();
        this._soiRA = builder.getSoIImage().getImage().getRandomAccess();
        this._soiImageAxisOrder = builder.getSoIImage().getImage().getMetadata().axisOrder();
        this._soiImageBoundary = _defineImageBoundaryAsBox(builder.getSoIImage().getImage());

        this._stick3DFigure = builder.getGaugeFigure();
        this._stick3DFigureRA = this._stick3DFigure.getImage().getRandomAccess();
        this._stick3DFigureBoundary = _defineImageBoundaryAsBox(this._stick3DFigure.getImage());

        this._orientationRA = builder.getOrientationImage().getRandomAccess();

        this._colormap = builder.getColorMap();
        this._stickLength = builder.getSticklength();

        this._stick = _defineBaseStick(this._stickLength, builder.getStickThickness(),
                this._stick3DFigure.getImage().getMetadata().axisOrder());
    }

    /**
     * Define the image region as a box spanning from pixel zero to dim - 1;
     */
    private IShape _defineImageBoundaryAsBox(Image<?> image) {
        long[] imageMax = Arrays.stream(image.getDimensions()).map((t) -> t - 1).toArray();
        long[] imageMin = new long[image.getDimensions().length];

        return new ShapeFactory().closedBox(imageMin, imageMax, image.getMetadata().axisOrder());
    }

    private IShape _defineBaseStick(int len, int thickness, AxisOrder axisOrder) {
        long[] stickMin = new long[AxisOrder.getNumDefinedAxis(axisOrder)];
        long[] stickMax = new long[AxisOrder.getNumDefinedAxis(axisOrder)];

        int zAxis = AxisOrder.getZAxis(axisOrder);
        stickMin[0] = -thickness / 2 + 1;
        stickMin[1] = -thickness / 2 + 1;
        stickMin[zAxis] = -len / 2 + 1;
        stickMax[0] = thickness / 2;
        stickMax[1] = thickness / 2;
        stickMax[zAxis] = len / 2;

        return new ShapeFactory().closedBox(stickMin, stickMax, axisOrder);
    }

    @Override
    public void draw(IShape region, UINT16 soiThreshold) {
        if (region.axisOrder() != this._soiImageBoundary.axisOrder()) {
            throw new IllegalArgumentException(
                    "The region should be defined over the same axis order as orientation image.");
        }
        int threshold = soiThreshold.get();
        Pixel<RGB16> pixel = new Pixel<>(RGB16.zero());

        IShapeIterator iterator = region.getIterator();
        while (iterator.hasNext()) {
            long[] dipolePosition = iterator.next();

            if (_soiImageBoundary.isInside(dipolePosition)) {
                this._soiRA.setPosition(dipolePosition);
                final IOrientationVector orientationVector = this._getOrientationVector(dipolePosition);

                if (_isSoIAboveThreshold(threshold) && orientationVector.isWellDefined()) {
                    _drawStick(pixel, orientationVector, dipolePosition);
                }
            }

        }
    }

    private void _drawStick(Pixel<RGB16> pixel, IOrientationVector orientationVector, long[] dipolePosition) {
        _transformStick(dipolePosition, orientationVector);
        final RGB16 color = _getStickColor(orientationVector);

        IShapeIterator stickIterator = this._stick.getIterator();
        while (stickIterator.hasNext()) {
            long[] stickPosition = stickIterator.next();
            if (this._stick3DFigureBoundary.isInside(stickPosition)) {
                this._stick3DFigureRA.setPosition(stickPosition);
                pixel.value().set(color.getR(), color.getG(), color.getB());
                this._stick3DFigureRA.setPixel(pixel);
            }
        }
    }

    private RGB16 _getStickColor(IOrientationVector orientationVector) {
        final RGB16 color = this._colormap.getColor(0, OrientationVector.maxAngle(OrientationAngle.delta),
                orientationVector.getAngle(OrientationAngle.delta));
        return color;
    }

    /**
     * To transform the base stick, suppose the dipole is located at x, y, z. Then
     * because the gauge figure has been interleaved in the z direction, the dipole
     * position would be in z + stick_len / 2, with stick stetching (possibly) from
     * z to z + stick_len.
     * 
     * Regarding the rotations, note that based on the definition of the base stick
     * (it's parallel to the z axis), if eta = 90 and rho = 0, then the axis must be
     * parallel to the x axis. Hence, when the orientation angle is 0, we need to
     * apply a -90 shift to the rho angle to ensure that the stick is represented
     * properly.
     * 
     * @param dipolePosition    is the dipole position in the orientation image.
     * @param orientationVector is the orientation of the dipole.
     */
    private void _transformStick(long[] dipolePosition, IOrientationVector orientationVector) {
        long[] stickTranslation = null;

        // In case the original image is planar, add the z translation, otherwise
        // use the z position in the soi image.
        if (AxisOrder.getNumDefinedAxis(this._soiImageAxisOrder) < 3) {
            stickTranslation = new long[3];
            System.arraycopy(dipolePosition, 0, stickTranslation, 0, 2);
            stickTranslation[2] = this._stickLength / 2 - 1;
        } else {
            stickTranslation = dipolePosition.clone();
            stickTranslation[2] = dipolePosition[3] * this._stickLength + this._stickLength / 2 - 1;
            stickTranslation[3] = dipolePosition[2];
        }

        this._stick.resetToOriginalShape();
        this._stick.rotate3D(orientationVector.getAngle(OrientationAngle.eta),
                -Math.PI / 2 + orientationVector.getAngle(OrientationAngle.rho), 0, Rotation3DOrder.XZY);
        this._stick.translate(stickTranslation);
    }

    private IOrientationVector _getOrientationVector(long[] stickCenterPosition) {
        this._orientationRA.setPosition(stickCenterPosition);
        return this._orientationRA.getOrientation();
    }

    private boolean _isSoIAboveThreshold(int threshold) {
        return this._soiRA.getPixel().value().get() >= threshold;
    }

    @Override
    public IGaugeFigure getFigure() {
        return _stick3DFigure;
    }

}