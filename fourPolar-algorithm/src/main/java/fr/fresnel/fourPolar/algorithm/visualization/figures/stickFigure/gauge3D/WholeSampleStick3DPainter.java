package fr.fresnel.fourPolar.algorithm.visualization.figures.stickFigure.gauge3D;

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
 * Class that fills the proper gauge with 3D sticks. See
 * {@link WholeSampleStick3DPainterBuilder}.
 */
class WholeSampleStick3DPainter implements IAngleGaugePainter {
    final private long[] _soiImageDim;
    final private IGaugeFigure _stick3DFigure;
    final private IPixelRandomAccess<RGB16> _stick3DFigureRA;
    final private IOrientationImageRandomAccess _orientationRA;
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
     * 
     */
    public WholeSampleStick3DPainter(WholeSampleStick3DPainterBuilder builder) {
        this._soiImageDim = builder.getSoIImage().getImage().getDimensions();
        this._soiRA = builder.getSoIImage().getImage().getRandomAccess();

        this._stick3DFigure = builder.getGaugeFigure();
        this._stick3DFigureRA = this._stick3DFigure.getImage().getRandomAccess();

        this._orientationRA = builder.getOrientationImage().getRandomAccess();
        this._colormap = builder.getColorMap();
        this._stickLength = builder.getSticklength();

        this._stick = _defineBaseStick(this._stickLength, builder.getStickThickness(),
                this._stick3DFigure.getImage().getDimensions());

        this._soiImageBoundary = _defineImageBoundaryAsBox(this._soiImageDim);
        this._stick3DFigureBoundary = _defineImageBoundaryAsBox(this._stick3DFigure.getImage().getDimensions());
    }

    /**
     * Define the image region as a box spanning from pixel zero to dim - 1;
     */
    private IShape _defineImageBoundaryAsBox(long[] imDimension) {
        long[] imageMax = imDimension.clone();
        long[] imageMin = new long[imDimension.length];

        for (int i = 0; i < imageMax.length; i++) {
            imageMax[i] -= 1;
        }

        return new ShapeFactory().closedBox(imageMin, imageMax);
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
            stickTranslation[2] = this._stickLength / 2 - 1;
        } else {
            stickTranslation = dipolePosition.clone();
            stickTranslation[2] = dipolePosition[3] * this._stickLength + this._stickLength / 2 - 1;
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