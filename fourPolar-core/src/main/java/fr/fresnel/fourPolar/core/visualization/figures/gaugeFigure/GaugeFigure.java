package fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure;

import java.util.Arrays;
import java.util.Objects;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.ARGB8;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelTypes;
import fr.fresnel.fourPolar.core.image.soi.ISoIImage;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.AngleGaugeType;

/**
 * A concrete implementation of the {@link IGaugeFigure} that uses {@link Image}
 * as its backend.
 * <p>
 * Note that regardless of the form of gauge figure, the underlying image is
 * always equivalent to {@link ISoIImage#AXIS_ORDER}.
 */
public class GaugeFigure implements IGaugeFigure {
    private final Image<ARGB8> _image;
    private final AngleGaugeType _type;
    private final ICapturedImageFileSet _fileSet;
    private final GaugeFigureLocalization _figureType;
    private final int _channel;

    /**
     * Creates an empty 2D gauge figure, that can be used for representing
     * orientation of a dipole stick.
     * 
     * @param figDim  is the dimension of the figure (both x and y).
     * @param channel is the channel number of this figure.
     * @param fileSet is the fileSet of the image this figure represents.
     * @param factory is the factory by which this figure is created.
     * 
     * @return an empty 2D gauge figure.
     */
    public static GaugeFigure singleDipoleDelta2DStick(long figDim, ISoIImage soiImage) {
        Objects.requireNonNull(soiImage, "soiImage can't be null");

        if (figDim < 0) {
            throw new IllegalArgumentException("figure dimension can't be negative");
        }

        long[] fig_dim = new long[AXIS_ORDER.numAxis];
        Arrays.setAll(fig_dim, (i) -> 1);
        fig_dim[0] = figDim;
        fig_dim[1] = figDim;

        IMetadata fig_metadata = new Metadata.MetadataBuilder(fig_dim).axisOrder(AXIS_ORDER)
                .bitPerPixel(PixelTypes.ARGB_8).build();
        Image<ARGB8> figure = soiImage.getImage().getFactory().create(fig_metadata, ARGB8.zero());

        return new GaugeFigure(GaugeFigureLocalization.SINGLE_DIPOLE, AngleGaugeType.Delta2D, figure,
                soiImage.getFileSet(), soiImage.channel());
    }

    /**
     * Creates a figure instance for representing {@link AngleGaugeType#Rho2D}. Note
     * that the same factory as the soi image is used for creating this gauge
     * figure.
     * 
     * @param soiImage is the soi image for which we want to create this gauge
     *                 figure.
     * @return an empty gauge figure.
     */
    public static GaugeFigure wholeSampleRho2DStick(ISoIImage soiImage) {
        return _createWholeSample2DStickFigure(soiImage, AngleGaugeType.Rho2D);
    }

    /**
     * Creates a figure instance for representing {@link AngleGaugeType#Delta2D}.
     * Note that the same factory as the soi image is used for creating this gauge
     * figure.
     * 
     * @param soiImage is the soi image for which we want to create this gauge
     *                 figure.
     * @return an empty gauge figure.
     */
    public static GaugeFigure wholeSampleDelta2DStick(ISoIImage soiImage) {
        return _createWholeSample2DStickFigure(soiImage, AngleGaugeType.Delta2D);
    }

    /**
     * Creates a figure instance for representing {@link AngleGaugeType#Eta2D}. Note
     * that the same factory as the soi image is used for creating this gauge
     * figure.
     * 
     * @param soiImage is the soi image for which we want to create this gauge
     *                 figure.
     * @return an empty gauge figure.
     */
    public static GaugeFigure wholeSampleEta2DStick(ISoIImage soiImage) {
        return _createWholeSample2DStickFigure(soiImage, AngleGaugeType.Eta2D);
    }

