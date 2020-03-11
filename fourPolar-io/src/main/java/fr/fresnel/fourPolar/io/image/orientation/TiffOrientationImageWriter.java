package fr.fresnel.fourPolar.io.image.orientation;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.core.image.captured.fileSet.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.orientation.IOrientationImage;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;
import fr.fresnel.fourPolar.io.exceptions.image.generic.NoWriterFoundForImage;
import fr.fresnel.fourPolar.io.image.generic.ImageWriter;
import fr.fresnel.fourPolar.io.image.generic.tiff.TiffImageWriterFactory;
import fr.fresnel.fourPolar.io.image.orientation.fileSet.TiffOrientationImageFileSet;

/**
 * A concrete implementation of {@link IOrientationImageReader} to write the
 * orientation image as tiff files.
 */
public class TiffOrientationImageWriter implements IOrientationImageWriter {
    final private ImageWriter<Float32> _writer;

    /**
     * @throws NoWriterFoundForImage
     * 
     */
    public TiffOrientationImageWriter(IOrientationImage image) throws NoWriterFoundForImage {
        _writer = TiffImageWriterFactory.getWriter(
            image.getAngleImage(OrientationAngle.rho).getImage(), new Float32());
    }

    @Override
    public void write(File rootFolder, ICapturedImageFileSet fileSet, IOrientationImage image) throws IOException {
        TiffOrientationImageFileSet oSet = new TiffOrientationImageFileSet(rootFolder, fileSet);

        _writer.write(oSet.getFile(OrientationAngle.rho), image.getAngleImage(OrientationAngle.rho).getImage());
        _writer.write(oSet.getFile(OrientationAngle.delta), image.getAngleImage(OrientationAngle.delta).getImage());
        _writer.write(oSet.getFile(OrientationAngle.eta), image.getAngleImage(OrientationAngle.eta).getImage());

    }

    @Override
    public void close() throws IOException {
        _writer.close();
    }

    
}