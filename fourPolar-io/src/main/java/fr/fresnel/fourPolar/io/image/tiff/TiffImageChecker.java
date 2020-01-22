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
    public static String notExist = "The file does not exist or cannot be accessed.";
    public static String not16bit = "The given tiff is not 16 bit.";
    public static String badExtension = "The given file is not tiff.";
    public static String corruptContent =  "File IO issue or Corrupt tiff content.";
    public static String formatError = "Format rendition error in SCIFIO package.";

    /**
     * Makes sure that the provided file is tif, and has bit depth of at least 16
     * bits.
     * 
     * @throws IOException
     * @throws CorruptCapturedImage
     */
    @Override
    public void checkCompatible(File image) throws CorruptCapturedImage {
        try {
            if (!image.isFile()){
                throw new CorruptCapturedImage(notExist);
            }                 
        } catch (SecurityException e) {
            throw new CorruptCapturedImage(notExist);
        }
        
        this._checkExtension(image.getName());

        try {
            this._bitDepthAbove16(image);
        } catch (IOException e) {
            throw new CorruptCapturedImage(corruptContent, e);
        } catch (FormatException e) {
            throw new CorruptCapturedImage(formatError, e);
        }
    }

    private void _checkExtension(String fileName) throws CorruptCapturedImage {
        int index = fileName.lastIndexOf('.');
        String extension = index > 0 ? fileName.substring(index + 1) : null;

        if (extension == null || !extension.equals(this.getExtension())) {
            throw new CorruptCapturedImage(badExtension);
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
    private void _bitDepthAbove16(File image) throws FormatException, IOException, CorruptCapturedImage {
        final SCIFIO scifio = new SCIFIO();
        final Reader reader = scifio.initializer().initializeReader(image.getAbsolutePath());
        final Metadata meta = reader.getMetadata();

        final ImageMetadata iMeta = meta.get(0);

        if (iMeta.getBitsPerPixel() < 16) {
            throw new CorruptCapturedImage(not16bit);
        } 
    }

    @Override
    public String getExtension() {
        return "tif";
    }

}