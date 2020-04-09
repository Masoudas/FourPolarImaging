package fr.fresnel.fourPolar.core.visualization.figures.stickFigure;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.visualization.figures.stickFigure.stick.StickType;

/**
 * An interface to model a stick figure.
 */
public interface IStickFigure {
    /**
     * Returns the type of the stick figure.
     */
    public StickType getType();

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