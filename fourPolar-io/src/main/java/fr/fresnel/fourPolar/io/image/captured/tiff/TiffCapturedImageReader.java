package fr.fresnel.fourPolar.io.image.captured.tiff;

import java.io.IOException;

import fr.fresnel.fourPolar.core.image.captured.CapturedImage;
import fr.fresnel.fourPolar.core.image.captured.ICapturedImage;
import fr.fresnel.fourPolar.core.image.captured.fileSet.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.io.exceptions.image.generic.NoReaderFoundForImage;
import fr.fresnel.fourPolar.io.image.captured.ICapturedImageReader;
import fr.fresnel.fourPolar.io.image.generic.ImageReader;
import fr.fresnel.fourPolar.io.image.generic.tiff.TiffImageReaderFactory;

/**
 * Used for reading a (16 bit tiff) captured images. An instance of this object
 * is enough to read several images.
 */
public class TiffCapturedImageReader implements ICapturedImageReader {
    final private ImageReader<UINT16> _reader;

    /**
     * Used for reading a (16 bit tiff) captured images. An instance of this object
     * is enough to read several images.
     * 
     * @throws NoReaderFoundForImage
     */
    public TiffCapturedImageReader(ImageFactory factory) throws NoReaderFoundForImage {
        _reader = TiffImageReaderFactory.getReader(factory, new UINT16());
    }

    @Override
    public ICapturedImage read(final ICapturedImageFileSet fileSet, final String fileLabel)
            throws IllegalArgumentException, IOException {
        _checkFileLabel(fileSet, fileLabel);

        Image<UINT16> img = _reader.read(fileSet.getFile(fileLabel));

        return new CapturedImage(fileSet, fileLabel, img);
    }

    /**
     * Close the resources of the underlying reader.
     * 
     * @throws IOException
     */
    public void close() throws IOException {
        _reader.close();
    }

    private void _checkFileLabel(ICapturedImageFileSet fileSet, String fileLabel) {
        if (!fileSet.hasLabel(fileLabel)) {
            throw new IllegalArgumentException("The given file label is not in the file set.");
        }
    }

}