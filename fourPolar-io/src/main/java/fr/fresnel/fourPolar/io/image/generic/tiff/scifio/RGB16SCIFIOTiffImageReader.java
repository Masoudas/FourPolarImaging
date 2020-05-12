package fr.fresnel.fourPolar.io.image.generic.tiff.scifio;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import fr.fresnel.fourPolar.core.exceptions.image.generic.axis.UnsupportedAxisOrder;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
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

public class RGB16SCIFIOTiffImageReader implements ImageReader<RGB16> {
    final private SCIFIOConfig _config;
    private Reader _reader;
    final private ImgOpener _imgOpener;
    final private ImgLib2ImageFactory _imgFactory;

    final UnsignedByteType _readType = new UnsignedByteType();
    final ARGBType _argbType = new ARGBType();

    public RGB16SCIFIOTiffImageReader(ImgLib2ImageFactory factory) {
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
    public Image<RGB16> read(File path) throws IOException, MetadataParseError {
        Objects.requireNonNull(path, "path should not be null");
        this._checkExtension(path.getName());
        this._checkFileExists(path);

        this._reader.setSource(path.getAbsolutePath(), this._config);

        // Read metadata
        final IMetadata metadata = _readMetadata();

        // Read the raw image, which holds R, G and B as channels.
        Img<UnsignedByteType> rawImage = this._imgOpener.openImgs(this._reader, this._readType, this._config).get(0);

        // Merger RGB channes to form ARGB image.
        Img<ARGBType> img = createARGBImage(rawImage);

        return this._imgFactory.create(img, this._argbType, metadata);
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
        IMetadata metadata = null;
        try {
            metadata = SCIFIOTiffMetadataConverter.convertFrom(this._reader.getMetadata().get(0));
        } catch (UnsupportedAxisOrder e) {
            throw new MetadataParseError(MetadataParseError.UNDEFINED_AXIS_ORDER);
        }

        return metadata;
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

    /**
     * Check file extension is tif or tiff.
     * 
     * @param fileName
     * @throws IOException
     */
    private void _checkExtension(String fileName) throws IOException {
        String extension = fileName.substring(fileName.lastIndexOf('.')).toLowerCase();

        if (!extension.equals("tiff") && !extension.equals("tif")) {
            throw new IOException("The given file to tiff reader is not tiff");
        }

    }

    private void _checkFileExists(File path) throws IOException {
        if (!path.exists()) {
            throw new IOException("The given Tiff file does not exist.");
        }
    }

}