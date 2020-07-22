package fr.fresnel.fourPolar.algorithm.visualization.figures.gaugeFigure.gauge3D;

import java.util.Objects;

import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.ConverterToImgLib2NotFound;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.color.ColorBlender;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.color.SoftLightColorBlender;
import fr.fresnel.fourPolar.core.image.orientation.IOrientationImage;
import fr.fresnel.fourPolar.core.image.soi.ISoIImage;
import fr.fresnel.fourPolar.core.util.image.colorMap.ColorMap;
import fr.fresnel.fourPolar.core.util.image.colorMap.ColorMapFactory;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.GaugeFigure;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.GaugeFigureLocalization;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.IAngleGaugePainter;

/**
 * Using this class, we can create an {@link IAngleGaugePainter} that has 3D
 * sticks. A 3D sticks represents in plane angle (rho), and off-plane angle
 * (eta) as a 3D stick, where the stick color is the wobbling (delta). To
 * generate the gauge figure, the orientation figure is interleaved in the
 * z-dimension to accommodate for the stick length (interleave factor =
 * stick_length). The {@link GaugeFigureLocalization} associated with this
 * builder would be WholeSample.
 * <p>
 * For the region provided for the painter built by this class, if a pixel of
 * the region is out of image dimension, no sticks are drawn. If the region's
 * space dimension is less than that of the orientation image, it's
 * automatically scaled to all higher dimensions. For example, the same 2D box
 * in region would be used for z = 0, 1, ... .
 * <p>
 */
public class WholeSampleStick3DPainterBuilder extends IWholeSampleStick3DPainterBuilder {
    private GaugeFigure _gaugeFigure;
    private IOrientationImage _orientationImage;
    private ISoIImage _soiImage;

    private ColorMap _colorMap = ColorMapFactory.create(ColorMapFactory.IMAGEJ_SPECTRUM);
    private int _thickness = 4;
    private int _length = 50;

    /**
     * Color blender that is used for mixing overlapping pixel colors.
     */
    private ColorBlender _colorBlender = new SoftLightColorBlender();

    /**
     * Initialize the painter with the given orientation and soi image, for the
     * given angle gauge type.
     * 
     * @param orientationImage         is the orientation image
     * @param soiImage                 is the corresponding soi Image of @param
     *                                 orientationImage.
     * @param IllegalArgumentException is thrown in case soi and orientation image
     *                                 are not from the same set, or that soi or
     *                                 orientation image have channels.
     */
    public WholeSampleStick3DPainterBuilder(IOrientationImage orientationImage, ISoIImage soiImage) {
        Objects.requireNonNull(soiImage, "soiImage cannot be null");
        Objects.requireNonNull(orientationImage, "orientationImage cannot be null");

        if (!orientationImage.getCapturedSet().getSetName().equals(soiImage.getFileSet().getSetName())
                || orientationImage.channel() != soiImage.channel()) {
            throw new IllegalArgumentException("orientation and soi images don't belong to the same set or channel.");
        }

        this._soiImage = soiImage;
        this._orientationImage = orientationImage;
    }

    /**
     * Define the colormap used for drawing the sticks. Note that two criteria
     * should be satisfied when choosing colormap:
     * 
     * 1- It must not have black or white colors, otherwise, it will be
     * misinterpreted as intensity (because the background is an SoI image).
     * 
     * 2- For Rho2D sticks, the colormap must wrap to the same color at both ends of
     * the spectrum, so that 0 and 180 degree have the same color.
     */
    public WholeSampleStick3DPainterBuilder colorMap(ColorMap colorMap) {
        Objects.requireNonNull(colorMap, "colorMap cannot be null;");
        return this;
    }

    /**
     * Define the thickness of each stick.
     */
    public WholeSampleStick3DPainterBuilder stickThickness(int thickness) {
        if (thickness < 1) {
            throw new IllegalArgumentException("thickness must be at least one");
        }

        this._thickness = thickness;
        return this;
    }

    /**
     * Define the length of each stick.
     */
    public WholeSampleStick3DPainterBuilder stickLen(int length) {
        if (length < 1) {
            throw new IllegalArgumentException("length must be at least one");
        }

        this._length = length;
        return this;
    }

    /**
     * Color blender that is used for mixing overlapping pixel colors.
     */
    public WholeSampleStick3DPainterBuilder colorBlender(ColorBlender blender) {
        Objects.requireNonNull(blender, "blender can't be null.");

        this._colorBlender = blender;
        return this;
    }

    /**
     * Create the appropriate empty gauge figure.
     */
    private void _setGaugeFigureAs3DStickFigure(ISoIImage soiImage) {
        _gaugeFigure = GaugeFigure.wholeSample3DStick(soiImage, this._length);
    }

    private void _checkSoIAndOrientationImageBelongToSameSet(IOrientationImage orientationImage, ISoIImage soiImage) {
        if (!soiImage.belongsTo(orientationImage)) {
            throw new IllegalArgumentException("orientation and soi images don't belong to the same set or channel.");
        }
    }

    /**
     * Build the Painter from the provided constraints.
     * 
     * @return the interface for the painter of sticks.
     */
    public IAngleGaugePainter build(ISoIImage soiImage, IOrientationImage orientationImage) {    
        Objects.requireNonNull(soiImage, "soiImage cannot be null");
        Objects.requireNonNull(orientationImage, "orientationImage cannot be null");

        _checkSoIAndOrientationImageBelongToSameSet(orientationImage, soiImage);
        _setGaugeFigureAs3DStickFigure(soiImage);

        return new WholeSampleStick3DPainter(this);
    }

    @Override
    ColorMap getColorMap() {
        return this._colorMap;
    }

    @Override
    int getSticklength() {
        return _length;
    }

    @Override
    IOrientationImage getOrientationImage() {
        return _orientationImage;
    }

    @Override
    ISoIImage getSoIImage() {
        return _soiImage;
    }

    @Override
    int getStickThickness() {
        return _thickness;
    }

    @Override
    ColorBlender getColorBlender() {
        return this._colorBlender;
    }

    @Override
    GaugeFigure getGaugeFigure() {
        return this._gaugeFigure;
    }

}