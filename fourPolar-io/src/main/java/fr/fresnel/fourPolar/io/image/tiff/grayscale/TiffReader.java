package fr.fresnel.fourPolar.io.image.tiff.grayscale;

import java.io.File;
import java.io.IOException;

import org.scijava.io.location.FileLocation;

import io.scif.FormatException;
import io.scif.Reader;
import io.scif.SCIFIO;
import io.scif.config.SCIFIOConfig;
import io.scif.img.ImgOpener;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.RealType;

/**
 * TiffReader
 */
public class TiffReader<T extends RealType<T>> {
    private T _type;

    public TiffReader(T type) {
        this._type = type;
    }

    public Img<T> read(File file) throws IOException {
        try {
            final SCIFIOConfig config = _setSCFIOConfig();
            final Reader reader = this._getTiFFReader(file);

            final Img<T> img = new ImgOpener().openImgs(reader, this._type, config).get(0);

            reader.close();
            return img;
        } catch (FormatException e) {
            // Format exception does not occur here.
        }

        return null;

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

    private Reader _getTiFFReader(final File file) throws FormatException, IOException {
        final SCIFIO scifio = new SCIFIO();
        final FileLocation fileLocation = new FileLocation(file);
        return scifio.initializer().initializeReader(fileLocation);
    }

}