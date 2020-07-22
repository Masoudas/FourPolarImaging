package fr.fresnel.fourPolar.algorithm.visualization.figures.gaugeFigure.gauge2D;

import java.util.Objects;

import fr.fresnel.fourPolar.core.image.generic.pixel.types.color.ColorBlender;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.color.SoftLightColorBlender;
import fr.fresnel.fourPolar.core.image.orientation.IOrientationImage;
import fr.fresnel.fourPolar.core.image.soi.ISoIImage;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;
import fr.fresnel.fourPolar.core.util.image.colorMap.ColorMap;
import fr.fresnel.fourPolar.core.util.image.colorMap.ColorMapFactory;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.GaugeFigure;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.GaugeFigureLocalization;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.IGaugeFigure;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.IAngleGaugePainter;

/**
 * Using this class, we can create an {@link IAngleGaugePainter} that is used to
 * depict 2D stick angle gauge over the entire orientation image. The user may
 * provide one or several regions over which to draw the sticks, but the output
 * {@link IGaugeFigure} will have the same size as the orientation (or soi)
 * image. The {@link GaugeFigureLocalization} associated with this builder would
 * be WholeSample. Note finally that the background of the gauge figure will be
 * filled with {@link ISoIImage}.
 * <p>
 * For the region provided for the painter built by this class, if a pixel of
 * the region is out of image dimension, no sticks are drawn. If the region's
 * space dimension is less than that of the orientation image, it's
 * automatically scaled to all higher dimensions. For example, the same 2D box
 * in region would be used for z = 0, 1, ... .
 * <p>
 */
public class WholeSampleStick2DPainterBuilder extends IWholeSampleStick2DPainterBuilder {
    private IOrientationImage _orientationImage;
    private ISoIImage _soiImage;

    private GaugeFigure _gaugeFigure = null;
    private ColorMap _colorMap = ColorMapFactory.create(ColorMapFactory.IMAGEJ_SPECTRUM);
    private int _thickness = 4;
    private int _length = 50;

    /**
     * The orientation angle that would be represented as slope of sticks,
     */
    private OrientationAngle _slopeAngle = null;

    /**
     * The orientation angle that would be used as the color of sticks.
     */
    private OrientationAngle _colorAngle = null;

    /**
     * Color blender that is used for mixing overlapping pixel colors.
     */
    private ColorBlender _colorBlender = new SoftLightColorBlender();

    public WholeSampleStick2DPainterBuilder() {
    }

