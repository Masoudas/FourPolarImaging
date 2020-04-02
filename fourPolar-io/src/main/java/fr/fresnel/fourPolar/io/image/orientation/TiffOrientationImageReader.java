package fr.fresnel.fourPolar.io.image.orientation;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.core.exceptions.image.orientation.CannotFormOrientationImage;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.orientation.IOrientationImage;
import fr.fresnel.fourPolar.core.image.orientation.OrientationImage;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;
import fr.fresnel.fourPolar.io.exceptions.image.generic.NoReaderFoundForImage;
import fr.fresnel.fourPolar.io.image.generic.ImageReader;
import fr.fresnel.fourPolar.io.image.generic.tiff.TiffImageReaderFactory;
import fr.fresnel.fourPolar.io.image.orientation.file.TiffOrientationImageFileSet;

/**
 * A concrete implementation of {@link IOrientationImageReader} to read tiff
 * orientation images.
 */
public class TiffOrientationImageReader implements IOrientationImageReader {
    final private ImageReader<Float32> _reader;

    /**
     * Initialize the reader for the provided {@link Image} implementation. The same
     * class can read several orientation images from the disk.
     * 
     * @param factory
     * @throws NoReaderFoundForImage
     */
    public TiffOrientationImageReader(ImageFactory factory) throws NoReaderFoundForImage {
        _reader = TiffImageReaderFactory.getReader(factory, new Float32());
    }

    @Override
    public IOrientationImage read(File rootFolder, ICapturedImageFileSet fileSet)
            throws IOException, CannotFormOrientationImage {
        TiffOrientationImageFileSet oSet = new TiffOrientationImageFileSet(rootFolder, fileSet);

        Image<Float32> rho = _reader.read(oSet.getFile(OrientationAngle.rho));
        Image<Float32> delta = _reader.read(oSet.getFile(OrientationAngle.delta));
        Image<Float32> eta = _reader.read(oSet.getFile(OrientationAngle.eta));

        return new OrientationImage(fileSet, rho, delta, eta);
    }

    @Override
    public void close() throws IOException {
        _reader.close();
    }

}