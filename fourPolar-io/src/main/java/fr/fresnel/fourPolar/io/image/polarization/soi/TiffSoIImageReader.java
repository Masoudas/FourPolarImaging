package fr.fresnel.fourPolar.io.image.polarization.soi;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.polarization.soi.ISoIImage;
import fr.fresnel.fourPolar.core.image.polarization.soi.SoIImage;
import fr.fresnel.fourPolar.io.exceptions.image.generic.NoReaderFoundForImage;
import fr.fresnel.fourPolar.io.image.generic.ImageReader;
import fr.fresnel.fourPolar.io.image.generic.tiff.TiffImageReaderFactory;
import fr.fresnel.fourPolar.io.image.polarization.soi.file.ISoIImageFile;
import fr.fresnel.fourPolar.io.image.polarization.soi.file.TiffSoIImageFile;

/**
 * A concrete implementation of {@link ISoIImageReader} to read tiff SoI image.
 */
public class TiffSoIImageReader implements ISoIImageReader {
    final private ImageReader<UINT16> _reader;

    public TiffSoIImageReader(ImageFactory factory) throws NoReaderFoundForImage {
        this._reader = TiffImageReaderFactory.getReader(factory, UINT16.zero());

    }

    @Override
    public ISoIImage read(File rootFolder, ICapturedImageFileSet fileSet) throws IOException {
        ISoIImageFile file = new TiffSoIImageFile(rootFolder, fileSet);

        return new SoIImage(fileSet, this._reader.read(file.getFile()));
    }

    @Override
    public void close() throws IOException {
        this._reader.close();
    }

}