    private void _checkSoIAndOrientationImageBelongToSameSet(IOrientationImage orientationImage, ISoIImage soiImage) {
        if (!soiImage.belongsTo(orientationImage)) {
            throw new IllegalArgumentException("orientation and soi images don't belong to the same set or channel.");
        }
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
    public WholeSampleStick2DPainterBuilder colorMap(ColorMap colorMap) {
        Objects.requireNonNull(colorMap, "colorMap cannot be null.");
        return this;
    }

    /**
     * Define the thickness of each stick.
     */
    public WholeSampleStick2DPainterBuilder stickThickness(int thickness) {
        if (thickness < 1) {
            throw new IllegalArgumentException("thickness must be at least one");
        }

        this._thickness = thickness;
        return this;
    }

    /**
     * Define the length of each stick.
     */
    public WholeSampleStick2DPainterBuilder stickLen(int length) {
        if (length < 1) {
            throw new IllegalArgumentException("length must be at least one");
        }

        this._length = length;
        return this;
    }

    /**
     * Color blender that is used for mixing overlapping pixel colors.
     */
    public WholeSampleStick2DPainterBuilder colorBlender(ColorBlender blender) {
        Objects.requireNonNull(blender, "blender can't be null.");

        this._colorBlender = blender;
        return this;
    }

    private void _setColorAngle(OrientationAngle colorAngle) {
        _colorAngle = colorAngle;
    }

    private void _setSlopeAngle(OrientationAngle slopeAngle) {
        _slopeAngle = slopeAngle;
    }

    private void _setSoIImage(ISoIImage soiImage) {
        this._soiImage = soiImage;
    }

    private void _setOrientationImage(IOrientationImage orientationImage) {
        this._orientationImage = orientationImage;
    }

    /**
     * Create the appropriate empty gauge figure.
     */
    private void _setGaugeFigureAsRho2D() {
        _gaugeFigure = GaugeFigure.wholeSampleRho2DStick(_soiImage);
    }

    /**
     * Create the appropriate empty gauge figure.
     */
    private void _setGaugeFigureAsDelta2D() {
        _gaugeFigure = GaugeFigure.wholeSampleDelta2DStick(_soiImage);
    }

    /**
     * Create the appropriate empty gauge figure.
     */
    private void _setGaugeFigureAsEta2D() {
        _gaugeFigure = GaugeFigure.wholeSampleEta2DStick(_soiImage);
    }

    /**
     * Build the painter from the provided constraints for drawing rho 2D sticks.
     * 
     * @param orientationImage is the orientation image
     * @param soiImage         is the corresponding soi Image
     * @return a painter for drawing the rho 2D sticks.
     * 
     * @throws IllegalArgumentException is thrown in case soi and orientation image
     *                                  don't belong together.
     */
    public IAngleGaugePainter buildRhoStickPainter(IOrientationImage orientationImage, ISoIImage soiImage) {
        Objects.requireNonNull(soiImage, "soiImage cannot be null");
        Objects.requireNonNull(orientationImage, "orientationImage cannot be null");

        _checkSoIAndOrientationImageBelongToSameSet(orientationImage, soiImage);

        _setSlopeAngle(OrientationAngle.rho);
        _setColorAngle(OrientationAngle.rho);
        _setGaugeFigureAsRho2D();
        _setSoIImage(soiImage);
        _setOrientationImage(orientationImage);

        return new WholeSampleStick2DPainter(this);
    }

    /**
     * Build the painter from the provided constraints for drawing delta 2D sticks.
     * 
     * @param orientationImage is the orientation image
     * @param soiImage         is the corresponding soi Image of
     * 
     * @return a painter for drawing the delta 2D sticks.
     * 
     * @throws IllegalArgumentException is thrown in case soi and orientation image
     *                                  don't belong together.
     */
    public IAngleGaugePainter buildDeltaStickPainter(IOrientationImage orientationImage, ISoIImage soiImage) {
        Objects.requireNonNull(soiImage, "soiImage cannot be null");
        Objects.requireNonNull(orientationImage, "orientationImage cannot be null");

        _checkSoIAndOrientationImageBelongToSameSet(orientationImage, soiImage);

        _setSlopeAngle(OrientationAngle.rho);
        _setColorAngle(OrientationAngle.delta);
        _setGaugeFigureAsDelta2D();
        _setSoIImage(soiImage);
        _setOrientationImage(orientationImage);

        return new WholeSampleStick2DPainter(this);
    }

    /**
     * Build the painter from the provided constraints for drawing eta 2D sticks.
     * 
     * @param orientationImage is the orientation image
     * @param soiImage         is the corresponding soi Image of
     * 
     * @return a painter for drawing the eta 2D sticks.
     * 
     * @throws IllegalArgumentException is thrown in case soi and orientation image
     *                                  don't belong together.
     */
    public IAngleGaugePainter buildEtaStickPainter(IOrientationImage orientationImage, ISoIImage soiImage) {
        Objects.requireNonNull(soiImage, "soiImage cannot be null");
        Objects.requireNonNull(orientationImage, "orientationImage cannot be null");

        _checkSoIAndOrientationImageBelongToSameSet(orientationImage, soiImage);

        _setSlopeAngle(OrientationAngle.rho);
        _setColorAngle(OrientationAngle.eta);
        _setGaugeFigureAsEta2D();
        _setSoIImage(soiImage);
        _setOrientationImage(orientationImage);

        return new WholeSampleStick2DPainter(this);
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
        return _colorBlender;
    }

    @Override
    IGaugeFigure getGauageFigure() {
        return this._gaugeFigure;
    }

    @Override
    OrientationAngle getSlopeAngle() {
        return this._slopeAngle;
    }

    @Override
    OrientationAngle getColorAngle() {
        return this._colorAngle;
    }

}