package fr.fresnel.fourPolar.algorithm.visualization.figures.stickFigure.gauge2D;

import java.util.Objects;

import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.ConverterToImgLib2NotFound;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.image.orientation.IOrientationImage;
import fr.fresnel.fourPolar.core.image.polarization.soi.ISoIImage;
import fr.fresnel.fourPolar.core.util.image.colorMap.ColorMap;
import fr.fresnel.fourPolar.core.util.image.colorMap.ColorMapFactory;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.GaugeFigureFactory;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.GaugeFigureType;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.IGaugeFigure;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.AngleGaugeType;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.IAngleGaugePainter;

public class Stick2DPainterBuilder {
    private final IOrientationImage _orientationImage;
    private final ISoIImage _soiImage;
    private final AngleGaugeType _gaugeType;

    private ColorMap _colorMap = ColorMapFactory.create(ColorMapFactory.IMAGEJ_SPECTRUM);
    private int _thickness = 4;
    private int _length = 50;
    private GaugeFigureType _gaugeFigureType = GaugeFigureType.WholeSample;

    private IGaugeFigure _gaugeFigure;

    public Stick2DPainterBuilder(IOrientationImage orientationImage, ISoIImage soiImage, AngleGaugeType gaugeType) {
        Objects.requireNonNull(soiImage, "soiImage cannot be null");
        Objects.requireNonNull(orientationImage, "orientationImage cannot be null");
        Objects.requireNonNull(gaugeType, "gaugeType cannot be null");

        this._gaugeType = gaugeType;
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
    public Stick2DPainterBuilder colorMap(ColorMap colorMap) {
        Objects.requireNonNull(colorMap, "colorMap cannot be null;");
        return this;
    }

    /**
     * Define the thickness of each stick.
     */
    public Stick2DPainterBuilder stickThickness(int thickness) {
        if (thickness < 1) {
            throw new IllegalArgumentException("thickness must be at least one");
        }

        this._thickness = thickness;

        return this;
    }

    /**
     * Define the length of each stick.
     */
    public Stick2DPainterBuilder stickLen(int length) {
        if (length < 1) {
            throw new IllegalArgumentException("length must be at least one");
        }

        this._length = length;

        return this;
    }

    /**
     * Determines the gauge figure type. If SingleSample is chosen, the stick figure
     * would be the same size as a single stick, and using a Point Shape in the
     * {@link IAngleGaugePainter}, the orientation of single sticks can be viewed.
     */
    public Stick2DPainterBuilder gaugeFigureType(GaugeFigureType figureType) {
        Objects.requireNonNull(figureType, "figureType cannot be null");
        this._gaugeFigureType = figureType;

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
        IAngleGaugePainter painter = null;
        switch (this._gaugeFigureType) {
            case WholeSample:
                this._gaugeFigure = this._createGaugeFigure(this._soiImage.getImage().getDimensions());
                painter = new WholeSampleStick2DPainter(this);
                break;

            case SingleDipole:
                this._gaugeFigure = this._createGaugeFigure(new long[] { this._length, this._length });
                painter = new SingleDipoleStick2DPainter(this);

            default:
                break;
        }

        return painter;

    }

    private IGaugeFigure _createGaugeFigure(long[] dim) {
        Image<RGB16> gaugeImage = this._soiImage.getImage().getFactory().create(dim, RGB16.zero());
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