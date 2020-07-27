package fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.vectorFigure;

import java.util.Objects;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelTypes;
import fr.fresnel.fourPolar.core.image.soi.ISoIImage;
import fr.fresnel.fourPolar.core.image.vector.VectorImage;
import fr.fresnel.fourPolar.core.image.vector.VectorImageFactory;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.GaugeFigureLocalization;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.IGaugeFigure;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.AngleGaugeType;

/**
 * A concrete implementation of {@link IGaugeFigure} that uses
 * {@link VectorImage} as its backend.
 * <p>
 * Note that regardless of the form of gauge figure, the underlying image is
 * always equivalent to {@link ISoIImage#AXIS_ORDER}.
 */
public class VectorGaugeFigure implements IGaugeFigure {
    private final VectorImage _image;
    private final AngleGaugeType _type;
    private final ICapturedImageFileSet _fileSet;
    private final GaugeFigureLocalization _figureType;
    private final int _channel;

    /**
     * Creates a figure instance for representing {@link AngleGaugeType#Rho2D}.
     * 
     * @param soiImage is the soi image for which we want to create this gauge
     *                 figure.
     * @param factor   is the factory for creating the vector image.
     * @return an empty gauge figure.
     */
    public static VectorGaugeFigure wholeSampleRho2DStick(ISoIImage soiImage, VectorImageFactory factory) {
        return _createWholeSample2DStickFigure(soiImage, AngleGaugeType.Rho2D, factory);
    }

    /**
     * Creates a figure instance for representing {@link AngleGaugeType#Delta2D}.
     * 
     * @param soiImage is the soi image for which we want to create this gauge
     *                 figure.
     * @param factor   is the factory for creating the vector image.
     * @return an empty gauge figure.
     */
    public static VectorGaugeFigure wholeSampleDelta2DStick(ISoIImage soiImage, VectorImageFactory factory) {
        return _createWholeSample2DStickFigure(soiImage, AngleGaugeType.Delta2D, factory);
    }

    /**
     * Creates a figure instance for representing {@link AngleGaugeType#Eta2D}.
     * 
     * @param soiImage is the soi image for which we want to create this gauge
     *                 figure.
     * @param factor   is the factory for creating the vector image.
     * @return an empty gauge figure.
     */
    public static VectorGaugeFigure wholeSampleEta2DStick(ISoIImage soiImage, VectorImageFactory factory) {
        return _createWholeSample2DStickFigure(soiImage, AngleGaugeType.Eta2D, factory);
    }

    /**
     * Create a vector gauge figure of same size as soi with the given gauge.
     */
    private static VectorGaugeFigure _createWholeSample2DStickFigure(ISoIImage soiImage, AngleGaugeType gaugeType,
            VectorImageFactory factory) {
        Objects.requireNonNull(soiImage, "soiImage can't be null");
        Objects.requireNonNull(gaugeType, "gaugeType can't be null");

        IMetadata fig_metadata = new Metadata.MetadataBuilder(soiImage.getImage().getMetadata().getDim())
                .axisOrder(AXIS_ORDER).bitPerPixel(PixelTypes.ARGB_8).build();
        VectorImage figure = factory.create(fig_metadata);

        return new VectorGaugeFigure(GaugeFigureLocalization.WHOLE_SAMPLE, gaugeType, figure, soiImage.getFileSet(),
                soiImage.channel());
    }

    /**
     * Create an stick figure using the provided vector image.
     * 
     * @param localization   is the localization level of this figure
     * @param angleGaugeType is the type of gauge.
     * @param image          is the vector image instance.
     * @param fileSet        is the file set this figure is associated with.
     * @param channel        is the channel this figure is associated with.
     * 
     * @throws IllegalArgumentException if vector image axis order is not equal to
     *                                  IGaugeFigure default order, or if channel is
     *                                  less than one.
     */
    public static VectorGaugeFigure create(GaugeFigureLocalization localization, AngleGaugeType angleGaugeType,
            VectorImage image, ICapturedImageFileSet fileSet, int channel) {
        if (image.metadata().axisOrder() != AXIS_ORDER) {
            throw new IllegalArgumentException(
                    "The given image does not have the same axis-order as default gauge figure.");
        }

        return new VectorGaugeFigure(localization, angleGaugeType, image, fileSet, channel);
    }

    /**
     * Create an stick figure using the provided vector image.
     * 
     * @param localization   is the localization level of this figure
     * @param angleGaugeType is the type of gauge.
     * @param image          is the vector image instance.
     * @param fileSet        is the file set this figure is associated with.
     * @param channel        is the channel this figure is associated with.
     */
    private VectorGaugeFigure(GaugeFigureLocalization localization, AngleGaugeType angleGaugeType, VectorImage image,
            ICapturedImageFileSet fileSet, int channel) {
        this._image = image;
        this._type = angleGaugeType;
        this._fileSet = fileSet;
        this._figureType = localization;
        this._channel = channel;
    }

    @Override
    public AngleGaugeType getGaugeType() {
        return _type;
    }

    public VectorImage getVectorImage() {
        return _image;
    }

    @Override
    public ICapturedImageFileSet getFileSet() {
        return _fileSet;
    }

    @Override
    public GaugeFigureLocalization getLocalization() {
        return this._figureType;
    }

    @Override
    public int getChannel() {
        return this._channel;
    }

}