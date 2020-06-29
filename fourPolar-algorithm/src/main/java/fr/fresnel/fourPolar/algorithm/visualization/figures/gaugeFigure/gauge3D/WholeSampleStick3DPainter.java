package fr.fresnel.fourPolar.algorithm.visualization.figures.gaugeFigure.gauge3D;

import java.util.Arrays;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.IPixelRandomAccess;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.IPixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.Pixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.orientation.IAngleImage;
import fr.fresnel.fourPolar.core.image.orientation.IOrientationImageRandomAccess;
import fr.fresnel.fourPolar.core.image.soi.ISoIImage;
import fr.fresnel.fourPolar.core.physics.dipole.IOrientationVector;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationVector;
import fr.fresnel.fourPolar.core.util.image.colorMap.ColorMap;
import fr.fresnel.fourPolar.core.util.shape.IShape;
import fr.fresnel.fourPolar.core.util.shape.IShapeIterator;
import fr.fresnel.fourPolar.core.util.shape.Rotation3DOrder;
import fr.fresnel.fourPolar.core.util.shape.ShapeFactory;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.GaugeFigureFactory;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.GaugeFigureType;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.IGaugeFigure;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.AngleGaugeType;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.IAngleGaugePainter;

/**
 * Class that fills the proper gauge with 3D sticks. See
 * {@link WholeSampleStick3DPainterBuilder}.
 */
class WholeSampleStick3DPainter implements IAngleGaugePainter {
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
    final private IShape _baseStick;

    /**
     * Create the 3D painter using the builder parameters.
     */
    public WholeSampleStick3DPainter(IWholeSampleStick3DPainterBuilder builder) {
        this._soiRA = builder.getSoIImage().getImage().getRandomAccess();
        this._soiImageBoundary = _defineImageBoundaryAsBox(builder.getSoIImage().getImage());

        this._stick3DFigure = this._createGaugeFigure(builder.getSoIImage(), builder.getSticklength());
        this._stick3DFigureRA = this._stick3DFigure.getImage().getRandomAccess();
        this._stick3DFigureBoundary = _defineImageBoundaryAsBox(this._stick3DFigure.getImage());

        this._orientationRA = builder.getOrientationImage().getRandomAccess();

        this._colormap = builder.getColorMap();
        this._stickLength = builder.getSticklength();

        this._baseStick = _defineBaseStick(this._stickLength, builder.getStickThickness(),
                this._stick3DFigure.getImage().getMetadata().axisOrder());
    }

    /**
     * The gauge figure is interleaved in the z-direction by the length of sticks.
     */
    private IGaugeFigure _createGaugeFigure(ISoIImage soiImage, int stickLength) {
        int channel = soiImage.channel();

        IMetadata soiMetadata = soiImage.getImage().getMetadata();
        long[] soiImgDim = soiMetadata.getDim();

        long[] dimGaugeIm = { soiImgDim[0], soiImgDim[1], 1, soiImgDim[3] * stickLength, soiImgDim[4] };
        IMetadata gaugeMetadata = new Metadata.MetadataBuilder(dimGaugeIm).axisOrder(IGaugeFigure.AXIS_ORDER).build();

        Image<RGB16> gaugeImage = soiImage.getImage().getFactory().create(gaugeMetadata, RGB16.zero());
        return GaugeFigureFactory.create(GaugeFigureType.WholeSample, AngleGaugeType.Stick3D, gaugeImage,
                soiImage.getFileSet(), channel);
    }

    /**
     * Define the image region as a box spanning from pixel zero to dim - 1;
     */
    private IShape _defineImageBoundaryAsBox(Image<?> image) {
        long[] imageMax = Arrays.stream(image.getMetadata().getDim()).map((t) -> t - 1).toArray();
        long[] imageMin = new long[image.getMetadata().getDim().length];

        return new ShapeFactory().closedBox(imageMin, imageMax, image.getMetadata().axisOrder());
    }

