package fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.AngleGaugeType;

/**
 * An interface to model a gauge figure.
 */
public interface IGaugeFigure {
    /**
     * Returns the type of the gauge figure.
     */
    public AngleGaugeType getType();

    /**
     * Returns the underlying {@link Image}.
     * @return
     */
    public Image<RGB16> getImage();

    /**
     * Returns the {@link ICapturedImageFileSet} this figure corresponds to.
     */
    public ICapturedImageFileSet getFileSet();

}