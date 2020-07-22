package fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure;

import java.util.Objects;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.ARGB8;
import fr.fresnel.fourPolar.core.image.soi.ISoIImage;
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
     * @param channel        is the channel number.
     * @return a gauge figure.
     * 
     * @throws IllegalArgumentException is thrown in case the axis order of the
     *                                  given image is not the same as that of
     *                                  {@link ISoIImage.AXIS_ORDER}.
     */
    public static IGaugeFigure create(GaugeFigureLocalization figureType, AngleGaugeType angleGaugeType, Image<ARGB8> image,
            ICapturedImageFileSet fileSet, int channel) {
        Objects.requireNonNull(angleGaugeType, "angleGaugeType cannot be null.");
        Objects.requireNonNull(image, "image cannot be null");
        Objects.requireNonNull(fileSet, "fileSet cannot be null");

        if (image.getMetadata().axisOrder() != ISoIImage.AXIS_ORDER) {
            throw new IllegalArgumentException("The given soi image does not have the same axis order as ISoIImage.");
        }

        return new GaugeFigure(figureType, angleGaugeType, image, fileSet, channel);
    }
}