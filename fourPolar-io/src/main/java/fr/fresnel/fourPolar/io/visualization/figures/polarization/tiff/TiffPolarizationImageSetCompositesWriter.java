package fr.fresnel.fourPolar.io.visualization.figures.polarization.tiff;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.ARGB8;
import fr.fresnel.fourPolar.core.preprocess.registration.RegistrationRule;
import fr.fresnel.fourPolar.core.visualization.figures.polarization.IPolarizationImageComposite;
import fr.fresnel.fourPolar.core.visualization.figures.polarization.IPolarizationImageSetComposites;
import fr.fresnel.fourPolar.io.image.generic.ImageWriter;
import fr.fresnel.fourPolar.io.image.generic.tiff.TiffImageWriterFactory;
import fr.fresnel.fourPolar.io.visualization.figures.polarization.IPolarizationImageSetCompositesWriter;

/**
 * A concrete implementation of {@link IPolarizationImageSetCompositesWriter} to
 * write the tiff composite images.
 */
public class TiffPolarizationImageSetCompositesWriter implements IPolarizationImageSetCompositesWriter {
    private ImageWriter<ARGB8> _writer;

    /**
     * Caches the image type supplied the last time. This would allow us to create
     * only one instance of writer if image type does not change.
     */
    private ImageFactory _cachedImageType;

    /**
     * Initialize the writer. The same class can write several composite instances
     * to the disk.
     */
    public TiffPolarizationImageSetCompositesWriter() {
    }

    @Override
    public void write(File root4PProject, String visualizationSession, IPolarizationImageSetComposites compositeFigures)
            throws IOException {
        Objects.requireNonNull(root4PProject);
        Objects.requireNonNull(visualizationSession);
        Objects.requireNonNull(compositeFigures);

        int channel = compositeFigures.channel();
        ICapturedImageFileSet fileSet = compositeFigures.getFileSet().get();

        File rootCompositeImages = TiffPolarizationImageSetCompositesUtil.getRootFolder(root4PProject,
                visualizationSession, channel, fileSet);

        this._writeRules(rootCompositeImages, compositeFigures);
    }

    @Override
    public void writeAsRegistrationComposite(File root4PProject, IPolarizationImageSetComposites compositeFigures)
            throws IOException {
        Objects.requireNonNull(root4PProject);
        Objects.requireNonNull(compositeFigures);

        int channel = compositeFigures.channel();
        File rootCompositeImages = TiffPolarizationImageSetCompositesUtil
                .getRootFolderRegistrationComposites(root4PProject, channel);

        this._writeRules(rootCompositeImages, compositeFigures);
    }

    /**
     * Write all the rules of this composite to the disk.
     */
    private void _writeRules(File rootCompositeImages, IPolarizationImageSetComposites compositeFigures)
            throws IOException {
        this._createWriter(compositeFigures);
        for (RegistrationRule rule : RegistrationRule.values()) {
            this._writeRule(rootCompositeImages, compositeFigures.getCompositeImage(rule));
        }
    }

    private void _writeRule(File rootCompositeImages, IPolarizationImageComposite composite) throws IOException {
        File ruleFile = TiffPolarizationImageSetCompositesUtil.getRuleFile(rootCompositeImages,
                composite.getRegistrationRule());

        this._writer.write(ruleFile, composite.getImage());
    }

    @Override
    public void close() throws IOException {
        this._writer.close();

    }

    /**
     * If image type has not changed, use the previous writer instance. Otherwise,
     * create a new one.
     */
    private void _createWriter(IPolarizationImageSetComposites compositeFigure) {
        ImageFactory factoryType = compositeFigure.getCompositeImage(RegistrationRule.values()[0]).getImage()
                .getFactory();

        if (factoryType != this._cachedImageType) {
            _writer = TiffImageWriterFactory.getWriter(factoryType, ARGB8.zero());

        }
    }

}