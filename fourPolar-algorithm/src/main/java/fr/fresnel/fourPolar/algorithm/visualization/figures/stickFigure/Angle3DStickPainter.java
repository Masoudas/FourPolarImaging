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
    final private long[] _soiImageDim;
    final private IGaugeFigure _stick3DFigure;
    final private IPixelRandomAccess<RGB16> _stick3DFigureRA;
    final private IOrientationImageRandomAccess _orientationRA;
    final private IPixelRandomAccess<UINT16> _soiRA;
    final private ColorMap _colormap;
    final private IShape _soiImageBoundary;
    final private IShape _stick3DFigureBoundary;

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

        this._soiImageDim = soiImage.getImage().getDimensions();
        this._stick3DFigure = gaugeFigure;
        this._stick3DFigureRA = gaugeFigure.getImage().getRandomAccess();
        this._soiRA = soiImage.getImage().getRandomAccess();
        this._orientationRA = orientationImage.getRandomAccess();
        this._colormap = colorMap;
        this._zInterleaveFactor = len;

        this._stick = _defineBaseStick(len, thickness, gaugeFigure.getImage().getDimensions());
        this._soiImageBoundary = _defineImageBoundaryAsBox(soiImage.getImage().getDimensions());
        this._stick3DFigureBoundary = _defineImageBoundaryAsBox(gaugeFigure.getImage().getDimensions());
        ;

    }

    /**
     * Define the image region as a box spanning from pixel zero to dim - 1;
     */
    private IShape _defineImageBoundaryAsBox(long[] imDimension) {
        long[] imageMax = imDimension.clone();
        for (int i = 0; i < imageMax.length; i++) {
            imageMax[i] -= 1;
        }

        return new ShapeFactory().closedBox(new long[imDimension.length], imageMax);
    }

    private IShape _defineBaseStick(int len, int thickness, long[] imDimension) {
        long[] stickMin = new long[imDimension.length];
        long[] stickMax = new long[imDimension.length];

        stickMin[0] = -thickness / 2 + 1;
        stickMin[1] = -thickness / 2 + 1;
        stickMin[2] = -len / 2 + 1;
        stickMax[0] = thickness / 2;
        stickMax[1] = thickness / 2;
        stickMax[2] = len / 2;

        return new ShapeFactory().closedBox(stickMin, stickMax);
    }

    @Override
    public void draw(IShape region, UINT16 soiThreshold) {
        if (region.spaceDim() > this._soiImageDim.length) {
            throw new IllegalArgumentException("The region to draw sticks over in the orientation image "
                    + "cannot have more dimensions than the orientation image.");
        }
        int threshold = soiThreshold.get();
        Pixel<RGB16> pixel = new Pixel<>(RGB16.zero());

        // If region has less dimension than the soi Image, scale its shape to higher
        // dimensions too.
        IShapeIterator regionScalarItr = ShapeUtils.scaleShapeOverHigherDim(region, this._soiImageDim);

        // For each pixel in the scaled region, if requirements are satisfied, draw the
        // the stick in the expanded image.
        while (regionScalarItr.hasNext()) {
            long[] dipolePosition = regionScalarItr.next();

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
            this._change3rdAnd4thStickPosition(stickPosition);
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
     * because the gauge figure has been interleaved in the z direction, we need to
     * draw the stick starting at z + stick_len / 2;
     * 
     * Regarding the rotations, note that based on the definition of the base stick
     * (it's parallel to the z axis), it eta = 90 and rho = 0, then the axis must be
     * parallel to the x axis. Hence, when the orientation angle is 0, we need to
     * apply a -PI/2 shift to the rho angle to ensure that the stick is represented
     * properly.
     * 
     * @param dipolePosition    is the dipole position in the orientation image.
     * @param orientationVector is the orientation of the dipole.
     */
    private void _transformStick(long[] dipolePosition, IOrientationVector orientationVector) {
        long[] stickTranslation = null;

        // In case the original image is planar, add the z translation, otherwise
        // use the z position in the soi image.
        if (dipolePosition.length < 3) {
            stickTranslation = new long[4];
            System.arraycopy(dipolePosition, 0, stickTranslation, 0, 2);
            stickTranslation[2] = this._zInterleaveFactor / 2 - 1;
        } else {
            stickTranslation = dipolePosition.clone();
            stickTranslation[2] = dipolePosition[3] * this._zInterleaveFactor + this._zInterleaveFactor / 2 - 1;
            stickTranslation[3] = dipolePosition[2];
        }

        this._stick.resetToOriginalShape();
        this._stick.rotate3D(orientationVector.getAngle(OrientationAngle.eta),
                -Math.PI / 2 + orientationVector.getAngle(OrientationAngle.rho), 0, new int[] { 0, 2, 1 });
        this._stick.translate(stickTranslation);
    }

    private IOrientationVector _getOrientationVector(long[] stickCenterPosition) {
        this._orientationRA.setPosition(stickCenterPosition);
        return this._orientationRA.getOrientation();
    }

    private boolean _isSoIAboveThreshold(int threshold) {
        return this._soiRA.getPixel().value().get() >= threshold;
    }

    /**
     * As we know, in standard bio-format images, the third dimension is channel and
     * fourth is z. However when we generate the sticks, we assume that the third
     * dimension is z (by convention, and so that we can apply affine transforms).
     * Hence, we need to exchange the third and forth dimension before placing it in
     * the gauge figure!
     * 
     * This is all because some idiot decided that the third dimension in a tiff
     * bio-image should be channel, and not z.
     */
    private void _change3rdAnd4thStickPosition(long[] stickPixel) {
        long thirdDim = stickPixel[2];
        stickPixel[2] = stickPixel[3];
        stickPixel[3] = thirdDim;
    }

    @Override
    public IGaugeFigure getStickFigure() {
        return _stick3DFigure;
    }

}