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
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.IAngleGaugePainter;

/**
 * Class that fills the proper gauge with 3D sticks. A 3D sticks represents in
 * plane angle (rho), and off-plane angle (eta) as a 3D stick, where the stick
 * color is the wobbling (delta). Note that stick length is closely associated
 * with the z-dimension of the images, in the sense that each 3D stick filles as
 * many z-planes as the length of the stick. Hence, the gauge figure need to
 * have been properly interleaved with z-planes to accommodate the 3D sticks.
 */
class Angle3DStickPainter implements IAngleGaugePainter {
    final private IGaugeFigure _stick3DFigure;
    final private IPixelRandomAccess<RGB16> _stick3DFigureRA;
    final private IOrientationImageRandomAccess _orientationRA;
    final private IPixelRandomAccess<UINT16> _soiRA;
    final private ColorMap _colormap;
    final private IShape _stickFigureRegion;

    /**
     * We generate a single stick, and then rotate and translate it for different
     * dipoles.
     */
    final private IShape _stick;

    /**
     * This is the interleaving factor (i.e, the stick len) in the z-direction.
     */
    final private int _zInterleaveFactor;

    /**
     * Create the 3D painter with the provided parameters.
     * 
     * @param gaugeFigure      is the gauge figure to be filled with this painter.
     * @param orientationImage is the orientation image associated with this gauge
     *                         figure.
     * @param soiImage         is the soi image associated with the orientation
     *                         image. Note that eventhough SoI is not used in the
     *                         final image, we need it for thresholding.
     * @param len              is the length of the stick (in z coordinates).
     * @param thickness        is the thickness of the stick.
     * @param colorMap         is the colormap used for representing the delta
     *                         angle.
     * 
     * @throws IllegalArgumentException in case the stick len or thickness is less
     *                                  than 2.
     */
    public Angle3DStickPainter(IGaugeFigure gaugeFigure, IOrientationImage orientationImage, ISoIImage soiImage,
            int len, int thickness, ColorMap colorMap) {
        Objects.requireNonNull(soiImage, "soiImage cannot be null");
        Objects.requireNonNull(orientationImage, "orientationImage cannot be null");
        Objects.requireNonNull(gaugeFigure, "gaugeFigure cannot be null");
        Objects.requireNonNull(colorMap, "colorMap cannot be null");

        if (len < 1 || len % 2 == 1) {
            throw new IllegalArgumentException("Stick len must be greater than one and it should be even.");
        }

        if (thickness < 1) {
            throw new IllegalArgumentException("Stick Thickness must be greater than one");
        }

        this._stick3DFigure = gaugeFigure;
        this._stick3DFigureRA = gaugeFigure.getImage().getRandomAccess();
        this._soiRA = soiImage.getImage().getRandomAccess();
        this._orientationRA = orientationImage.getRandomAccess();
        this._colormap = colorMap;
        this._zInterleaveFactor = len;

        this._stick = _defineBaseStick(len, thickness, gaugeFigure.getImage().getDimensions());
        this._stickFigureRegion = _defineGaugeImageBoundaryAsBoxShape(gaugeFigure.getImage().getDimensions());

    }

    /**
     * Define the image region from pixel zero to dim - 1;
     */
    private IShape _defineGaugeImageBoundaryAsBoxShape(long[] imDimension) {
        long[] imageMax = imDimension.clone();
        for (int i = 0; i < imageMax.length; i++) {
            imageMax[i] -= 1;
        }

        return new ShapeFactory().closedBox(new long[imDimension.length], imageMax);
    }

    private IShape _defineBaseStick(int len, int thickness, long[] imDimension) {
        long[] stickMin = new long[imDimension.length];
        long[] stickMax = new long[imDimension.length];

        stickMin[0] = -thickness / 2;
        stickMin[1] = -thickness / 2;
        stickMin[3] = -len / 2;
        stickMax[0] = thickness / 2;
        stickMax[1] = thickness / 2;
        stickMax[3] = len / 2;

        return new ShapeFactory().closedBox(stickMin, stickMax);
    }

    @Override
    public void draw(IShape region, UINT16 soiThreshold) {
        if (region.spaceDim() > this._stickFigureRegion.spaceDim()) {
            throw new IllegalArgumentException("The space dimension of the region to draw sticks over cannot"
                    + "be greater than the gauge figure dimension.");
        }
        int threshold = soiThreshold.get();
        Pixel<RGB16> pixel = new Pixel<>(RGB16.zero());

        IShapeIterator regionScalarItr = ShapeUtils.scaleShapeOverHigherDim(region,
                this._stick3DFigure.getImage().getDimensions());
        while (regionScalarItr.hasNext()) {
            long[] stickCenterPosition = regionScalarItr.next();

            if (_stickFigureRegion.isInside(stickCenterPosition)) {
                this._soiRA.setPosition(stickCenterPosition);
                final IOrientationVector orientationVector = this._getOrientationVector(stickCenterPosition);
                if (_isSoIAboveThreshold(threshold) && orientationVector.isWellDefined()) {
                    _drawStick(pixel, orientationVector, stickCenterPosition);
                }
            }

        }
    }

    private void _drawStick(Pixel<RGB16> pixel, IOrientationVector orientationVector, long[] stickCenterPosition) {
        _transformStick(stickCenterPosition, orientationVector);
        this._stick.and(this._stickFigureRegion);
        final RGB16 color = _getStickColor(orientationVector);

        IShapeIterator stickIterator = this._stick.getIterator();
        while (stickIterator.hasNext()) {
            long[] stickPosition = stickIterator.next();
            this._stick3DFigureRA.setPosition(stickPosition);
            pixel.value().set(color.getR(), color.getG(), color.getB());
            this._stick3DFigureRA.setPixel(pixel);

        }
    }

    private RGB16 _getStickColor(IOrientationVector orientationVector) {
        final RGB16 color = this._colormap.getColor(0, OrientationVector.maxAngle(OrientationAngle.delta),
                orientationVector.getAngle(OrientationAngle.delta));
        return color;
    }

    /**
     * To transform the base stick, suppose the dipole is located at x, y, z. Then
     * because the gauge figure has been interleaved in the z direction, we need to
     * draw the stick starting at z + stick_len / 2;
     * 
     * @param position          is the dipole position in the orientation image.
     * @param orientationVector is the orientation of the dipole.
     */
    private void _transformStick(long[] position, IOrientationVector orientationVector) {
        position[3] = position[3] + this._zInterleaveFactor / 2;
        this._stick.resetToOriginalShape();
        this._stick.transform(position, orientationVector.getAngle(OrientationAngle.eta),
                orientationVector.getAngle(OrientationAngle.rho), 0);
    }

    private IOrientationVector _getOrientationVector(long[] stickCenterPosition) {
        this._orientationRA.setPosition(stickCenterPosition);
        return this._orientationRA.getOrientation();
    }

    private boolean _isSoIAboveThreshold(int threshold) {
        return this._soiRA.getPixel().value().get() >= threshold;
    }

    @Override
    public IGaugeFigure getStickFigure() {
        return _stick3DFigure;
    }

}