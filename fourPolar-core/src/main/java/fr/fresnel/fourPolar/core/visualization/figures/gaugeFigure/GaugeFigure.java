package fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.ARGB8;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.AngleGaugeType;

/**
 * A concrete implementation of the {@link IGaugeFigure}.
 */
class GaugeFigure implements IGaugeFigure {
    private final Image<ARGB8> _image;
    private final AngleGaugeType _type;
    private final ICapturedImageFileSet _fileSet;
    private final GaugeFigureLocalization _figureType;
    private final int _channel;

    /**
     * Create an stick figure using the interface of colored {@link ISoIImage}.
     * 
     * @param type    is the type of the figure.
     * @param image   is the colored SoI image.
     * @param fileSet is the captured file set this stick image corresponds to.
     */
    public GaugeFigure(GaugeFigureLocalization figureType, AngleGaugeType type, Image<ARGB8> image,
            ICapturedImageFileSet fileSet, int channel) {
        this._image = image;
        this._type = type;
        this._fileSet = fileSet;
        this._figureType = figureType;
        this._channel = channel;
    }

    @Override
    public AngleGaugeType getGaugeType() {
        return _type;
    }

    @Override
    public Image<ARGB8> getImage() {
        return _image;
    }

    @Override
    public ICapturedImageFileSet getFileSet() {
        return _fileSet;
    }

    @Override
    public GaugeFigureLocalization getFigureType() {
        return this._figureType;
    }

    @Override
    public int getChannel() {
        return this._channel;
    }

}