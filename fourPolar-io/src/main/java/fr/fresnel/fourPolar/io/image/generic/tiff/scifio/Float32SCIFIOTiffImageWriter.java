package fr.fresnel.fourPolar.io.image.generic.tiff.scifio;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.ConverterToImgLib2NotFound;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImageToImgLib2Converter;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.io.image.generic.ImageWriter;
import fr.fresnel.fourPolar.io.image.generic.tiff.scifio.metadata.SCIFIOTiffMetadataConverter;
import io.scif.FormatException;
import io.scif.ImageMetadata;
import io.scif.config.SCIFIOConfig;
import io.scif.formats.TIFFFormat.Metadata;
import io.scif.formats.TIFFFormat.Writer;
import io.scif.img.ImgSaver;
import io.scif.util.FormatTools;

/**
 * Class for writing grayscale tiffs to disk.
 * 
 */
public class Float32SCIFIOTiffImageWriter implements ImageWriter<Float32> {
    final private Float32 _pixelType = Float32.zero();
    final private SCIFIOConfig _config;
    final private Writer<Metadata> _writer;
    final private ImgSaver _imgSaver;

    public Float32SCIFIOTiffImageWriter() {
        this._config = _setSCFIOConfig();
        this._imgSaver = new ImgSaver();
        this._writer = new Writer<>();
        this._writer.setContext(this._imgSaver.context());
    }

    @Override
    public void write(File path, Image<Float32> image) throws IOException {
        Objects.requireNonNull(path, "path should not be null");
        Objects.requireNonNull(image, "image should not be null");

        SCIFIOUtils.checkExtension(path.getName());
        SCIFIOUtils.deleteFileIfExists(path);

        Metadata scifioMetadata = this._createSCIFIOMetadata();
        SCIFIOTiffMetadataConverter.convertTo(image.getMetadata(), scifioMetadata);
        
        try {
            this._writer.setMetadata(scifioMetadata);
            this._writer.setDest(path.getAbsolutePath(), this._config);

            this._imgSaver.saveImg(this._writer, ImageToImgLib2Converter.getImg(image, this._pixelType));
        } catch (ConverterToImgLib2NotFound e) {
        } catch (FormatException e) {
            // This exception is not caught, because we've used tiff formater.
        }

    }

    @Override
    public void close() throws IOException {
        this._imgSaver.context().dispose();
    }

    /**
     * Sets the configuration for how the image is opened.
     * 
     * @return
     */
    private SCIFIOConfig _setSCFIOConfig() {
        // For the time being, we use the very basic config.
        SCIFIOConfig config = new SCIFIOConfig();
        return config;
    }

    /**
     * Create a base metadata, and let axis and axis length be set later.
     */
    private Metadata _createSCIFIOMetadata() {
        Metadata metadata = new Metadata();

        metadata.createImageMetadata(1);

        ImageMetadata imageMetadata = metadata.get(0);
        imageMetadata.setFalseColor(true);
        imageMetadata.setPixelType(FormatTools.FLOAT);
        imageMetadata.setPlanarAxisCount(2);
        imageMetadata.setLittleEndian(false);
        imageMetadata.setIndexed(false);
        imageMetadata.setInterleavedAxisCount(0);
        imageMetadata.setThumbnail(false);
        imageMetadata.setOrderCertain(true);
        imageMetadata.setMetadataComplete(true);
        return metadata;
    }

}