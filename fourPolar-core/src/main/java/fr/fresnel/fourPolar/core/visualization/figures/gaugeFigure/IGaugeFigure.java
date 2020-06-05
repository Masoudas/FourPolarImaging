package fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.AngleGaugeType;

/**
 * An interface to model a gauge figure. A gague figure is an RGB16 image by
 * default, that has no channel, and it's axis order is XYCZT.
 */
public interface IGaugeFigure {
    /**
     * Axis order of the image.
     */
    public static final AxisOrder AXIS_ORDER = AxisOrder.XYCZT;

    /**
     * Returns the type of the gauge figure.
     */
    public AngleGaugeType getGaugeType();

    /**
     * Returns the type of the gauge figure.
     */
    public GaugeFigureType getFigureType();

    /**
     * Returns the underlying {@link Image}.
     * 
     * @return
     */
    public Image<RGB16> getImage();

    /**
     * Returns the {@link ICapturedImageFileSet} this figure corresponds to.
     */
    public ICapturedImageFileSet getFileSet();

    /**
     * Get channel number.
     */
    public int getChannel();

}