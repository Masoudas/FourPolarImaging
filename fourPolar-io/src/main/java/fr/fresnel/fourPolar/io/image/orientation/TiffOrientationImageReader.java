package fr.fresnel.fourPolar.io.image.orientation;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.core.exceptions.image.orientation.CannotFormOrientationImage;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.orientation.IOrientationImage;
import fr.fresnel.fourPolar.core.image.orientation.OrientationImageFactory;
import fr.fresnel.fourPolar.core.physics.channel.ChannelUtils;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;
import fr.fresnel.fourPolar.io.exceptions.image.generic.NoReaderFoundForImage;
import fr.fresnel.fourPolar.io.exceptions.image.generic.metadata.MetadataParseError;
import fr.fresnel.fourPolar.io.image.generic.ImageReader;
import fr.fresnel.fourPolar.io.image.generic.tiff.TiffImageReaderFactory;
import fr.fresnel.fourPolar.io.image.orientation.file.TiffOrientationImageFileSet;

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

        IOrientationImage orientationImage = null;
        try {
            Image<Float32> rho = _reader.read(oSet.getFile(OrientationAngle.rho));
            Image<Float32> delta = _reader.read(oSet.getFile(OrientationAngle.delta));
            Image<Float32> eta = _reader.read(oSet.getFile(OrientationAngle.eta));
            orientationImage = OrientationImageFactory.create(fileSet, channel, rho, delta, eta);
        } catch (MetadataParseError | IOException e) {
            throw new IOException("orientation image doesn't exist or is corrupted");
        }

        return orientationImage;
    }

    @Override
    public void close() throws IOException {
        _reader.close();
    }

}