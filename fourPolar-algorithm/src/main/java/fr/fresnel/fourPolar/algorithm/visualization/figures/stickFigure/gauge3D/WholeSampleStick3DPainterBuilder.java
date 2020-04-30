package fr.fresnel.fourPolar.algorithm.visualization.figures.stickFigure.gauge3D;

import java.util.Objects;

import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.ConverterToImgLib2NotFound;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.image.orientation.IOrientationImage;
import fr.fresnel.fourPolar.core.image.polarization.soi.ISoIImage;
import fr.fresnel.fourPolar.core.physics.axis.AxisOrder;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;
import fr.fresnel.fourPolar.core.util.image.colorMap.ColorMap;
import fr.fresnel.fourPolar.core.util.image.colorMap.ColorMapFactory;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.GaugeFigureFactory;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.GaugeFigureType;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.IGaugeFigure;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.AngleGaugeType;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.IAngleGaugePainter;

/**
 * Using this class, we can create an {@link IAngleGaugePainter} that has 3D
 * sticks. A 3D sticks represents in plane angle (rho), and off-plane angle
 * (eta) as a 3D stick, where the stick color is the wobbling (delta). To
 * generate the gauge figure, the orientation figure is interleaved in the
 * z-dimension to accommodate for the stick length (interleave factor =
 * stick_length). The {@link GaugeFigureType} associated with this builder would
 * be WholeSample.
 * <p>
 * For the region provided for the painter built by this class, if a pixel of
 * the region is out of image dimension, no sticks are drawn. If the region's
 * space dimension is less than that of the orientation image, it's
 * automatically scaled to all higher dimensions. For example, the same 2D box
 * in region would be used for z = 0, 1, ... .
 */
public class WholeSampleStick3DPainterBuilder {
    private final IOrientationImage _orientationImage;
    private final ISoIImage _soiImage;

    private ColorMap _colorMap = ColorMapFactory.create(ColorMapFactory.IMAGEJ_SPECTRUM);
    private int _thickness = 4;
    private int _length = 50;

    private IGaugeFigure _gaugeFigure;

    /**
     * Initiate builder from orientation and soi images.
     * 
     * @param IllegalArgumentException is thrown in case orientation image is not at
     *                                 least two dimensions.
     */
    public WholeSampleStick3DPainterBuilder(IOrientationImage orientationImage, ISoIImage soiImage) {
        Objects.requireNonNull(soiImage, "soiImage cannot be null");
        Objects.requireNonNull(orientationImage, "orientationImage cannot be null");

        long[] orientationImageDim = orientationImage.getAngleImage(OrientationAngle.rho).getImage().getDimensions();
        if (orientationImageDim.length < 2) {
            throw new IllegalArgumentException("The orientation image must be at least two dimensionsal.");
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
        if (length < 1 || length % 2 == 1) {
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
        long[] orientationImageDim = this._orientationImage.getAngleImage(OrientationAngle.rho).getImage()
                .getDimensions();
        IMetadata orientImMetadata = this._orientationImage.getAngleImage(OrientationAngle.rho).getImage()
                .getMetadata();
        this._gaugeFigure = this._createGaugeFigure(orientationImageDim, orientImMetadata);
        return new WholeSampleStick3DPainter(this);
    }

    /**
     * To create the gauge figure, the gauge figure interleaves the orientation
     * image in the z-direction. For 2D orientation image, just add a z-axis,
     * otherwise, keep axis order of orientation image.
     */
    private IGaugeFigure _createGaugeFigure(long[] dim, IMetadata orientImMetadata) {
        AxisOrder orientImAxisOrder = orientImMetadata.axisOrder();

        IMetadata gaugeMetadata;
        long[] dimGaugeIm;
        if (AxisOrder.getZAxis(orientImAxisOrder) < 0) // If image has no z.
        {
            dimGaugeIm = new long[] { dim[0], dim[1], this._length };
            gaugeMetadata = new Metadata.MetadataBuilder(orientImMetadata).axisOrder(AxisOrder.XYZ).build();
        } else { 
            int zAxis = AxisOrder.getZAxis(orientImAxisOrder);
            dimGaugeIm = dim.clone();
            dimGaugeIm[zAxis] = dimGaugeIm[zAxis] * this._length;
            gaugeMetadata = new Metadata.MetadataBuilder(orientImMetadata).build();
        }

        Image<RGB16> gaugeImage = this._soiImage.getImage().getFactory().create(dimGaugeIm, RGB16.zero(), gaugeMetadata);
        return GaugeFigureFactory.create(GaugeFigureType.WholeSample, AngleGaugeType.Stick3D, gaugeImage,
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