package fr.fresnel.fourPolar.io.image.tiff.grayscale;

import java.io.IOException;

import fr.fresnel.fourPolar.core.image.captured.CapturedImage;
import fr.fresnel.fourPolar.core.image.captured.ICapturedImage;
import fr.fresnel.fourPolar.core.image.captured.fileContainer.ICapturedImageFileSet;
import fr.fresnel.fourPolar.io.image.ICapturedImageReader;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.UnsignedShortType;

/**
 * Used for reading a (16 bit tiff) captured images. An instance of this object
 * is enough to read several images.
 */
public class TiffCapturedImageReader implements ICapturedImageReader {
    final private TiffGrayScaleReader<UnsignedShortType> _reader;

    /**
     * Used for reading a (16 bit tiff) captured images. An instance of this object
     * is enough to read several images.
     */
    public TiffCapturedImageReader() {
        _reader = new TiffGrayScaleReader<UnsignedShortType>(new UnsignedShortType());
    }

    @Override
    public ICapturedImage read(final ICapturedImageFileSet fileSet, final String fileLabel)
            throws IllegalArgumentException, IOException {
        _checkFileLabel(fileSet, fileLabel);

        Img<UnsignedShortType> img = _reader.read(fileSet.getFile(fileLabel));

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