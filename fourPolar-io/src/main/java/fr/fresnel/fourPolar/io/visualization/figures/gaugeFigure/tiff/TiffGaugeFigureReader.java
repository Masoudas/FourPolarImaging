package fr.fresnel.fourPolar.io.visualization.figures.gaugeFigure.tiff;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.ARGB8;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.GaugeFigure;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.GaugeFigureLocalization;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.IGaugeFigure;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.AngleGaugeType;
import fr.fresnel.fourPolar.io.exceptions.visualization.gaugeFigure.GaugeFigureIOException;
import fr.fresnel.fourPolar.io.image.generic.ImageReader;
import fr.fresnel.fourPolar.io.image.generic.tiff.TiffImageReaderFactory;
import fr.fresnel.fourPolar.io.visualization.figures.gaugeFigure.IGaugeFigureReader;

/**
 * A concrete implementation of {@link IGaugeFigureReader} that reads the images
 * as tif.
 */
public class TiffGaugeFigureReader implements IGaugeFigureReader {
    private AngleGaugeType _angleGaugeType;
    private GaugeFigureLocalization _gaugeFigureLocalization;

    private ImageReader<ARGB8> _reader;

    /**
     * Initializes the reader to read a particular form of gauge figure.
     * 
     * @param imageFactory    is the desired image model to be used.
     * @param gaugeFigureType is the type of gauge figure to be read.
     * @param angleGaugeType  is the type of angle gauge associated with the gauge
     *                        figure.
     */
    public TiffGaugeFigureReader(ImageFactory imageFactory, GaugeFigureLocalization gaugeFigureType,
            AngleGaugeType angleGaugeType) {
        this._reader = TiffImageReaderFactory.getReader(imageFactory, ARGB8.zero());
        this._gaugeFigureLocalization = gaugeFigureType;
        this._angleGaugeType = angleGaugeType;
    }

    @Override
    public IGaugeFigure read(File root4PProject, String visualizationSession, int channel,
            ICapturedImageFileSet capturedImageFileSet) throws GaugeFigureIOException {
        File pathToFigure = this._getPathToFigure(root4PProject, visualizationSession, channel, capturedImageFileSet);

        try {
            Image<ARGB8> gaugeFigure = this._readGaugeFigure(pathToFigure);
            return GaugeFigure.create(this._gaugeFigureLocalization, this._angleGaugeType, gaugeFigure,
                    capturedImageFileSet, channel);
        } catch (IOException e) {
            throw _createGaugeFigureIOException(visualizationSession, channel, capturedImageFileSet,
                    GaugeFigureIOException._READ_ERR);
        }

    }

    private GaugeFigureIOException _createGaugeFigureIOException(String visualizationSession, int channel,
            ICapturedImageFileSet capturedImageFileSet, String message) {
        GaugeFigureIOException ioException = new GaugeFigureIOException(message);
        ioException.setAngleGaugeType(_angleGaugeType);
        ioException.setChannel(channel);
        ioException.setFileSet(capturedImageFileSet);
        ioException.setLocalization(_gaugeFigureLocalization);
        ioException.setVisualizationSession(visualizationSession);
        return ioException;
    }

    @Override
    public void close() throws IOException {
        this._reader.close();

    }

    private File _getPathToFigure(File root4PProject, String visualizationSession, int channel,
            ICapturedImageFileSet capturedImageFileSet) {
        return TiffGaugeFigurePathUtil.createGaugeFigurePath(root4PProject, visualizationSession, channel,
                capturedImageFileSet, this._gaugeFigureLocalization, this._angleGaugeType);
    }

    private Image<ARGB8> _readGaugeFigure(File pathToFigure) throws IOException {
        return this._reader.read(pathToFigure);
    }

    @Override
    public void setLocalization(GaugeFigureLocalization localization) {
        _gaugeFigureLocalization = localization;
    }

    @Override
    public void setAngleGaugeType(AngleGaugeType gaugeType) {
        _angleGaugeType = gaugeType;
    }
}