package fr.fresnel.fourPolar.io.image.orientation;

import java.io.File;
import java.io.IOException;

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
import fr.fresnel.fourPolar.io.exceptions.image.generic.NoReaderFoundForImage;
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
    public TiffOrientationImageReader(ImageFactory factory, int numChannels) throws NoReaderFoundForImage {
        _reader = TiffImageReaderFactory.getReader(factory, Float32.zero());
        _numChannels = numChannels;
    }

    @Override
    public IOrientationImage read(File root4PProject, ICapturedImageFileSet fileSet, int channel)
            throws IOException, CannotFormOrientationImage {
        ChannelUtils.checkChannel(channel, this._numChannels);
        TiffOrientationImageFileSet oSet = new TiffOrientationImageFileSet(root4PProject, fileSet, channel);

        return this._read(oSet, fileSet);
    }

    @Override
    public IOrientationImage readFromDegrees(File root4PProject, ICapturedImageFileSet fileSet, int channel)
            throws IOException, CannotFormOrientationImage {
        ChannelUtils.checkChannel(channel, this._numChannels);
        TiffOrientationImageInDegreeFileSet oSet = new TiffOrientationImageInDegreeFileSet(root4PProject, fileSet,
                channel);

        IOrientationImage orientationImage = this._read(oSet, fileSet);
        converAnglesToRadian(orientationImage);
        return orientationImage;
    }

    @Override
    public void close() throws IOException {
        _reader.close();
    }

    private IOrientationImage _read(IOrientationImageFileSet orientationSet, ICapturedImageFileSet capturedSet)
            throws CannotFormOrientationImage, IOException {
        IOrientationImage orientationImage = null;
        try {
            Image<Float32> rho = _reader.read(orientationSet.getFile(OrientationAngle.rho));
            this._reassignAngleImageToXYCZT(rho);

            Image<Float32> delta = _reader.read(orientationSet.getFile(OrientationAngle.delta));
            this._reassignAngleImageToXYCZT(delta);

            Image<Float32> eta = _reader.read(orientationSet.getFile(OrientationAngle.eta));
            this._reassignAngleImageToXYCZT(eta);
            
            orientationImage = OrientationImageFactory.create(capturedSet, orientationSet.getChannel(), rho, delta,
                    eta);
        } catch (MetadataParseError | IOException e) {
            throw new IOException("orientation image doesn't exist or is corrupted");
        }

        return orientationImage;
    }

    /**
     * It may happen that if the t dimension of the image is 1 (or z for that matter), when we 
     * read it from disk, it would appear as xy. Hence, we need to reassign if necessary.
     */
    private Image<Float32> _reassignAngleImageToXYCZT(Image<Float32> angleImage){
        // TODO we need to get rid of this method, by making sure that when image is written,
        // z and t with dimension 1 are properly written to the disk.
        if (angleImage.getMetadata().axisOrder() == IAngleImage.AXIS_ORDER){
            return angleImage;
        } else {
            return AxisReassigner.reassignToXYCZT(angleImage, Float32.zero());
        }

    }

    private void converAnglesToRadian(IOrientationImage orientationImage) {
        OrientationAngleConverter.convertToRadian(orientationImage);
    }

}