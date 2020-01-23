package fr.fresnel.fourPolar.io.image.tiff;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.core.image.captured.CapturedImage;
import fr.fresnel.fourPolar.core.image.captured.ICapturedImage;
import fr.fresnel.fourPolar.core.imageSet.acquisition.ICapturedImageFileSet;
import fr.fresnel.fourPolar.io.image.ICapturedImageReader;
import io.scif.FormatException;
import io.scif.Metadata;
import io.scif.Reader;
import io.scif.SCIFIO;
import io.scif.config.SCIFIOConfig;
import io.scif.img.ImgOpener;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.UnsignedShortType;

/**
 * Used for reading a tiff image and converting to the unsigned short.
 */
public class TiffCapturedImageReader implements ICapturedImageReader {

    @Override
    public ICapturedImage read(ICapturedImageFileSet fileSet, String fileLabel)
            throws IllegalArgumentException, IOException {
        _checkFileLabel(fileSet, fileLabel);

        try {
            SCIFIOConfig config = _setSCFIOConfig();
            Reader reader = _getTiFFReader(fileSet.getFile(fileLabel), config);
            Img<UnsignedShortType> img = new ImgOpener(reader.context()).openImg(reader, new UnsignedShortType(), config);
             
            reader.close();
            reader.context().dispose();

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
        return config;
    }

    private Reader _getTiFFReader(File file, SCIFIOConfig config) throws FormatException, IOException {
        SCIFIO scifio = new SCIFIO();

        return scifio.initializer().initializeReader(file.getAbsolutePath(), config);
    }



    
}