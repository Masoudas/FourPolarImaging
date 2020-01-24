package fr.fresnel.fourPolar.io.image.tiff;

import java.io.File;
import java.io.IOException;

import org.scijava.io.location.FileLocation;

import fr.fresnel.fourPolar.core.image.captured.CapturedImage;
import fr.fresnel.fourPolar.core.image.captured.ICapturedImage;
import fr.fresnel.fourPolar.core.imageSet.acquisition.ICapturedImageFileSet;
import fr.fresnel.fourPolar.io.image.ICapturedImageReader;
import io.scif.FormatException;
import io.scif.Reader;
import io.scif.SCIFIO;
import io.scif.config.SCIFIOConfig;
import io.scif.img.ImgOpener;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.UnsignedShortType;

/**
 * Used for reading a 16 bit tiff image.
 */
public class TiffCapturedImageReader implements ICapturedImageReader {

    @Override
    public ICapturedImage read(final ICapturedImageFileSet fileSet, final String fileLabel)
            throws IllegalArgumentException, IOException {
        _checkFileLabel(fileSet, fileLabel);

        try {
            final SCIFIOConfig config = _setSCFIOConfig();
            final Reader reader = this._getTiFFReader(fileSet.getFile(fileLabel));

            final Img<UnsignedShortType> img = new ImgOpener().openImgs(reader, new UnsignedShortType(), config).get(0);

            reader.close();
            return new CapturedImage(fileSet, fileLabel, img);
        } catch (FormatException e) {
            // Format exception does not occur here.
        }

        return null;
    }

    private void _checkFileLabel(ICapturedImageFileSet fileSet, String fileLabel) {
        if (!fileSet.hasLabel(fileLabel)) {
            throw new IllegalArgumentException("The given file label is not in the file set.");
        }
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