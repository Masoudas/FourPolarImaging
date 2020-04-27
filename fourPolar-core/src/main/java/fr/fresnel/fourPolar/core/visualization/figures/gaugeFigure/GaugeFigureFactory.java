package fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure;

import java.util.Objects;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.AngleGaugeType;

/**
 * The factory to generate a {@link IGaugeFigure}.
 */
public class GaugeFigureFactory {
    /**
     * Create a gauge figure with the provided image interface.
     * 
     * @param figureType     is the type of figure (representation) meant by this
     *                       figure.
     * @param angleGaugeType is the angle gauge type of this gauge figure.
     * @param image          is the {@link Image} interface of the figure.
     * @param fileSet        is the fileSet associated with this gauge figure.
     * @return an gauge figure.
     */
    public static IGaugeFigure create(GaugeFigureType figureType, AngleGaugeType angleGaugeType, Image<RGB16> image,
            ICapturedImageFileSet fileSet) {
        Objects.requireNonNull(angleGaugeType, "angleGaugeType cannot be null.");
        Objects.requireNonNull(image, "image cannot be null");
        Objects.requireNonNull(fileSet, "fileSet cannot be null");

        return new GaugeFigure(figureType, angleGaugeType, image, fileSet);
    }
}