    /**
     * Creates a figure instance for representing a {@link AngleGaugeType#Stick3D}.
     * To accommodate the sticks in the z direction, the generated image is
     * interleaved in the z direction by a factor. Hence if factor is 2, the gauge
     * figure would have to z-planes for each z-plane of soi.
     * <p>
     * Note that the same factory as the soi image is used for creating this gauge
     * figure.
     * 
     * @param soiImage           is the soi image for which we want to create this
     *                           gauge figure.
     * @param z_interleaveFactor is ratio of z-planes to be placed for each soi
     *                           z-plane.
     * @return an empty gauge figure.
     */
    public static GaugeFigure wholeSample3DStick(ISoIImage soiImage, int z_interleaveFactor) {
        Objects.requireNonNull(soiImage, "soiImage can't be null");

        if (z_interleaveFactor < 1) {
            throw new IllegalArgumentException("z-interleave factor can't be less than one.");
        }

        IMetadata soi_metadata = soiImage.getImage().getMetadata();
        long[] img_size = soi_metadata.getDim();
        img_size[AXIS_ORDER.z_axis] *= z_interleaveFactor;

        IMetadata fig_metadata = new Metadata.MetadataBuilder(img_size).axisOrder(AXIS_ORDER)
                .bitPerPixel(PixelTypes.ARGB_8).build();
        Image<ARGB8> figure = soiImage.getImage().getFactory().create(fig_metadata, ARGB8.zero());

        return new GaugeFigure(GaugeFigureLocalization.WHOLE_SAMPLE, AngleGaugeType.Stick3D, figure,
                soiImage.getFileSet(), soiImage.channel());
    }

    /**
     * Create a gauge figure of same size as soi with the given gauge.
     */
    private static GaugeFigure _createWholeSample2DStickFigure(ISoIImage soiImage, AngleGaugeType gaugeType) {
        Objects.requireNonNull(soiImage, "soiImage can't be null");
        Objects.requireNonNull(gaugeType, "gaugeType can't be null");

        IMetadata fig_metadata = new Metadata.MetadataBuilder(soiImage.getImage().getMetadata().getDim())
                .axisOrder(AXIS_ORDER).bitPerPixel(PixelTypes.ARGB_8).build();
        Image<ARGB8> figure = soiImage.getImage().getFactory().create(fig_metadata, ARGB8.zero());

        return new GaugeFigure(GaugeFigureLocalization.WHOLE_SAMPLE, gaugeType, figure, soiImage.getFileSet(),
                soiImage.channel());
    }

    /**
     * Create a gauge figure with the provided image interface.
     * 
     * @param figureType     is the type of figure (representation) meant by this
     *                       figure.
     * @param angleGaugeType is the angle gauge type of this gauge figure.
     * @param image          is the {@link Image} interface of the figure.
     * @param fileSet        is the fileSet associated with this gauge figure.
     * @param channel        is the channel number.
     * @return a gauge figure.
     * 
     * @throws IllegalArgumentException is thrown in case the axis order of the
     *                                  given image is not the same as that of
     *                                  {@link ISoIImage.AXIS_ORDER}.
     */
    public static IGaugeFigure create(GaugeFigureLocalization figureType, AngleGaugeType angleGaugeType,
            Image<ARGB8> image, ICapturedImageFileSet fileSet, int channel) {
        Objects.requireNonNull(angleGaugeType, "angleGaugeType cannot be null.");
        Objects.requireNonNull(image, "image cannot be null");
        Objects.requireNonNull(fileSet, "fileSet cannot be null");

        if (image.getMetadata().axisOrder() != AXIS_ORDER) {
            throw new IllegalArgumentException(
                    "The given image does not have the same axis-order as default gauge figure.");
        }

        return new GaugeFigure(figureType, angleGaugeType, image, fileSet, channel);
    }

    /**
     * Create an stick figure using the provided color image.
     * 
     * @param localization   is the localization level of this figure
     * @param angleGaugeType is the type of gauge.
     * @param image          is the image instance.
     * @param fileSet        is the file set this figure is associated with.
     * @param channel        is the channel this figure is associated with.
     */
    private GaugeFigure(GaugeFigureLocalization localization, AngleGaugeType angleGaugeType, Image<ARGB8> image,
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

    public Image<ARGB8> getImage() {
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