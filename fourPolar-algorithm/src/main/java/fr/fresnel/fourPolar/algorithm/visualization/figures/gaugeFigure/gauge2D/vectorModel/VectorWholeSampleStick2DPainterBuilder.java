package fr.fresnel.fourPolar.algorithm.visualization.figures.gaugeFigure.gauge2D.vectorModel;

import java.util.Objects;
import java.util.Optional;

import fr.fresnel.fourPolar.core.image.generic.pixel.types.ARGB8;
import fr.fresnel.fourPolar.core.image.orientation.IOrientationImage;
import fr.fresnel.fourPolar.core.image.soi.ISoIImage;
import fr.fresnel.fourPolar.core.image.vector.VectorImageFactory;
import fr.fresnel.fourPolar.core.image.vector.filter.FilterComposite;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;
import fr.fresnel.fourPolar.core.util.image.generic.colorMap.ColorMap;
import fr.fresnel.fourPolar.core.util.image.generic.colorMap.ColorMapFactory;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.IAngleGaugePainter;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.vectorFigure.VectorGaugeFigure;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.vectorFigure.animation.OrientationAnimationCreator;

/**
 * !!!Note that color blending happens inside the builder!!! So the filter has
 * to be put here. Also the
 */
public class VectorWholeSampleStick2DPainterBuilder extends IVectorWholeSampleStick2DPainterBuilder {
    private final VectorImageFactory _factory;

    private IOrientationImage _orientationImage;
    private ISoIImage _soiImage;

    private OrientationAnimationCreator _animationCreator;

    private VectorGaugeFigure _gaugeFigure = null;

    private ARGB8 _transparency = new ARGB8(0, 0, 0, 255);
    private ColorMap _colorMap = ColorMapFactory.create(ColorMapFactory.IMAGEJ_SPECTRUM);
    private int _thickness = 4;
    private int _length = 30;

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
    private FilterComposite _colorBlender = null;

    public VectorWholeSampleStick2DPainterBuilder(VectorImageFactory factory) {
        Objects.requireNonNull(factory, "factory can't be null.");
        _factory = factory;
    }

    /**
     * Set the colormap for the painter.
     * 
     * @param colorMap is the desired color map.
     */
    public VectorWholeSampleStick2DPainterBuilder colorMap(ColorMap colorMap) {
        Objects.requireNonNull(colorMap, "colorMap can't be null.");

        this._colorMap = colorMap;
        return this;
    }

    /**
     * Set the stick length for the painter.
     * 
     * @param len is the desired stick length.
     * @throws IllegalArgumentException in case length is less than one.
     */
    public VectorWholeSampleStick2DPainterBuilder sticklength(int len) {
        if (len < 1) {
            throw new IllegalArgumentException("len can't be less than one.");
        }
        this._length = len;
        return this;
    }

    /**
     * Set the stick thickness associated with this builder.
     * 
     * @param thickness is the thickness of the stick.
     * @throws IllegalArgumentException in case length is less than one.
     */
    public VectorWholeSampleStick2DPainterBuilder stickThickness(int thickness) {
        if (thickness < 1) {
            throw new IllegalArgumentException("thickness can't be less than one.");
        }

        this._thickness = thickness;
        return this;
    }

    /**
     * Set the color blender that will be used to mix colors when creating the
     * figure. This composite will be written at the top of the svg figure as a
     * definition, and will be used for each single stick to blend colors.
     * 
     * @param composite is the filter composite.
     */
    public VectorWholeSampleStick2DPainterBuilder colorBlender(FilterComposite colorBlender) {
        Objects.requireNonNull(colorBlender, "colorBlender can't be null.");

        _colorBlender = colorBlender;
        return this;
    }

    /**
     * Set the transparency of the sticks. This transparency is applied to each
     * stick of the figure, and if the color blender uses transparency, will be used
     * for that as well. The transparency is read from the ARGB8 color.
     * 
     * @param transparency contains the transparency as part of an argb color model.
     */
    public VectorWholeSampleStick2DPainterBuilder stickTransparency(ARGB8 transparency) {
        Objects.requireNonNull(transparency, "transparency can't be null.");

        _transparency = transparency;
        return this;
    }