    /**
     * The base stick is an XYCZT rectangle.
     */
    private IShape _defineBaseStick(int len, int thickness, AxisOrder axisOrder) {
        long[] stickMin = new long[IGaugeFigure.AXIS_ORDER.numAxis];
        long[] stickMax = new long[IGaugeFigure.AXIS_ORDER.numAxis];

        stickMin[0] = -thickness / 2 + 1;
        stickMin[1] = -thickness / 2 + 1;
        stickMin[IGaugeFigure.AXIS_ORDER.z_axis] = -len / 2 + 1;
        stickMin[IGaugeFigure.AXIS_ORDER.t_axis] = 0;
        stickMin[IGaugeFigure.AXIS_ORDER.c_axis] = 0;

        stickMax[0] = thickness / 2;
        stickMax[1] = thickness / 2;
        stickMax[IGaugeFigure.AXIS_ORDER.z_axis] = len / 2;
        stickMax[IGaugeFigure.AXIS_ORDER.t_axis] = 0;
        stickMax[IGaugeFigure.AXIS_ORDER.c_axis] = 0;

        return new ShapeFactory().closedBox(stickMin, stickMax, axisOrder);
    }

    @Override
    public void draw(IShape region, UINT16 soiThreshold) {
        if (region.axisOrder() != ISoIImage.AXIS_ORDER) {
            throw new IllegalArgumentException("The region should have the same axis order as the soi image.");
        }

        for (IShapeIterator iterator = region.getIterator(); iterator.hasNext();) {
            long[] dipolePosition = iterator.next();

            if (_soiImageBoundary.isInside(dipolePosition)) {
                this._soiRA.setPosition(dipolePosition);
                final IOrientationVector orientationVector = this._getOrientationVector(dipolePosition);

                if (_isSoIAboveThreshold(soiThreshold.get()) && orientationVector.isWellDefined()) {
                    _drawStick(orientationVector, dipolePosition);
                }
            }

        }
    }

    private void _drawStick(IOrientationVector orientationVector, long[] dipolePosition) {
        final RGB16 color = _getStickColor(orientationVector);

        IShapeIterator stickIterator = _createStickIteratorForThisDipole(orientationVector, dipolePosition);
        while (stickIterator.hasNext()) {
            long[] stickPosition = stickIterator.next();
            if (this._stick3DFigureBoundary.isInside(stickPosition)) {
                this._stick3DFigureRA.setPosition(stickPosition);
                
                IPixel<RGB16> stickPositionPixel = this._stick3DFigureRA.getPixel();
                stickPositionPixel.value().add(color);
                
                this._stick3DFigureRA.setPixel(stickPositionPixel);
            }
        }
    }

    private IShapeIterator _createStickIteratorForThisDipole(IOrientationVector orientationVector,
            long[] dipolePosition) {
        IShape transformedShape = _transformStick(dipolePosition, orientationVector);
        IShapeIterator stickIterator = transformedShape.getIterator();
        return stickIterator;
    }

    private RGB16 _getStickColor(IOrientationVector orientationVector) {
        return this._colormap.getColor(0, OrientationVector.maxAngle(OrientationAngle.delta),
                orientationVector.getAngle(OrientationAngle.delta));
    }

    /**
     * To transform the base stick, suppose the dipole is located at x, y, c, z, t.
     * Then because the gauge figure has been interleaved in the z direction, the
     * dipole position would be in z + stick_len / 2, with stick stetching
     * (possibly) from z to z + stick_len.
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
    private IShape _transformStick(long[] dipolePosition, IOrientationVector orientationVector) {
        int z_axis = IAngleImage.AXIS_ORDER.z_axis;
        int t_axis = IAngleImage.AXIS_ORDER.t_axis;
        long[] stickTranslation = { dipolePosition[0], dipolePosition[1], 0,
                dipolePosition[z_axis] * this._stickLength + this._stickLength / 2 - 1, dipolePosition[t_axis] };

        IShape transformedShape = this._baseStick.rotate3D(orientationVector.getAngle(OrientationAngle.eta),
                -Math.PI / 2 + orientationVector.getAngle(OrientationAngle.rho), 0, Rotation3DOrder.XZY);
        return transformedShape.translate(stickTranslation);
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