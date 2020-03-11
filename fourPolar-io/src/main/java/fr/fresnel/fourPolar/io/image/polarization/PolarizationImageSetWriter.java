package fr.fresnel.fourPolar.io.image.polarization;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.io.exceptions.image.generic.NoWriterFoundForImage;
import fr.fresnel.fourPolar.io.image.generic.ImageWriter;
import fr.fresnel.fourPolar.io.image.generic.tiff.TiffImageWriterFactory;
import fr.fresnel.fourPolar.io.image.polarization.fileSet.IPolarizationImageFileSet;
import fr.fresnel.fourPolar.io.image.polarization.fileSet.TiffPolarizationImageFileSet;

/**
 * A concrete implementation of the {@link IPolarizationImageSetWriter}. This
 * writer, writes the {@link IPolarizationImageSet} as tiff files.
 */
public class PolarizationImageSetWriter implements IPolarizationImageSetWriter {
    private ImageWriter<UINT16> _writer;

    /**
     * A concrete implementation of the {@link IPolarizationImageSetWriter}.
     * 
     * @param imageSet is the imageSet we wish to write to disk. Note that once this
     *                 class is constructed, it can be used to write different
     *                 instances of {@code IPolarizationImageSet} with the same
     *                 instance.
     */
    public PolarizationImageSetWriter(IPolarizationImageSet imageSet) throws NoWriterFoundForImage{
        this._writer = TiffImageWriterFactory.getWriter(imageSet.getPolarizationImage(Polarization.pol0).getImage(),
                new UINT16());
    }

    @Override
    public void write(File rootFolder, IPolarizationImageSet imageSet) throws IOException {
        IPolarizationImageFileSet polFileSet = new TiffPolarizationImageFileSet(rootFolder, imageSet.getFileSet());
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
    private void _writePolarizationImage(
        Polarization pol, IPolarizationImageFileSet polFileSet, IPolarizationImageSet imageSet) 
        throws IOException {
        this._writer.write(polFileSet.getFile(pol), imageSet.getPolarizationImage(pol).getImage());
    }

    /**
     * A method for writing for a polarization image with metadata.
     * 
     * @param pol
     * @param imageSet
     * @param metadata
     * @throws IOException
     */
    private void _writePolarizationImage(
        Polarization pol, IPolarizationImageFileSet polFileSet, IPolarizationImageSet imageSet, 
        IMetadata metadata) throws IOException {
        this._writer.write(polFileSet.getFile(pol), metadata, imageSet.getPolarizationImage(pol).getImage());
    }

    @Override
    public void close() throws IOException {
        this._writer.close();
    }

}