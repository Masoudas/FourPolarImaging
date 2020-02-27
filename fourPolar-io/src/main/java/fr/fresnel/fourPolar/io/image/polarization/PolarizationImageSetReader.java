package fr.fresnel.fourPolar.io.image.polarization;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImage;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.image.polarization.PolarizationImage;
import fr.fresnel.fourPolar.core.image.polarization.PolarizationImageSet;
import fr.fresnel.fourPolar.core.image.polarization.fileContainer.IPolarizationImageFileSet;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.io.image.generic.ImageReader;

/**
 * A concrete implementation of the {@link IPolarizationImageSetReader}, which can be used
 * to read an instance of {@link PolarizationImageSet} from disk.
 */
public class PolarizationImageSetReader implements IPolarizationImageSetReader {
    final private ImageReader<UINT16> _reader;

    public PolarizationImageSetReader(ImageReader<UINT16> reader) {
        this._reader = reader;
    }

    @Override
    public IPolarizationImageSet read(IPolarizationImageFileSet fileSet) throws IOException {
        IPolarizationImage pol0 = _readPolarizationImage(Polarization.pol0, fileSet);
        IPolarizationImage pol45 = _readPolarizationImage(Polarization.pol45, fileSet);
        IPolarizationImage pol90 = _readPolarizationImage(Polarization.pol90, fileSet);
        IPolarizationImage pol135 = _readPolarizationImage(Polarization.pol90, fileSet);
        return new PolarizationImageSet(fileSet, pol0, pol45, pol90, pol135);
    }

    private IPolarizationImage _readPolarizationImage(Polarization pol, IPolarizationImageFileSet fileSet)
            throws IOException {
        File imageFile = fileSet.getFile(pol);
        Image<UINT16> image = this._reader.read(imageFile);

        return new PolarizationImage(pol, image);
    }

    @Override    
    public void close() throws IOException {
        this._reader.close();
    }

    
}