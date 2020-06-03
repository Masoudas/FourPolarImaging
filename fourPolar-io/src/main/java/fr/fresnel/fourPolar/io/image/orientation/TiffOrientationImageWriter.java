package fr.fresnel.fourPolar.io.image.orientation;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.algorithm.postprocess.orientation.OrientationAngleConverter;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.orientation.IOrientationImage;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;
import fr.fresnel.fourPolar.io.exceptions.image.generic.NoWriterFoundForImage;
import fr.fresnel.fourPolar.io.image.generic.ImageWriter;
import fr.fresnel.fourPolar.io.image.generic.tiff.TiffImageWriterFactory;
import fr.fresnel.fourPolar.io.image.orientation.file.IOrientationImageFileSet;
import fr.fresnel.fourPolar.io.image.orientation.file.TiffOrientationImageFileSet;
import fr.fresnel.fourPolar.io.image.orientation.file.TiffOrientationImageInDegreeFileSet;

/**
 * A concrete implementation of {@link IOrientationImageReader} to write the
 * orientation image as tiff files.
 */
public class TiffOrientationImageWriter implements IOrientationImageWriter {
    final private ImageWriter<Float32> _writer;

    /**
     * Initialize the writer for the provided type of orientation image. The same
     * class can write several orientation images to the disk.
     * 
     * @param image
     * @throws NoWriterFoundForImage
     */
    public TiffOrientationImageWriter(IOrientationImage image) throws NoWriterFoundForImage {
        _writer = TiffImageWriterFactory.getWriter(image.getAngleImage(OrientationAngle.rho).getImage(),
                Float32.zero());
    }

    /**
     * Initialize the writer for the given type of image factory. The same class can
     * write several orientation images to the disk.
     * 
     * @param image
     * @throws NoWriterFoundForImage
     */
    public TiffOrientationImageWriter(ImageFactory imageFactory) throws NoWriterFoundForImage {
        _writer = TiffImageWriterFactory.getWriter(imageFactory, Float32.zero());
    }

    @Override
    public void write(File root4PProject, IOrientationImage orientationImage) throws IOException {
        TiffOrientationImageFileSet oSet = new TiffOrientationImageFileSet(root4PProject,
                orientationImage.getCapturedSet(), orientationImage.channel());

        _write(orientationImage, oSet);

    }

    @Override
    public void close() throws IOException {
        _writer.close();
    }

    @Override
    public void writeInDegrees(File root4PProject, IOrientationImage orientationImage) throws IOException {
        TiffOrientationImageInDegreeFileSet oSet = new TiffOrientationImageInDegreeFileSet(root4PProject,
                orientationImage.getCapturedSet(), orientationImage.channel());
        this._converAnglesToDegrees(orientationImage);
        this._write(orientationImage, oSet);
        this._converAnglesBackToRadian(orientationImage);
    }

    private void _write(IOrientationImage orientationImage, IOrientationImageFileSet oSet) throws IOException {
        _writer.write(oSet.getFile(OrientationAngle.rho),
                orientationImage.getAngleImage(OrientationAngle.rho).getImage());
        _writer.write(oSet.getFile(OrientationAngle.delta),
                orientationImage.getAngleImage(OrientationAngle.delta).getImage());
        _writer.write(oSet.getFile(OrientationAngle.eta),
                orientationImage.getAngleImage(OrientationAngle.eta).getImage());
    }

    private void _converAnglesBackToRadian(IOrientationImage orientationImage) {
        OrientationAngleConverter.convertToRadian(orientationImage);
    }

    private void _converAnglesToDegrees(IOrientationImage orientationImage) {
        OrientationAngleConverter.convertToDegree(orientationImage);
    }

}