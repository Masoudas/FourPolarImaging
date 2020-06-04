package fr.fresnel.fourPolar.io.visualization.figures.polarization.tiff;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.physics.channel.ChannelUtils;
import fr.fresnel.fourPolar.core.preprocess.registration.RegistrationRule;
import fr.fresnel.fourPolar.core.visualization.figures.polarization.IPolarizationImageSetComposites;
import fr.fresnel.fourPolar.core.visualization.figures.polarization.PolarizationImageSetCompositesBuilder;
import fr.fresnel.fourPolar.io.exceptions.image.generic.metadata.MetadataParseError;
import fr.fresnel.fourPolar.io.image.generic.ImageReader;
import fr.fresnel.fourPolar.io.image.generic.tiff.TiffImageReaderFactory;
import fr.fresnel.fourPolar.io.visualization.figures.polarization.IPolarizationImageSetCompositesReader;

/**
 * A concrete implementation of {@link IPolarizationImageSetCompositesReader} to
 * read tiff composite images.
 */
public class TiffPolarizationImageSetCompositesReader implements IPolarizationImageSetCompositesReader {
    final private ImageReader<RGB16> _reader;
    final private int _numChannels;
    final private PolarizationImageSetCompositesBuilder _compositeSetBuilder;

    /**
     * Initialize the reader for the provided {@link Image} implementation. The same
     * class can read several orientation images from the disk.
     * 
     * @param factory
     * @throws NoReaderFoundForImage
     */
    public TiffPolarizationImageSetCompositesReader(ImageFactory factory, int numChannels) {
        Objects.requireNonNull(factory);
        ChannelUtils.checkNumChannelsNonZero(numChannels);

        _reader = TiffImageReaderFactory.getReader(factory, RGB16.zero());
        _numChannels = numChannels;
        _compositeSetBuilder = new PolarizationImageSetCompositesBuilder(this._numChannels);
    }

    /**
     * Read all composite images on the given root. fileSet is allowed to be null if
     * this is a registration image set.
     */
    private IPolarizationImageSetComposites _read(File rootCompositeImages, ICapturedImageFileSet fileSet, int channel)
            throws IOException {
        HashMap<RegistrationRule, Image<RGB16>> compositeFigures = _readRules(rootCompositeImages);
        _buildCompositionSet(fileSet, channel, compositeFigures);

        return this._compositeSetBuilder.build();
    }

    private HashMap<RegistrationRule, Image<RGB16>> _readRules(File rootCompositeImages) throws IOException {
        HashMap<RegistrationRule, Image<RGB16>> compositeFigures = new HashMap<>();

        for (RegistrationRule rule : RegistrationRule.values()) {
            compositeFigures.put(rule, this._readRule(rootCompositeImages, rule));
        }
        return compositeFigures;
    }

    private Image<RGB16> _readRule(File rootCompositeImages, RegistrationRule rule) throws IOException {
        File ruleFile = TiffPolarizationImageSetCompositesUtil.getRuleFile(rootCompositeImages, rule);

        Image<RGB16> ruleImage = null;
        try {
            ruleImage = this._reader.read(ruleFile);
        } catch (MetadataParseError | IOException e) {
            throw new IOException("composite images does not exist or is corrupted");
        }

        return ruleImage;
    }


    private void _buildCompositionSet(ICapturedImageFileSet fileSet, int channel,
            HashMap<RegistrationRule, Image<RGB16>> compositeFigures) {
        this._compositeSetBuilder.setChannel(channel);
        this._compositeSetBuilder.setFileSet(fileSet);

        for (RegistrationRule rule : RegistrationRule.values()) {
            this._compositeSetBuilder.setCompositeImage(rule, compositeFigures.get(rule));
        }
    }


    @Override
    public void close() throws IOException {
        _reader.close();
    }

    @Override
    public IPolarizationImageSetComposites readRegistrationComposite(File root4PProject, int channel)
            throws IOException {
        Objects.requireNonNull(root4PProject);
        ChannelUtils.checkChannel(channel, this._numChannels);
        File rootCompositeImages = TiffPolarizationImageSetCompositesUtil
                .getRootFolderRegistrationComposites(root4PProject, channel);
        return this._read(rootCompositeImages, null, channel);
    }

    @Override
    public IPolarizationImageSetComposites read(File root4PProject, String visualizationSession, int channel,
            ICapturedImageFileSet fileSet) throws IOException {
        Objects.requireNonNull(fileSet);
        Objects.requireNonNull(root4PProject);
        ChannelUtils.checkChannel(channel, this._numChannels);
        
        File rootCompositeImages = TiffPolarizationImageSetCompositesUtil.getRootFolder(root4PProject,
                visualizationSession, channel, fileSet);
        return this._read(rootCompositeImages, fileSet, channel);
    }

}