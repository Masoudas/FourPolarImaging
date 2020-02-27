package fr.fresnel.fourPolar.io.image.polarization;

import java.io.IOException;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.io.image.generic.ImageWriter;

/**
 * A concrete implementation of the {@link IPolarizationImageSetWriter}, which can be used
 * to write an instance of {@link PolarizationImageSet} to disk.
 */
public class PolarizationImageSetWriter implements IPolarizationImageSetWriter {
    final private ImageWriter<UINT16> _writer;

    /**
     * A concrete implementation of the {@link IPolarizationImageSetWriter}. 
     * 
     * @param writer is a writer interface for writing {@link UINT16} type image.
     */
    public PolarizationImageSetWriter(ImageWriter<UINT16> writer) {
        this._writer = writer;
    }

    @Override
    public void write(IPolarizationImageSet imageSet) throws IOException {
        _writePolarizationImage(Polarization.pol0, imageSet);
        _writePolarizationImage(Polarization.pol45, imageSet);
        _writePolarizationImage(Polarization.pol90, imageSet);
        _writePolarizationImage(Polarization.pol135, imageSet);

    }

    /**
     * A method for writing for a polarization image without metadata.
     * @param pol
     * @param imageSet
     * @throws IOException
     */
    private void _writePolarizationImage(Polarization pol, IPolarizationImageSet imageSet) throws IOException {
        this._writer.write(imageSet.getFileSet().getFile(pol), imageSet.getPolarizationImage(pol).getImage());
    }

    /**
     * A method for writing for a polarization image with metadata.
     * @param pol
     * @param imageSet
     * @param metadata
     * @throws IOException
     */
    private void _writePolarizationImage(Polarization pol, IPolarizationImageSet imageSet, IMetadata metadata) throws IOException {
        this._writer.write(imageSet.getFileSet().getFile(pol), metadata, imageSet.getPolarizationImage(pol).getImage());
    }

    @Override
    public void close() throws IOException {
        this._writer.close();
    }

}