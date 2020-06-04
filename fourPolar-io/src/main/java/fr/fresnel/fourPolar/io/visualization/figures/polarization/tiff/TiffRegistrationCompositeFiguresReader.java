package fr.fresnel.fourPolar.io.visualization.figures.polarization.tiff;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.physics.channel.ChannelUtils;
import fr.fresnel.fourPolar.core.preprocess.registration.RegistrationRule;
import fr.fresnel.fourPolar.core.visualization.figures.polarization.IPolarizationImageSetComposites;
import fr.fresnel.fourPolar.core.visualization.figures.polarization.PolarizationImageSetComposites;
import fr.fresnel.fourPolar.io.exceptions.image.generic.metadata.MetadataParseError;
import fr.fresnel.fourPolar.io.image.generic.ImageReader;
import fr.fresnel.fourPolar.io.image.generic.tiff.TiffImageReaderFactory;
import fr.fresnel.fourPolar.io.visualization.figures.polarization.IRegistrationCompositeFiguresReader;

/**
 * A concrete implementation of {@link IRegistrationCompositeFiguresReader} to
 * read tiff composite images.
 */
public class TiffRegistrationCompositeFiguresReader implements IRegistrationCompositeFiguresReader {
    final private ImageReader<RGB16> _reader;
    final private int _numChannels;

    /**
     * Initialize the reader for the provided {@link Image} implementation. The same
     * class can read several orientation images from the disk.
     * 
     * @param factory
     * @throws NoReaderFoundForImage
     */
    public TiffRegistrationCompositeFiguresReader(ImageFactory factory, int numChannels) {
        Objects.requireNonNull(factory);
        ChannelUtils.checkNumChannelsNonZero(numChannels);

        _reader = TiffImageReaderFactory.getReader(factory, RGB16.zero());
        _numChannels = numChannels;
    }

    @Override
    public IPolarizationImageSetComposites read(File root4PProject, int channel) throws IOException {
        ChannelUtils.checkChannel(channel, this._numChannels);
        IPolarizationImageSetComposites compositeFigures = new PolarizationImageSetComposites(channel);

        try {
            for (RegistrationRule rule : RegistrationRule.values()) {
                File rulePath = TiffRegistrationCompositeFiguresUtils.getRuleFile(root4PProject, channel, rule);
                Image<RGB16> compositeImage = this._reader.read(rulePath);
                compositeFigures.setCompositeImage(rule, compositeImage);
            }
        } catch (MetadataParseError | IOException e) {
            throw new IOException("composite images don't exist or are corrupted");
        }

        return compositeFigures;
    }

    @Override
    public void close() throws IOException {
        _reader.close();
    }

}