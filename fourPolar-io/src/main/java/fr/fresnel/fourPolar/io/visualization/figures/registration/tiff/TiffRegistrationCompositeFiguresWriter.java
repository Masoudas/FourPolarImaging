package fr.fresnel.fourPolar.io.visualization.figures.registration.tiff;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.preprocess.registration.RegistrationRule;
import fr.fresnel.fourPolar.core.visualization.figures.polarization.IPolarizationImageSetComposites;
import fr.fresnel.fourPolar.io.image.generic.ImageWriter;
import fr.fresnel.fourPolar.io.image.generic.tiff.TiffImageWriterFactory;
import fr.fresnel.fourPolar.io.visualization.figures.registration.IRegistrationCompositeFiguresWriter;

/**
 * A concrete implementation of {@link IRegistrationCompositeFiguresWriter} to
 * write the tiff composite images.
 */
public class TiffRegistrationCompositeFiguresWriter implements IRegistrationCompositeFiguresWriter {
    private ImageWriter<RGB16> _writer;

    /**
     * Caches the image type supplied the last time. This would allow us to create
     * only one instance of writer if image type does not change.
     */
    private ImageFactory _cachedImageType;

    /**
     * Initialize the writer. The same class can write several composite instances
     * to the disk.
     */
    public TiffRegistrationCompositeFiguresWriter() {
    }

    @Override
    public void write(File root4PProject, IPolarizationImageSetComposites composites) throws IOException {
        this._createWriter(composites);
        int channel = composites.channel();
        for (RegistrationRule rule : RegistrationRule.values()) {
            File rulePath = TiffRegistrationCompositeFiguresUtils.getRuleFile(root4PProject, channel, rule);
            Image<RGB16> compositeImage = composites.getCompositeImage(rule);

            this._writer.write(rulePath, compositeImage);
        }
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
        ImageFactory factoryType = compositeFigure.getCompositeImage(RegistrationRule.values()[0]).getFactory();

        if (factoryType != this._cachedImageType) {
            _writer = TiffImageWriterFactory.getWriter(factoryType, RGB16.zero());

        }
    }

}