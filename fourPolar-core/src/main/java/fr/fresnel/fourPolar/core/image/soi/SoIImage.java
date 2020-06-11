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
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

/**
 * A concrete implementation of {@link ISoIImage}.
 */
public class SoIImage implements ISoIImage {
    final private Image<UINT16> _image;
    final private ICapturedImageFileSet _fileSet;

    private int _channel = -1;

    /**
     * Create an SoI image that corresponds (in size) to a particular
     * {@link IPolarizationImage}
     * 
     * @param polarizationImageSet is a polarization image set.
     * 
     * @param factory              is the desired image factory.
     * 
     */
    public static ISoIImage create(IPolarizationImageSet polarizationImageSet) {
        Image<UINT16> pol0 = polarizationImageSet.getPolarizationImage(Polarization.pol0).getImage();

        IMetadata soiMetadata = new Metadata.MetadataBuilder(pol0.getMetadata().getDim()).axisOrder(AxisOrder.XYCZT)
                .bitPerPixel(PixelTypes.UINT_16).build();

        Image<UINT16> image = pol0.getFactory().create(soiMetadata, UINT16.zero());

        SoIImage soiImage = new SoIImage(polarizationImageSet.getFileSet(), image);
        soiImage.setChannel(polarizationImageSet.channel());

        return soiImage;
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

        SoIImage soiImage = new SoIImage(fileSet, image);
        soiImage.setChannel(channel);

        return soiImage;
    }

    /**
     * This constructor is used for an already generated SoI image.
     * 
     * @param fileSet
     * @param image
     */
    private SoIImage(ICapturedImageFileSet fileSet, Image<UINT16> image) {
        if (image.getMetadata().axisOrder() != AxisOrder.XYCZT) {
            throw new IllegalArgumentException("image must be XYCZT.");
        }

        if (image.getMetadata().numChannels() != 1) {
            throw new IllegalArgumentException("image must jave only one channel.");
        }

        this._fileSet = fileSet;
        this._image = image;
    }

    @Override
    public Image<UINT16> getImage() {
        return this._image;
    }

    @Override
    public ICapturedImageFileSet getFileSet() {
        return this._fileSet;
    }

    private void setChannel(int channel) {
        if (this._channel > 0) {
            throw new IllegalAccessError("Can't reset channel");
        }

        this._channel = channel;
    }

    @Override
    public int channel() {
        return this._channel;
    }
}