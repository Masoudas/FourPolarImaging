package fr.fresnel.fourPolar.io.image.orientation;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.algorithm.postprocess.orientation.OrientationAngleConverter;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.orientation.IOrientationImage;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;
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
    private ImageWriter<Float32> _writer;

    /**
     * Caches the image type supplied the last time. This would allow us to create
     * only one instance of writer if image type does not change.
     */
    private ImageFactory _cachedImageType;

    /**
     * Initialize the writer. The same class can write several orientation images to
     * the disk.
     */
    public TiffOrientationImageWriter() {
        _cachedImageType = null;
    }

    @Override
    public void write(File root4PProject, IOrientationImage orientationImage) throws IOException {
        IOrientationImageFileSet oSet = _createOrientationImageFileSet(root4PProject, orientationImage);
        _write(orientationImage, oSet);

    }

    @Override
    public void close() throws IOException {
        _writer.close();
    }

    @Override
    public void writeInDegrees(File root4PProject, IOrientationImage orientationImage) throws IOException {
        IOrientationImageFileSet oSet = _createOrientationImageInDegreeFileSet(root4PProject, orientationImage);

        for (OrientationAngle angle : OrientationAngle.values()) {
            Image<Float32> angleImage = this._converAngleImageToDegree(orientationImage, angle);
            _writer.write(oSet.getFile(OrientationAngle.rho), angleImage);
        }
        
    }

    private void _write(IOrientationImage orientationImage, IOrientationImageFileSet oSet) throws IOException {
        this._createWriter(orientationImage);
        for (OrientationAngle angle : OrientationAngle.values()) {
            _writer.write(oSet.getFile(angle), orientationImage.getAngleImage(angle).getImage());
        }
    }

    private Image<Float32> _converAngleImageToDegree(IOrientationImage orientationImage, OrientationAngle angle) {
        return OrientationAngleConverter.convertToDegree(orientationImage, angle);
    }

    /**
     * If image type has not changed, use the previous writer instance. Otherwise,
     * create a new one.
     */
    private void _createWriter(IOrientationImage orientationImage) {
        ImageFactory factoryType = orientationImage.getAngleImage(OrientationAngle.rho).getImage().getFactory();

        if (factoryType != this._cachedImageType) {
            _writer = TiffImageWriterFactory.getWriter(factoryType, Float32.zero());

        }
    }

    private IOrientationImageFileSet _createOrientationImageFileSet(File root4PProject,
            IOrientationImage orientationImage) {
        return new TiffOrientationImageFileSet(root4PProject, orientationImage.getCapturedSet(),
                orientationImage.channel());
    }

    private IOrientationImageFileSet _createOrientationImageInDegreeFileSet(File root4PProject,
            IOrientationImage orientationImage) {
        return new TiffOrientationImageInDegreeFileSet(root4PProject, orientationImage.getCapturedSet(),
                orientationImage.channel());
    }

}