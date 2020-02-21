package fr.fresnel.fourPolar.io.image.generic.tiff.scifio;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.imglib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;
import fr.fresnel.fourPolar.io.image.generic.ImageReader;
import io.scif.FormatException;
import io.scif.Reader;
import io.scif.config.SCIFIOConfig;
import io.scif.formats.TIFFFormat;
import io.scif.img.ImgOpener;

/**
 * An abstract class for reading grayscale tiff images using the SCIFIO library.
 * 
 * @param <T> extends Pixel type.
 */
public abstract class GrayScaleImgLib2TiffReader<T extends PixelType> implements ImageReader<T> {
    final protected SCIFIOConfig _config;
    protected Reader _reader;
    final protected ImgOpener _imgOpener;
    final protected ImgLib2ImageFactory<T> _imgFactory;

    /**
     * An abstract class for reading grayscale tiff images using the SCIFIO library.
     * 
     */
    public GrayScaleImgLib2TiffReader(ImgLib2ImageFactory<T> factory) {
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
    public abstract Image<T> read(File file) throws IOException;

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