package fr.fresnel.fourPolar.core.visualization.figures.stickFigure;

import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;

/**
 * A concrete implementation of the {@link IStickFigure}.
 */
class StickFigure implements IStickFigure {
    private final Image<RGB16> _image;
    private final StickFigureType _type;

    public StickFigure(StickFigureType type, Image<RGB16> image) {
        this._image = image;
        this._type = type;
    }

    @Override
    public StickFigureType getType() {
        return _type;
    }

    @Override
    public Image<RGB16> getImage() {
        return _image;
    }

}