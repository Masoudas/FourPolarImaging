package fr.fresnel.fourPolar.io.visualization.figures.registration.tiff;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.preprocess.registration.RegistrationRule;
import fr.fresnel.fourPolar.core.visualization.figures.registration.IRegistrationCompositeFigures;
import fr.fresnel.fourPolar.io.exceptions.image.generic.NoWriterFoundForImage;
import fr.fresnel.fourPolar.io.image.generic.ImageWriter;
import fr.fresnel.fourPolar.io.image.generic.tiff.TiffImageWriterFactory;
import fr.fresnel.fourPolar.io.visualization.figures.registration.IRegistrationCompositeFiguresWriter;

/**
 * A concrete implementation of {@link IRegistrationCompositeFiguresWriter} to
 * write the tiff composite images.
 */
public class TiffRegistrationCompositeFiguresWriter implements IRegistrationCompositeFiguresWriter {
    final private ImageWriter<RGB16> _writer;

    /**
     * Initialize the writer for the provided type of composites. The same class can
     * write several orientation images to the disk.
     * 
     * @param image
     * @throws NoWriterFoundForImage
     */
    public TiffRegistrationCompositeFiguresWriter(ImageFactory factory)
            throws NoWriterFoundForImage {
        _writer = TiffImageWriterFactory.getWriter(factory, RGB16.zero());
    }

    @Override
    public void write(File root4PProject, IRegistrationCompositeFigures composites) throws IOException {
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

}