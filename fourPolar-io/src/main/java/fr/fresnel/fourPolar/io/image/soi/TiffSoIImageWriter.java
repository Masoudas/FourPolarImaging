package fr.fresnel.fourPolar.io.image.soi;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.soi.ISoIImage;
import fr.fresnel.fourPolar.io.exceptions.image.generic.NoWriterFoundForImage;
import fr.fresnel.fourPolar.io.image.generic.ImageWriter;
import fr.fresnel.fourPolar.io.image.generic.tiff.TiffImageWriterFactory;
import fr.fresnel.fourPolar.io.image.soi.file.ISoIImageFile;
import fr.fresnel.fourPolar.io.image.soi.file.TiffSoIImageFile;

/**
 * A concrete implementation of {@link ISoIImageWriter} to write the SoI image
 * as tiff file.
 */
public class TiffSoIImageWriter implements ISoIImageWriter {
    final private ImageWriter<UINT16> _writer;

    public TiffSoIImageWriter(ISoIImage soiImage) throws NoWriterFoundForImage {
        this._writer = TiffImageWriterFactory.getWriter(soiImage.getImage(), UINT16.zero());
    }

    @Override
    public void write(File rootFolder, ISoIImage soiImage) throws IOException {
        ISoIImageFile file = new TiffSoIImageFile(rootFolder, soiImage.getFileSet());

        this._writer.write(file.getFile(), soiImage.getImage());
    }

    @Override
    public void close() throws IOException {
        this._writer.close();
    }

}