package fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure;

import java.util.Objects;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.image.polarization.soi.ISoIImage;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.GaugeType;

/**
 * The factory to generate a {@link IGaugeFigure}.
 */
public class GaugeFigureFactory {
    /**
     * Create a stick figure from an Image interface. This constructor should be
     * used when a previously generated stick figure is read.
     * 
     * @param type  is the type of this stick figure.
     * @param image is the {@link Image} interface of the figure.
     * @return an stick figure.
     */
    public static IGaugeFigure createExisting(Image<RGB16> image, ICapturedImageFileSet fileSet) {
        Objects.requireNonNull(image, "image cannot be null");
        Objects.requireNonNull(fileSet, "fileSet cannot be null");

        return new GaugeFigure(GaugeType.Empty, image, fileSet);
    }

    /**
     * Creates an empty stick figure, which can be filled with sticks. Note that The
     * generated figure is in fact not empty, but rather has an RGB version of
     * {@link ISoIImage}. The sticks are drawn on top of this figure. Note that the
     * underlying {@link Image} uses the same {@link ImageFactory} (has the same
     * image implementation).
     * 
     * @param type     is the desired type of stick figure.
     * @param soiImage is the corresponding {@link ISoIImage}.
     * @return an empty stick figure.
     */
    public static IGaugeFigure createEmpty(
        GaugeType type, ISoIImage soiImage, ICapturedImageFileSet fileSet) {
        Objects.requireNonNull(type, "type cannot be null");
        Objects.requireNonNull(soiImage, "soiImage cannot be null");
        Objects.requireNonNull(fileSet, "fileSet cannot be null");

        Image<RGB16> image = soiImage.getImage().getFactory().create(
            soiImage.getImage().getDimensions(), RGB16.zero());

        return new GaugeFigure(type, image, fileSet);

    }
}