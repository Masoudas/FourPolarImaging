package fr.fresnel.fourPolar.io.image.soi;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.algorithm.util.image.axis.AxisReassigner;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.soi.ISoIImage;
import fr.fresnel.fourPolar.core.image.soi.SoIImage;
import fr.fresnel.fourPolar.core.physics.channel.ChannelUtils;
import fr.fresnel.fourPolar.io.exceptions.image.generic.metadata.MetadataParseError;
import fr.fresnel.fourPolar.io.image.generic.ImageReader;
import fr.fresnel.fourPolar.io.image.generic.tiff.TiffImageReaderFactory;
import fr.fresnel.fourPolar.io.image.soi.file.ISoIImageFile;
import fr.fresnel.fourPolar.io.image.soi.file.TiffSoIImageFile;

/**
 * A concrete implementation of {@link ISoIImageReader} to read tiff SoI image.
 */
public class TiffSoIImageReader implements ISoIImageReader {
    final private ImageReader<UINT16> _reader;
    final private int _numChannels;

    public TiffSoIImageReader(ImageFactory factory, int numChannels) {
        this._reader = TiffImageReaderFactory.getReader(factory, UINT16.zero());
        this._numChannels = numChannels;
    }

    @Override
    public ISoIImage read(File root4PProject, ICapturedImageFileSet fileSet, int channel) throws IOException {
        ChannelUtils.checkChannelExists(channel, _numChannels);
        ISoIImageFile file = _createSoIImageFile(root4PProject, fileSet, channel);

        Image<UINT16> diskSoI = _readSoI(file);
        Image<UINT16> soi_xyczt = this._reassignToXYCZT(diskSoI);
        return SoIImage.create(fileSet, soi_xyczt, channel);
    }

    private TiffSoIImageFile _createSoIImageFile(File root4PProject, ICapturedImageFileSet fileSet, int channel) {
        return new TiffSoIImageFile(root4PProject, fileSet, channel);
    }

    private Image<UINT16> _readSoI(ISoIImageFile file) throws IOException {
        Image<UINT16> diskSoI = null;
        try {
            diskSoI = this._reader.read(file.getFile());
        } catch (MetadataParseError | IOException e) {
            throw new IOException("SoI images doesn't exist or is corrupted");
        }
        return diskSoI;
    }

    private Image<UINT16> _reassignToXYCZT(Image<UINT16> diskSoI) {
        // TODO we need to get rid of this method, by making sure that when image is
        // written, z and t with dimension 1 are properly written to the disk.
        if (diskSoI.getMetadata().axisOrder() == ISoIImage.AXIS_ORDER) {
            return diskSoI;
        } else {
            return AxisReassigner.reassignToXYCZT(diskSoI, UINT16.zero());
        }

    }

    @Override
    public void close() throws IOException {
        this._reader.close();
    }

}