package fr.fresnel.fourPolar.io.image.polarization;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.io.image.generic.ImageWriter;
import fr.fresnel.fourPolar.io.image.generic.tiff.TiffImageWriterFactory;
import fr.fresnel.fourPolar.io.image.polarization.file.IPolarizationImageFileSet;
import fr.fresnel.fourPolar.io.image.polarization.file.TiffPolarizationImageFileSet;

/**
 * A concrete implementation of {@link IPolarizationImageSetWriter} to write the
 * polarization image as tiff files.
 */
public class TiffPolarizationImageSetWriter implements IPolarizationImageSetWriter {
    private ImageWriter<UINT16> _writer;

    /**
     * Caches the image type supplied the last time. This would allow us to create
     * only one instance of writer if image type does not change.
     */
    private ImageFactory _cachedImageType;

    /**
     * Initialize the writer for the provided type of orientation image. The same
     * class can write several orientation images to the disk.
     *
     * @param imageSet
     * @throws NoWriterFoundForImage
     */
    public TiffPolarizationImageSetWriter() {
    }

    /**
     * Initialize the writer for the provided image type. The same class can write
     * several orientation images to the disk.
     *
     * @param imageSet
     * @throws NoWriterFoundForImage
     */
    public TiffPolarizationImageSetWriter(ImageFactory imageFactory) {
        this._writer = TiffImageWriterFactory.getWriter(imageFactory, UINT16.zero());
    }

    @Override
    public void write(File root4PProject, IPolarizationImageSet imageSet) throws IOException {
        this._createWriter(imageSet);

        IPolarizationImageFileSet polFileSet = new TiffPolarizationImageFileSet(root4PProject, imageSet.getFileSet(),
                imageSet.channel());
        _writePolarizationImage(Polarization.pol0, polFileSet, imageSet);
        _writePolarizationImage(Polarization.pol45, polFileSet, imageSet);
        _writePolarizationImage(Polarization.pol90, polFileSet, imageSet);
        _writePolarizationImage(Polarization.pol135, polFileSet, imageSet);
    }

    /**
     * A method for writing for a polarization image without metadata.
     * 
     * @param pol
     * @param imageSet
     * @throws IOException
     */
    private void _writePolarizationImage(Polarization pol, IPolarizationImageFileSet polFileSet,
            IPolarizationImageSet imageSet) throws IOException {
        this._writer.write(polFileSet.getFile(pol), imageSet.getPolarizationImage(pol).getImage());
    }

    @Override
    public void close() throws IOException {
        this._writer.close();
    }

    /**
     * If image type has not changed, use the previous writer instance. Otherwise,
     * create a new one.
     */
    private void _createWriter(IPolarizationImageSet imageSet) {
        ImageFactory factoryType = imageSet.getPolarizationImage(Polarization.pol0).getImage().getFactory();

        if (factoryType != this._cachedImageType) {
            _writer = TiffImageWriterFactory.getWriter(factoryType, UINT16.zero());
            _cachedImageType = factoryType;
        }
    }

}