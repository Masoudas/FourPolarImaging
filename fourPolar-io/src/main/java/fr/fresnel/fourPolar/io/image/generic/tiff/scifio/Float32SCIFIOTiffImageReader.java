package fr.fresnel.fourPolar.io.image.generic.tiff.scifio;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import fr.fresnel.fourPolar.core.exceptions.image.generic.axis.UnsupportedAxisOrder;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.io.exceptions.image.generic.metadata.MetadataParseError;
import fr.fresnel.fourPolar.io.image.generic.ImageReader;
import fr.fresnel.fourPolar.io.image.generic.tiff.scifio.metadata.SCIFIOTiffMetadataConverter;
import io.scif.config.SCIFIOConfig;
import io.scif.formats.TIFFFormat.Metadata;
import io.scif.formats.TIFFFormat.Reader;
import io.scif.img.ImgOpener;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.real.FloatType;

/**
 * This class reads a 16 bit unsigned image using SCIFIO library.
 */
public class Float32SCIFIOTiffImageReader implements ImageReader<Float32> {
    final private FloatType imgLib2Type = new FloatType();
    final private SCIFIOConfig _config;
    final private Reader<Metadata> _reader;
    final private ImgOpener _imgOpener;
    final private ImgLib2ImageFactory _imgFactory;

    public Float32SCIFIOTiffImageReader(ImgLib2ImageFactory factory) {
        this._config = _setSCFIOConfig();
        this._imgOpener = new ImgOpener();
        this._imgFactory = factory;
        this._reader = new Reader<>();
        this._reader.setContext(this._imgOpener.context());
    }

    @Override
    public Image<Float32> read(File path) throws IOException, MetadataParseError {
        Objects.requireNonNull(path, "path should not be null");
        this._checkExtension(path.getName());
        this._checkFileExists(path);

        this._reader.setSource(path.getAbsolutePath(), this._config);

        final IMetadata metadata = _readMetadata();
        final Img<FloatType> img = this._imgOpener.openImgs(_reader, imgLib2Type, this._config).get(0);

        return this._imgFactory.create(img, imgLib2Type, metadata);
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