package fr.fresnel.fourPolar.core.visualization.figures.stickFigure;

import java.util.Objects;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;

/**
 * A concrete implementation of the {@link IStickFigure}.
 */
class StickFigure implements IStickFigure {
    private final Image<RGB16> _image;
    private final StickFigureType _type;
    private final ICapturedImageFileSet _fileSet;

    /**
     * Create an stick figure using the interface of colored {@link ISoIImage}.
     * 
     * @param type    is the type of the figure.
     * @param image   is the colored SoI image.
     * @param fileSet is the captured file set this stick image corresponds to.
     */
    public StickFigure(StickFigureType type, Image<RGB16> image, ICapturedImageFileSet fileSet) {
        this._image = image;
        this._type = type;
        this._fileSet = fileSet;
    }

    @Override
    public StickFigureType getType() {
        return _type;
    }

    @Override
    public Image<RGB16> getImage() {
        return _image;
    }

    @Override
    public ICapturedImageFileSet getFileSet() {
        return _fileSet;
    }

}