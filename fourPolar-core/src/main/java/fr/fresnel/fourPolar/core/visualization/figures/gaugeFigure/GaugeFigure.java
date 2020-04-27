package fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.AngleGaugeType;

/**
 * A concrete implementation of the {@link IGaugeFigure}.
 */
class GaugeFigure implements IGaugeFigure {
    private final Image<RGB16> _image;
    private final AngleGaugeType _type;
    private final ICapturedImageFileSet _fileSet;
    private final GaugeFigureType _figureType;

    /**
     * Create an stick figure using the interface of colored {@link ISoIImage}.
     * 
     * @param type    is the type of the figure.
     * @param image   is the colored SoI image.
     * @param fileSet is the captured file set this stick image corresponds to.
     */
    public GaugeFigure(GaugeFigureType figureType, AngleGaugeType type, Image<RGB16> image, ICapturedImageFileSet fileSet) {
        this._image = image;
        this._type = type;
        this._fileSet = fileSet;
        this._figureType = figureType;
    }

    @Override
    public AngleGaugeType getGaugeType() {
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

    @Override
    public GaugeFigureType getFigureType() {
        return this._figureType;
    }

}