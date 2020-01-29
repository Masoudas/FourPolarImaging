package fr.fresnel.fourPolar.io.image.tiff.grayscale;

import java.io.File;
import java.io.IOException;

import io.scif.FormatException;
import io.scif.Reader;
import io.scif.SCIFIO;
import io.scif.config.SCIFIOConfig;
import io.scif.formats.TIFFFormat;
import io.scif.img.ImgOpener;
import net.imglib2.img.Img;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;

/**
 * A module level private class, used for reading grayscale images of arbitrary
 * type.
 * 
 * @param <T> extends {@ RealType}.
 */
class TiffGrayScaleReader<T extends RealType<T> & NativeType<T>> {
    final private T _type;
    final private SCIFIOConfig _config;
    private Reader _reader;
    final private ImgOpener _imgOpener;

    /**
     * A module level private class, used for reading grayscale images of arbitrary
     * type.
     * 
     * @param type an instance of {@ RealType}.
     */
    public TiffGrayScaleReader(T type) {
        this._type = type;
        this._config = _setSCFIOConfig();
        this._imgOpener = new ImgOpener();
        
        final TIFFFormat format = new TIFFFormat();
        format.setContext(_imgOpener.getContext());

        try {
            this._reader = format.createReader();
        } catch (FormatException e) {
            // Will never be caught.

        }

    }

    /**
     * Reads the image and returns the proper image interface.
     * 
     * @param file
     * @return
     * @throws IOException
     */
    public Img<T> read(File file) throws IOException {
        _reader.setSource(file.getAbsolutePath(), this._config);
        final Img<T> img = this._imgOpener.openImgs(_reader, this._type, this._config).get(0);

        return img;
    }

    public void close() throws IOException {
        this._reader.close();
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

}