package fr.fresnel.fourPolar.io.image.soi;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.soi.ISoIImage;
import fr.fresnel.fourPolar.io.image.generic.ImageWriter;
import fr.fresnel.fourPolar.io.image.generic.tiff.TiffImageWriterFactory;
import fr.fresnel.fourPolar.io.image.soi.file.ISoIImageFile;
import fr.fresnel.fourPolar.io.image.soi.file.TiffSoIImageFile;

/**
 * A concrete implementation of {@link ISoIImageWriter} to write the SoI image
 * as tiff file.
 */
public class TiffSoIImageWriter implements ISoIImageWriter {
    private ImageWriter<UINT16> _writer;

    /**
     * Caches the image type supplied the last time. This would allow us to create
     * only one instance of writer if image type does not change.
     */
    private ImageFactory _cachedImageType;

    public TiffSoIImageWriter() {
    }

    @Override
    public void write(File root4PProject, ISoIImage soiImage) throws IOException {
        this._createWriter(soiImage);
        ISoIImageFile file = new TiffSoIImageFile(root4PProject, soiImage.getFileSet(), soiImage.channel());

        this._writer.write(file.getFile(), soiImage.getImage());
    }

    @Override
    public void close() throws IOException {
        this._writer.close();
    }

    /**
     * If image type has not changed, use the previous writer instance. Otherwise,
     * create a new one.
     */
    private void _createWriter(ISoIImage soiImage) {
        ImageFactory factoryType = soiImage.getImage().getFactory();

        if (factoryType != this._cachedImageType) {
            _writer = TiffImageWriterFactory.getWriter(factoryType, UINT16.zero());
            _cachedImageType = factoryType;
        }
    }

}