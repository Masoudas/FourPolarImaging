package fr.fresnel.fourPolar.core.visualization.figures.stickFigure;

import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.image.polarization.soi.ISoIImage;

/**
 * The factory to generate a {@link IStickFigure}.
 */
public class StickFigureFactory {
    /**
     * Create a stick figure from an Image interface. This constructor should
     * be used when a previously generated stick figure is read.
     * 
     * @param type is the type of this stick figure.
     * @param image is the {@link Image} interface of the figure.
     * @return an stick figure.
     */
    public static IStickFigure createExisting(StickFigureType type, Image<RGB16> image) {
        return new StickFigure(type, image);
    }

    public static IStickFigure createEmpty(StickFigureType type, ISoIImage soiImage) {
        
    }
}