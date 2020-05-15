package fr.fresnel.fourPolar.io.image.captured.tiff.checker;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.core.exceptions.image.generic.axis.UnsupportedAxisOrder;
import fr.fresnel.fourPolar.core.exceptions.imageSet.acquisition.IncompatibleCapturedImage;
import fr.fresnel.fourPolar.core.image.captured.checker.ICapturedImageChecker;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFile;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.imageSet.acquisition.RejectedCapturedImage;
import fr.fresnel.fourPolar.io.exceptions.image.generic.metadata.MetadataParseError;
import fr.fresnel.fourPolar.io.image.generic.IMetadataReader;

/**
 * A class for checking the compatibility of a tiff image with the software
 * criteria.
 */
public class TiffCapturedImageChecker implements ICapturedImageChecker {
    /**
     * List of all the conditions that are checked together with conditions.
     */
    public final static String NOT_EXIST = "The file does not exist or cannot be accessed.";
    public final static String NOT_16_BIT = "Bit depth != 16. The image should not be used.";
    public final static String NOT_TIFF = "Not a tiff (tif) file.";
    public final static String CONTENT_CORRUPT = "File IO issue or Corrupt tiff content.";
    public final static String UNDEFINED_AXIS = "At least one axis is undefined or axis order is incompatible (e.g CXY)";
    public final static String WRONG_NUM_CHANNEL = "The number of image channels (wavelengths) don't match imaging wavelengths.";
    final private IMetadataReader _metaDataReader;

    public TiffCapturedImageChecker(IMetadataReader reader) {
        this._metaDataReader = reader;
    }

    /**
     * Checks whether the given image satisfies the constraints listed by the static
     * parameters of this class.
     * 
     * @throws IncompatibleCapturedImage for any of violated conditions. A reference
     *                                   to the file that violates the condition
     *                                   together with its cause is in the
     *                                   exception.
     * @throws UnsupportedAxisOrder
     * @throws IOException
     */
    @Override
    public void check(ICapturedImageFile image) throws IncompatibleCapturedImage {
        _imageExistsAndReadable(image);
        _extensionIsTifOrTiff(image);

        IMetadata metadata = _axisOrderIsWellDefined(image);

        _bitDepthAbove16(metadata, image);
        _NumImageChannelsCorrespond(metadata, image);
    }

    private IMetadata _axisOrderIsWellDefined(ICapturedImageFile image) throws IncompatibleCapturedImage {
        IMetadata metadata = null;
        try {
            metadata = this._metaDataReader.read(image.file());
        } catch (MetadataParseError e) {
            throw new IncompatibleCapturedImage(new RejectedCapturedImage(image.file(), UNDEFINED_AXIS));
        } catch (IOException e) {
            throw new IncompatibleCapturedImage(new RejectedCapturedImage(image.file(), CONTENT_CORRUPT));
        }

        return metadata;
    }

    private void _imageExistsAndReadable(ICapturedImageFile image) throws IncompatibleCapturedImage {
        try {
            if (!image.file().exists()) {
                throw new IncompatibleCapturedImage(new RejectedCapturedImage(image.file(), NOT_EXIST));
            }
        } catch (SecurityException e) {
            throw new IncompatibleCapturedImage(new RejectedCapturedImage(image.file(), NOT_EXIST));
        }
    }

    private void _extensionIsTifOrTiff(ICapturedImageFile image) throws IncompatibleCapturedImage {
        int index = image.file().getName().lastIndexOf('.');
        String extension = index > 0 ? image.file().getName().substring(index + 1) : null;

        if (extension == null
                || (!extension.equals(this.getExtension()) && !extension.equals(this.getExtension() + 'f'))) {
            throw new IncompatibleCapturedImage(new RejectedCapturedImage(image.file(), NOT_TIFF));
        }
    }

    /**
     * Using the metadata of the image, make sure that the given image has at least
     * 16 bit depth.
     */
    private void _bitDepthAbove16(IMetadata metadata, ICapturedImageFile image) throws IncompatibleCapturedImage {
        if (metadata.bitPerPixel() != 16) {
            throw new IncompatibleCapturedImage(new RejectedCapturedImage(image.file(), NOT_16_BIT));
        }
    }

    /**
     * Using the metadata of the image, make sure that number of channels equal the
     * given number of channels;
     */
    private void _NumImageChannelsCorrespond(IMetadata metadata, ICapturedImageFile image)
            throws IncompatibleCapturedImage {
        boolean userSpecifiedOneChannel = image.channels().length == 1;
        boolean tiffOneChannel = metadata.numChannels() == 0 || metadata.numChannels() == 1;

        boolean userSpecifiedMultiChannel = !userSpecifiedOneChannel && image.channels().length > 1;
        boolean tiffAndUserHaveSameNumChannels = metadata.numChannels() == image.channels().length;

        if (userSpecifiedOneChannel && !tiffOneChannel) {
            throw new IncompatibleCapturedImage(new RejectedCapturedImage(image.file(), WRONG_NUM_CHANNEL));
        } else if (userSpecifiedMultiChannel && !tiffAndUserHaveSameNumChannels) {
            throw new IncompatibleCapturedImage(new RejectedCapturedImage(image.file(), WRONG_NUM_CHANNEL));
        }

    }

    private String getExtension() {
        return "tif";
    }

}