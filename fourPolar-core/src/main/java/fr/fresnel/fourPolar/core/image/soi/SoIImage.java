package fr.fresnel.fourPolar.core.image.soi;

import java.util.Objects;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelTypes;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImage;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.physics.channel.ChannelUtils;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

/**
 * A concrete implementation of {@link ISoIImage}.
 */
public class SoIImage implements ISoIImage {
    /**
     * Create an SoI image that corresponds (in size) to a particular
     * {@link IPolarizationImage}. The image is created with the same
     * {@link ImageFactory} as the polarization image.
     * 
     * @param polarizationImageSet is a polarization image set.
     */
    public static ISoIImage create(IPolarizationImageSet polarizationImageSet) {
        Image<UINT16> soiImage = _createSoIImageFromPolarizationImage(polarizationImageSet);
        return new SoIImage(polarizationImageSet.getFileSet(), soiImage, polarizationImageSet.channel());
    }

    /**
     * Use one of the polarization images (here pol0), and create an image that has
     * the same size as this image to be used for SoI.
     */
    private static Image<UINT16> _createSoIImageFromPolarizationImage(IPolarizationImageSet polarizationImageSet) {
        Image<UINT16> pol0 = polarizationImageSet.getPolarizationImage(Polarization.pol0).getImage();

        IMetadata soiMetadata = new Metadata.MetadataBuilder(pol0.getMetadata().getDim())
                .axisOrder(ISoIImage.AXIS_ORDER).bitPerPixel(PixelTypes.UINT_16).build();

        return pol0.getFactory().create(soiMetadata, UINT16.zero());
    }

    /**
     * Create an SoI image directly from an image interface. This method is used
     * when reading the image from the disk.
     * 
     * @throws IllegalArgumentException in case image is not XYCZT or has multiple
     *                                  channels.
     */
    public static ISoIImage create(ICapturedImageFileSet fileSet, Image<UINT16> image, int channel) {
        Objects.requireNonNull(fileSet, "fileSet can't be null");
        Objects.requireNonNull(image, "image can't be null");

        if (channel < 0) {
            throw new IllegalArgumentException("Channel must be greater than zero.");
        }

        return new SoIImage(fileSet, image, channel);
    }

    final private Image<UINT16> _image;
    final private ICapturedImageFileSet _fileSet;

    final private int _channel;

    /**
     * This constructor is used for an already generated SoI image.
     * 
     * @param fileSet
     * @param image
     */
    private SoIImage(ICapturedImageFileSet fileSet, Image<UINT16> image, int channel) {
        _checkAxisOrder(image);
        _checkImageIsSingleChannel(image);
        ChannelUtils.checkChannelExists(channel, fileSet.getChannels().length);

        this._fileSet = fileSet;
        this._image = image;
        this._channel = channel;
    }

    private void _checkImageIsSingleChannel(Image<UINT16> image) {
        if (image.getMetadata().numChannels() != 1) {
            throw new IllegalArgumentException("image must jave only one channel.");
        }
    }

    private void _checkAxisOrder(Image<UINT16> image) {
        if (image.getMetadata().axisOrder() != AxisOrder.XYCZT) {
            throw new IllegalArgumentException("image must be XYCZT.");
        }
    }

    @Override
    public Image<UINT16> getImage() {
        return this._image;
    }

    @Override
    public ICapturedImageFileSet getFileSet() {
        return this._fileSet;
    }

    @Override
    public int channel() {
        return this._channel;
    }
}