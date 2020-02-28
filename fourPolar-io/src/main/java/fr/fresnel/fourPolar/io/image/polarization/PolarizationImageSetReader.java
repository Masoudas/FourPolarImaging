package fr.fresnel.fourPolar.io.image.polarization;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.core.image.captured.fileSet.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImage;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.image.polarization.PolarizationImage;
import fr.fresnel.fourPolar.core.image.polarization.PolarizationImageSet;
import fr.fresnel.fourPolar.io.image.polarization.fileSet.IPolarizationImageFileSet;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.io.exceptions.image.generic.NoReaderFoundForImage;
import fr.fresnel.fourPolar.io.image.generic.ImageReader;
import fr.fresnel.fourPolar.io.image.generic.tiff.TiffImageReaderFactory;
import fr.fresnel.fourPolar.io.image.polarization.fileSet.TiffPolarizationImageFileSet;

/**
 * A concrete implementation of the {@link IPolarizationImageSetReader}, which
 * can be used to read an instance of {@link PolarizationImageSet} from disk.
 * In this implementation, it is assumed that the images are tiff.
 */
public class PolarizationImageSetReader implements IPolarizationImageSetReader {
    final private ImageReader<UINT16> _reader;

    public PolarizationImageSetReader(ImageFactory factory) throws NoReaderFoundForImage {
        this._reader = TiffImageReaderFactory.getReader(factory, new UINT16());
    }

    @Override
    public IPolarizationImageSet read(File rootFolder, ICapturedImageFileSet fileSet) throws IOException {
        IPolarizationImageFileSet polFileSet = new TiffPolarizationImageFileSet(rootFolder, fileSet);
        IPolarizationImage pol0 = _readPolarizationImage(Polarization.pol0, polFileSet);
        IPolarizationImage pol45 = _readPolarizationImage(Polarization.pol45, polFileSet);
        IPolarizationImage pol90 = _readPolarizationImage(Polarization.pol90, polFileSet);
        IPolarizationImage pol135 = _readPolarizationImage(Polarization.pol90, polFileSet);
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