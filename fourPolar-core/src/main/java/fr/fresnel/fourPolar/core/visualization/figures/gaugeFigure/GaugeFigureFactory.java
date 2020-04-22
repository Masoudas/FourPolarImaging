package fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure;

import java.util.Objects;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.image.polarization.soi.ISoIImage;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.AngleGaugeType;

/**
 * The factory to generate a {@link IGaugeFigure}.
 */
public class GaugeFigureFactory {
    /**
     * Create a gauge figure from an Image interface. This constructor should be
     * used when a previously generated gauge figure is read.
     * 
     * @param angleGaugeType is the angle gauge type of this gauge figure.
     * @param image          is the {@link Image} interface of the figure.
     * @return an gauge figure.
     */
    public static IGaugeFigure createExisting(AngleGaugeType angleGaugeType, Image<RGB16> image,
            ICapturedImageFileSet fileSet) {
        Objects.requireNonNull(image, "image cannot be null");
        Objects.requireNonNull(fileSet, "fileSet cannot be null");

        return new GaugeFigure(angleGaugeType, image, fileSet);
    }

    /**
     * Creates an empty gauge figure, which can be filled with sticks. Note that The
     * generated figure is in fact not empty, but rather has an RGB version of
     * {@link ISoIImage}. The sticks are drawn on top of this figure. Note that the
     * underlying {@link Image} uses the same {@link ImageFactory} (has the same
     * image implementation).
     * 
     * @param angleGaugeType is the angle gauge used in this figure.
     * @param type           is the desired type of gauge figure.
     * @param soiImage       is the corresponding {@link ISoIImage}.
     * @return an empty gauge figure.
     */
    public static IGaugeFigure createEmpty(AngleGaugeType angleGaugeType, ISoIImage soiImage,
            ICapturedImageFileSet fileSet) {
        Objects.requireNonNull(soiImage, "soiImage cannot be null");
        Objects.requireNonNull(fileSet, "fileSet cannot be null");

        Image<RGB16> image = soiImage.getImage().getFactory().create(soiImage.getImage().getDimensions(), RGB16.zero());

        return new GaugeFigure(angleGaugeType, image, fileSet);

    }
}