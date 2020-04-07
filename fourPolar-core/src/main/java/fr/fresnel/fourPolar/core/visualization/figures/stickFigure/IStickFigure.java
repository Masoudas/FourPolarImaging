package fr.fresnel.fourPolar.core.visualization.figures.stickFigure;

import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;

/**
 * An interface to model a stick figure.
 */
public interface IStickFigure {
    /**
     * Returns the type of the stick figure.
     */
    public StickFigureType getType();

    /**
     * Returns the underlying {@link Image}.
     * @return
     */
    public Image<RGB16> getImage();

}