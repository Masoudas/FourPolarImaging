package fr.fresnel.fourPolar.io.image.tiff;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.core.exceptions.image.acquisition.CorruptCapturedImage;
import fr.fresnel.fourPolar.core.imageSet.acquisition.ICapturedImageChecker;
import io.scif.FormatException;
import io.scif.ImageMetadata;
import io.scif.Metadata;
import io.scif.Reader;
import io.scif.SCIFIO;

/**
 * A class for checking the compatibility of a tiff image with the software
 * criteria.
 */
public class TiffImageChecker implements ICapturedImageChecker {

    /**
     * Makes sure that the provided file is tif, and has bit depth of at least 16
     * bits.
     * 
     * @throws IOException
     * @throws CorruptCapturedImage
     */
    @Override
    public boolean checkCompatible(File image) throws CorruptCapturedImage {
        if (!this._checkExtension(image.getName())) {
            return false;
        }

        try {
            if (this._bitDepthAbove16(image)) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            throw new CorruptCapturedImage("File IO issue or Corrupt image content", e);
        } catch (FormatException e) {
            throw new CorruptCapturedImage("Format rendition error", e);
        }
    }

    private boolean _checkExtension(String fileName) {
        int index = fileName.lastIndexOf('.');
        String extension = index > 0 ? fileName.substring(index + 1) : null;

        if (extension == null || !extension.equals(this.getExtension())) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Using the metadata of the image, make sure that the given image has at least
     * 16 bit depth.
     * 
     * @return
     * @throws IOException
     * @throws FormatException
     */
    private boolean _bitDepthAbove16(File image) throws FormatException, IOException {
        final SCIFIO scifio = new SCIFIO();
        final Reader reader = scifio.initializer().initializeReader(image.getAbsolutePath());
        final Metadata meta = reader.getMetadata();

        final ImageMetadata iMeta = meta.get(0);

        if (iMeta.getBitsPerPixel() < 16) {
            return false;
        } else {
            return true;
        }

    }

    @Override
    public String getExtension() {
        return "tif";
    }

    @Override
    public String getIncompatibilityMessage() {
        return "The given tiff image does not have compatible format.";
    }

}