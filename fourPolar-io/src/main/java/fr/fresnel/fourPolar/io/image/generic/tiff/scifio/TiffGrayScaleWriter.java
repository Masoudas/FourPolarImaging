package fr.fresnel.fourPolar.io.image.generic.tiff.scifio;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.core.image.generic.ITiffMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;
import fr.fresnel.fourPolar.io.image.generic.tiff.TiffImageWriter;
import io.scif.Writer;
import io.scif.config.SCIFIOConfig;
import io.scif.formats.TIFFFormat;
import io.scif.img.ImgSaver;
import io.scif.FormatException;

/**
 * An abstract class for writing grayscale tiff images using the SCIFIO library.
 * 
 * @param <T> extends Pixel type.
 */
public abstract class TiffGrayScaleWriter<T extends PixelType> implements TiffImageWriter<T> {
    final protected ImgSaver _saver;
    final protected SCIFIOConfig _config;
    protected Writer _writer;

    /**
     * An abstract class for writing grayscale tiff images using the SCIFIO library.
     * 
     */
    public TiffGrayScaleWriter() {
        this._saver = new ImgSaver();
        this._config = this._setSCFIOConfig();

        final TIFFFormat tiffFormat = new TIFFFormat();
        tiffFormat.setContext(this._saver.getContext());
        try {
            _writer = tiffFormat.createWriter();

        } catch (FormatException e) {
            // Will never be caught.
        }
    }

    /**
     * Close all resources when done reading all the files.
     */
    public void close() {
        this._saver.context().dispose();
    }

    /**
     * Sets the configuration for how the image is opened.
     * 
     * @return
     */
    private SCIFIOConfig _setSCFIOConfig() {
        // For the time being, we use the very basic config.
        SCIFIOConfig config = new SCIFIOConfig();
        config.imgSaverSetWriteRGB(false);
        config.writerSetSequential(true);
        config.writerSetFramesPerSecond(100);
        config.checkerSetOpen(false);
        return config;
    }

    @Override
    public abstract void write(File path, Image<T> image) throws IOException;

    @Override
    public abstract void write(File path, ITiffMetadata metadata, Image<T> tiff) throws IOException;
}