    /**
     * Set the animation creater of the stick, which will be used to visualize an
     * angle with animation in the figure.
     * 
     * @param animationCreator is the animation creater.
     */
    public VectorWholeSampleStick2DPainterBuilder animationCreator(OrientationAnimationCreator animationCreator) {
        Objects.requireNonNull(animationCreator, "animationCreator can't be null.");

        _animationCreator = animationCreator;
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
     * Create the appropriate empty gauge figure for rho 2D.
     */
    private void _setGaugeFigureAsRho2D() {
        _gaugeFigure = VectorGaugeFigure.wholeSampleRho2DStick(_soiImage, _factory);
        _setGaugeFigureColorBlender();
    }

    /**
     * Create the appropriate empty gauge figure for delta 2D.
     */
    private void _setGaugeFigureAsDelta2D() {
        _gaugeFigure = VectorGaugeFigure.wholeSampleDelta2DStick(_soiImage, _factory);
        _setGaugeFigureColorBlender();
    }

    /**
     * Create the appropriate empty gauge figure for eta 2D.
     */
    private void _setGaugeFigureAsEta2D() {
        _gaugeFigure = VectorGaugeFigure.wholeSampleEta2DStick(_soiImage, _factory);
        _setGaugeFigureColorBlender();

    }

    private void _setGaugeFigureColorBlender() {
        if (_colorBlender != null) {
            _gaugeFigure.getVectorImage().addFilterComposite(_colorBlender);
        }
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

        _setSoIImage(soiImage);
        _setOrientationImage(orientationImage);
        _setSlopeAngle(OrientationAngle.rho);
        _setColorAngle(OrientationAngle.rho);
        _setGaugeFigureAsRho2D();

        return new VectorWholeSampleStick2DPainter(this);
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

        _setSoIImage(soiImage);
        _setOrientationImage(orientationImage);
        _setGaugeFigureAsDelta2D();
        _setSlopeAngle(OrientationAngle.rho);
        _setColorAngle(OrientationAngle.delta);

        return new VectorWholeSampleStick2DPainter(this);
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

        _setSoIImage(soiImage);
        _setOrientationImage(orientationImage);
        _setGaugeFigureAsEta2D();
        _setSlopeAngle(OrientationAngle.rho);
        _setColorAngle(OrientationAngle.eta);

        return new VectorWholeSampleStick2DPainter(this);
    }

    private void _checkSoIAndOrientationImageBelongToSameSet(IOrientationImage orientationImage, ISoIImage soiImage) {
        if (!soiImage.belongsTo(orientationImage)) {
            throw new IllegalArgumentException("orientation and soi images don't belong to the same set or channel.");
        }
    }

    @Override
    ColorMap getColorMap() {
        return this._colorMap;
    }

    @Override
    int getSticklength() {
        return this._length;
    }

    @Override
    IOrientationImage getOrientationImage() {
        return this._orientationImage;
    }

    @Override
    ISoIImage getSoIImage() {
        return this._soiImage;
    }

    @Override
    int getStickThickness() {
        return this._thickness;
    }

    @Override
    VectorGaugeFigure getGauageFigure() {
        return this._gaugeFigure;
    }

    @Override
    Optional<FilterComposite> getColorBlender() {
        return Optional.ofNullable(_colorBlender);
    }

    @Override
    ARGB8 getStickTransparency() {
        return this._transparency;
    }

    @Override
    OrientationAngle getSlopeAngle() {
        return this._slopeAngle;
    }

    @Override
    OrientationAngle getColorAngle() {
        return this._colorAngle;
    }

    @Override
    Optional<OrientationAnimationCreator> getAnimationCreator() {
        return Optional.ofNullable(_animationCreator);
    }

}