package fr.fresnel.fourPolar.io.image.tiff.grayscale;

import java.io.File;
import java.io.IOException;

import io.scif.Writer;
import io.scif.config.SCIFIOConfig;
import io.scif.formats.TIFFFormat;
import io.scif.img.ImgIOException;
import io.scif.img.ImgSaver;
import io.scif.FormatException;
import io.scif.Metadata;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.RealType;

/**
 * Class for writing grayscale tiffs to disk.
 * 
 * @param <T> extends {@ RealType}.
 */
class TiffGrayScaleWriter<T extends RealType<T>> {
    final private ImgSaver _saver;
    final private SCIFIOConfig _config;
    private Writer _writer;

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
     * Note that opening one instance of the writer is enought to write several
     * images. Otherwise, a context is created everytime.
     * 
     * @param destination
     * @param img
     * @throws ImgIOException
     */
    public void write(final File destination, Img<T> img) throws IOException {
        if (destination.exists()) {
            destination.delete();
        }
        this._saver.saveImg(destination.getAbsolutePath(), img, this._config);

    }

    public void write(final File destination, Metadata metadata, Img<T> img) throws IOException {
        /**
         * We are probably going to need to define a Dataplane object for each plane of
         * the image to be written, then populate it with img object plane, and finally
         * write every plane with the writer object. It appears that if I want to write
         * omero metadata, I need to manually iterate over the image.
         * 
         * It will be interesting to see how the saveImg method of ImgSaver populates
         * the metadata too.
         * 
         * If it comes down to simply adding comments, this link is helpful
         * https://github.com/ome/bioformats/blob/develop/components/formats-gpl/utils/EditTiffComment.java,
         * where the TiffParser gets us the comments and TiffSaver has a method for
         * overriding comments.
         * 
         * This link is also interesting:
         * https://github.com/ome/bioformats/blob/develop/components/formats-gpl/utils/EditImageName.java
         */
        if (destination.exists()) {
            destination.delete();
        }

        try {
            _writer.setMetadata(metadata);
            _writer.setDest(destination.getAbsolutePath(), this._config);

            this._saver.saveImg(_writer, img, _config);
        } catch (FormatException e) {

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
}