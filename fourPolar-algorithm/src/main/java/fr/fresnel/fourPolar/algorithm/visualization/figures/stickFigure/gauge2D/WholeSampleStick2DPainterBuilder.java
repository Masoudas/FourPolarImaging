package fr.fresnel.fourPolar.algorithm.visualization.figures.stickFigure.gauge2D;

import java.util.Objects;

import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.ConverterToImgLib2NotFound;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.image.orientation.IOrientationImage;
import fr.fresnel.fourPolar.core.image.polarization.soi.ISoIImage;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;
import fr.fresnel.fourPolar.core.util.image.colorMap.ColorMap;
import fr.fresnel.fourPolar.core.util.image.colorMap.ColorMapFactory;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.GaugeFigureFactory;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.GaugeFigureType;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.IGaugeFigure;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.AngleGaugeType;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.IAngleGaugePainter;

/**
 * Using this class, we can create an {@link IAngleGaugePainter} that is used to
 * depict 2D stick angle gauge over the entire orientation image. The user may
 * provide one or several regions over which to draw the sticks, but the output
 * {@link IGaugeFigure} will have the same size as the orientation (or soi)
 * image. The {@link GaugeFigureType} associated with this builder would be
 * WholeSample. Note finally that the background of the gauge figure will be
 * filled with {@link ISoIImage}.
 * <p>
 * For the region provided for the painter built by this class, if a pixel of
 * the region is out of image dimension, no sticks are drawn. If the region's
 * space dimension is less than that of the orientation image, it's
 * automatically scaled to all higher dimensions. For example, the same 2D box
 * in region would be used for z = 0, 1, ... .
 */
public class WholeSampleStick2DPainterBuilder {
    private final IOrientationImage _orientationImage;
    private final ISoIImage _soiImage;
    private final AngleGaugeType _gaugeType;

    private ColorMap _colorMap = ColorMapFactory.create(ColorMapFactory.IMAGEJ_SPECTRUM);
    private int _thickness = 4;
    private int _length = 50;
    private GaugeFigureType _gaugeFigureType = GaugeFigureType.WholeSample;

    private IGaugeFigure _gaugeFigure;

    /**
     * Initialize the painter with the given orientation and soi image, for the
     * given angle gauge tpe.
     * 
     * @param orientationImage is the orientation image
     * @param soiImage         is the corresponding soi Image of @param
     *                         orientationImage.
     * @param angleGaugeType   is the angle gauge type to be painted.
     */
    public WholeSampleStick2DPainterBuilder(IOrientationImage orientationImage, ISoIImage soiImage,
            AngleGaugeType angleGaugeType) {
        Objects.requireNonNull(soiImage, "soiImage cannot be null");
        Objects.requireNonNull(orientationImage, "orientationImage cannot be null");
        Objects.requireNonNull(angleGaugeType, "gaugeType cannot be null");

        long[] orientationImageDim = orientationImage.getAngleImage(OrientationAngle.rho).getImage().getDimensions();
        if (orientationImageDim.length < 2) {
            throw new IllegalArgumentException("The orientation image must be at least two dimensionsal.");
        }

        this._gaugeType = angleGaugeType;
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
    public WholeSampleStick2DPainterBuilder colorMap(ColorMap colorMap) {
        Objects.requireNonNull(colorMap, "colorMap cannot be null;");
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
     * Build the Painter from the provided constraints.
     * 
     * @return the interface for the painter of sticks.
     * @throws ConverterToImgLib2NotFound in case the Image interface of SoIImage
     *                                    cannot be converted to ImgLib2 image type.
     */
    public IAngleGaugePainter build() throws ConverterToImgLib2NotFound {
        IMetadata orientImMetadata = this._orientationImage.getAngleImage(OrientationAngle.rho).getImage()
                .getMetadata();
        this._gaugeFigure = this._createGaugeFigure(this._soiImage.getImage().getDimensions(), orientImMetadata);
        return new WholeSampleStick2DPainter(this);
    }

    private IGaugeFigure _createGaugeFigure(long[] dim, IMetadata orientationImgMetadata) {
        IMetadata metadata = new Metadata.MetadataBuilder(orientationImgMetadata).build();
        Image<RGB16> gaugeImage = this._soiImage.getImage().getFactory().create(dim, RGB16.zero(), metadata);
        return GaugeFigureFactory.create(this._gaugeFigureType, this._gaugeType, gaugeImage,
                this._soiImage.getFileSet());
    }

    public ColorMap getColorMap() {
        return this._colorMap;
    }

    public IGaugeFigure getGaugeFigure() {
        return _gaugeFigure;
    }

    public int getSticklength() {
        return _length;
    }

    public IOrientationImage getOrientationImage() {
        return _orientationImage;
    }

    public ISoIImage getSoIImage() {
        return _soiImage;
    }

    public int getStickThickness() {
        return _thickness;
    }

}