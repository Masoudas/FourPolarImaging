package fr.fresnel.fourPolar.io.image.generic.tiff.scifio;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.ARGB8;
import fr.fresnel.fourPolar.io.exceptions.image.generic.metadata.MetadataParseError;
import fr.fresnel.fourPolar.io.image.generic.ImageReader;
import fr.fresnel.fourPolar.io.image.generic.tiff.scifio.metadata.SCIFIOTiffMetadataConverter;
import io.scif.FormatException;
import io.scif.Reader;
import io.scif.config.SCIFIOConfig;
import io.scif.formats.TIFFFormat;
import io.scif.img.ImgOpener;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.converter.ColorChannelOrder;
import net.imglib2.converter.Converters;
import net.imglib2.img.Img;
import net.imglib2.loops.LoopBuilder;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.util.Util;

public class SCIFIORGB16TiffReader implements ImageReader<ARGB8> {
    final private SCIFIOConfig _config;
    private Reader _reader;
    final private ImgOpener _imgOpener;
    final private ImgLib2ImageFactory _imgFactory;

    final UnsignedByteType _readType = new UnsignedByteType();
    final ARGBType _argbType = new ARGBType();

    public static String NOT_3CHANNEL_IMAGE = "RGB image does not have 3 channels.";
    public static String NOT_8BIT = "Image is not 8 bit per pixel.";

    public SCIFIORGB16TiffReader(ImgLib2ImageFactory factory) {
        this._config = _setSCFIOConfig();
        this._imgOpener = new ImgOpener();
        this._imgFactory = factory;

        final TIFFFormat format = new TIFFFormat();
        format.setContext(_imgOpener.getContext());

        try {
            this._reader = format.createReader();
        } catch (FormatException e) {
            // Will never be caught.
        }
    }

    @Override
    public Image<ARGB8> read(File path) throws IOException, MetadataParseError {
        throw new AssertionError("This reader has not been refactored to consider ARGB images with channel.");
        // Objects.requireNonNull(path, "path should not be null");
        // SCIFIOUtils.checkExtension(path.getName());
        // SCIFIOUtils.checkFileExists(path);

        // this._reader.setSource(path.getAbsolutePath(), this._config);

        // final IMetadata rawMetadata = _readMetadata();

        // // Apply checks
        // _checkImageIs8bit(rawMetadata);

        // // Check image has exactly 3 channels.
        // _checkImageHas3Channels(rawMetadata);

        // // Create a copy of metadata, indicating only a single channel.

        // // Read the raw image, which holds R, G and B as channels.
        // Img<UnsignedByteType> rawImage = this._imgOpener.openImgs(this._reader, this._readType, this._config).get(0);

        // // Merger RGB channes to form ARGB image.
        // Img<ARGBType> img = createARGBImage(rawImage);

        // return this._imgFactory.create(img, this._argbType, rawMetadata);
    }

    /**
     * Creates an {@link Img} interface from a given
     * {@link RandomAccessibleInterval}.
     */
    private Img<ARGBType> createARGBImage(Img<UnsignedByteType> rawImage) {
        assert rawImage != null : "Image cannot be null";
        RandomAccessibleInterval<ARGBType> rai = Converters.mergeARGB(rawImage, ColorChannelOrder.RGB);

        Img<ARGBType> img = Util.getSuitableImgFactory(rai, new ARGBType()).create(rai);
        LoopBuilder.setImages(img, rai).forEachPixel(ARGBType::set);

        return img;
    }

    @Override
    public void close() throws IOException {
        this._reader.close();

    }

    /**
     * Read metadata independently of the underlying image.
     * 
     * @throws MetadataParseError if there are problems parsing the metadata.
     */
    private IMetadata _readMetadata() throws IOException, MetadataParseError {
        return SCIFIOTiffMetadataConverter.convertFrom(this._reader.getMetadata().get(0));
    }

    /**
     * Sets the configuration for how the image is opened.
     * 
     * @return
     */
    private SCIFIOConfig _setSCFIOConfig() {
        // For the time being, we use the very basic config.
        SCIFIOConfig config = new SCIFIOConfig();
        config.imgOpenerSetImgModes(SCIFIOConfig.ImgMode.AUTO);
        return config;
    }

    private void _checkImageIs8bit(IMetadata metadata) throws IOException {
        if (metadata.bitPerPixel() != 8) {
            throw new IOException(NOT_8BIT);
        }

    }

    private void _checkImageHas3Channels(IMetadata metadata) throws IOException {
        if (metadata.axisOrder() == AxisOrder.NoOrder || metadata.numChannels() != 3) {
            throw new IOException(NOT_3CHANNEL_IMAGE);
        }

    }

    /**
     * Create a version of metadata that contains only one channel (becuase this is
     * how the {@link Image} treats the image).
     * 
     * @param rawMetadata is the raw metadata read from the disk.
     */
    // private _createSingleChannelMetadata(IMetadata rawMetadata){
    // new Metadata.MetadataBuilder()
    // }

}