package fr.fresnel.fourPolar.io.image.orientation;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import fr.fresnel.fourPolar.algorithm.postprocess.orientation.OrientationAngleConverter;
import fr.fresnel.fourPolar.algorithm.util.image.axis.AxisReassigner;
import fr.fresnel.fourPolar.core.exceptions.image.orientation.CannotFormOrientationImage;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.orientation.IAngleImage;
import fr.fresnel.fourPolar.core.image.orientation.IOrientationImage;
import fr.fresnel.fourPolar.core.image.orientation.OrientationImageFactory;
import fr.fresnel.fourPolar.core.physics.channel.ChannelUtils;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;
import fr.fresnel.fourPolar.io.exceptions.image.generic.metadata.MetadataParseError;
import fr.fresnel.fourPolar.io.image.generic.ImageReader;
import fr.fresnel.fourPolar.io.image.generic.tiff.TiffImageReaderFactory;
import fr.fresnel.fourPolar.io.image.orientation.file.IOrientationImageFileSet;
import fr.fresnel.fourPolar.io.image.orientation.file.TiffOrientationImageFileSet;
import fr.fresnel.fourPolar.io.image.orientation.file.TiffOrientationImageInDegreeFileSet;

/**
 * A concrete implementation of {@link IOrientationImageReader} to read tiff
 * orientation images.
 */
public class TiffOrientationImageReader implements IOrientationImageReader {
    final private ImageReader<Float32> _reader;
    final private int _numChannels;

    /**
     * Initialize the reader for the provided {@link Image} implementation. The same
     * class can read several orientation images from the disk.
     * 
     * @param factory
     * @throws NoReaderFoundForImage
     */
    public TiffOrientationImageReader(ImageFactory factory, int numChannels) {
        _reader = TiffImageReaderFactory.getReader(factory, Float32.zero());
        _numChannels = numChannels;
    }

    @Override
    public IOrientationImage read(File root4PProject, ICapturedImageFileSet fileSet, int channel)
            throws IOException, CannotFormOrientationImage {
        ChannelUtils.checkChannelExists(channel, this._numChannels);
        TiffOrientationImageFileSet oSet = new TiffOrientationImageFileSet(root4PProject, fileSet, channel);

        HashMap<OrientationAngle, Image<Float32>> angleImages = this._read(oSet, fileSet);
        return _createOrientationImage(angleImages.get(OrientationAngle.rho), angleImages.get(OrientationAngle.delta), angleImages.get(OrientationAngle.eta), 
        fileSet, channel);
    }

    @Override
    public IOrientationImage readFromDegrees(File root4PProject, ICapturedImageFileSet fileSet, int channel)
            throws IOException, CannotFormOrientationImage {
        ChannelUtils.checkChannelExists(channel, this._numChannels);
        TiffOrientationImageInDegreeFileSet oSet = new TiffOrientationImageInDegreeFileSet(root4PProject, fileSet,
                channel);

        HashMap<OrientationAngle, Image<Float32>> angleImages = this._read(oSet, fileSet);
        for (OrientationAngle angle : OrientationAngle.values()) {
            _convertDegreeImageToRadian(angleImages.get(angle));
        }

        return _createOrientationImage(angleImages.get(OrientationAngle.rho), angleImages.get(OrientationAngle.delta), angleImages.get(OrientationAngle.eta), 
            fileSet, channel);
    }

    @Override
    public void close() throws IOException {
        _reader.close();
    }


    private HashMap<OrientationAngle, Image<Float32>> _read(IOrientationImageFileSet orientationImageFileSet, ICapturedImageFileSet capturedSet)
            throws IOException {
        HashMap<OrientationAngle, Image<Float32>> angleImages = new HashMap<>();
        for (OrientationAngle angle : OrientationAngle.values()) {
            try {

                angleImages.put(angle, this._readAngleImage(angle, orientationImageFileSet));
            } catch (MetadataParseError | IOException e) {
                throw new IOException("orientation image doesn't exist or is corrupted");
            }
        }

        return angleImages;
    }

    private IOrientationImage _createOrientationImage(Image<Float32> rhoImage, Image<Float32> deltaImage, Image<Float32> etaImage, 
        ICapturedImageFileSet fileSet, int channel) throws IOException {
        IOrientationImage orientationImage = null;
        try {
            orientationImage = OrientationImageFactory.create(fileSet, channel, rhoImage, deltaImage, etaImage);
        } catch (CannotFormOrientationImage e){
            // Because if orientation images don't have the same size, the files are not coherent.
            throw new IOException("orientation image doesn't exist or is corrupted");
        }

        return orientationImage;

    }

    private Image<Float32> _readAngleImage(OrientationAngle angle, IOrientationImageFileSet orientationImageFileSet)
            throws IOException, MetadataParseError {
        Image<Float32> diskAngleImage = _reader.read(orientationImageFileSet.getFile(angle));
        return this._reassignAngleImageToXYCZT(diskAngleImage);
    }

    /**
     * It may happen that if the t dimension of the image is 1 (or z for that
     * matter), when we read it from disk, it would appear as xy. Hence, we need to
     * reassign if necessary.
     */
    private Image<Float32> _reassignAngleImageToXYCZT(Image<Float32> angleImage) {
        // TODO we need to get rid of this method, by making sure that when image is
        // written, z and t with dimension 1 are properly written to the disk.
        if (angleImage.getMetadata().axisOrder() == IAngleImage.AXIS_ORDER) {
            return angleImage;
        } else {
            return AxisReassigner.reassignToXYCZT(angleImage, Float32.zero());
        }

    }

    private void _convertDegreeImageToRadian(Image<Float32> angleImageInDegree) {
        OrientationAngleConverter.convertToRadian(angleImageInDegree);
    }

}