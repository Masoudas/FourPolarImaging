package fr.fresnel.fourPolar.io.visualization.figures.gaugeFigure.vector.svg;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.vector.VectorImage;
import fr.fresnel.fourPolar.core.image.vector.VectorImageFactory;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.GaugeFigureLocalization;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.IGaugeFigure;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.AngleGaugeType;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.vectorFigure.VectorGaugeFigure;
import fr.fresnel.fourPolar.io.exceptions.visualization.gaugeFigure.GaugeFigureIOException;
import fr.fresnel.fourPolar.io.image.vector.VectorImageReader;
import fr.fresnel.fourPolar.io.image.vector.svg.SVGVectorImageReaderFactory;
import fr.fresnel.fourPolar.io.visualization.figures.gaugeFigure.GaugeFigurePathUtil;
import fr.fresnel.fourPolar.io.visualization.figures.gaugeFigure.IGaugeFigureReader;

/**
 * A concrete implementation of {@link IGaugeFigureWriter} that reads a vector
 * gauge figure from the root specified by
 * {@link GaugeFigurePathUtil#createRoot()} using an instance of
 * {@link VectorImageReader}.
 */
public class SVGVectorGaugeFigureReader implements IGaugeFigureReader {
    private AngleGaugeType _angleGaugeType;
    private GaugeFigureLocalization _gaugeFigureLocalization;

    private final VectorImageReader _reader;

    public SVGVectorGaugeFigureReader(VectorImageFactory factory, GaugeFigureLocalization localization,
            AngleGaugeType angleGaugeType) {
        this._reader = SVGVectorImageReaderFactory.getReader(factory);
        this._gaugeFigureLocalization = localization;
        this._angleGaugeType = angleGaugeType;
    }

    @Override
    public void setLocalization(GaugeFigureLocalization localization) {
        Objects.requireNonNull(localization, "localization can't be null");
        _gaugeFigureLocalization = localization;
    }

    @Override
    public void setAngleGaugeType(AngleGaugeType gaugeType) {
        Objects.requireNonNull(gaugeType, "gaugeType can't be null");
        _angleGaugeType = gaugeType;
    }

    @Override
    public IGaugeFigure read(File root4PProject, String visualizationSession, int channel,
            ICapturedImageFileSet capturedImageFileSet) throws GaugeFigureIOException {
        try {
            VectorImage gaugeImage = _readGaugeFigure(root4PProject, visualizationSession, channel,
                    capturedImageFileSet);
            return _createGaugeFigure(gaugeImage, capturedImageFileSet, channel);
        } catch (IOException e) {
            throw _buildGaugeFigureIOException(visualizationSession, channel, capturedImageFileSet);
        }

    }

    /**
     * Create the root of gauge figures for this channel of the set.
     */
    private File _getPathToRoot(File root4PProject, String visualizationSession, int channel,
            ICapturedImageFileSet capturedImageFileSet) {
        return GaugeFigurePathUtil.createRoot(root4PProject, visualizationSession, channel, capturedImageFileSet);
    }

    private String _getImageName() {
        return GaugeFigurePathUtil.getGaugeFigureName(_gaugeFigureLocalization, _angleGaugeType);
    }

    private VectorImage _readGaugeFigure(File root4PProject, String visualizationSession, int channel,
            ICapturedImageFileSet capturedImageFileSet) throws IOException {
        File rootFolder = _getPathToRoot(root4PProject, visualizationSession, channel, capturedImageFileSet);
        String imageName = _getImageName();
        return _reader.read(rootFolder, imageName);
    }

    private VectorGaugeFigure _createGaugeFigure(VectorImage gaugeImage, ICapturedImageFileSet fileSet, int channel) {
        return VectorGaugeFigure.create(_gaugeFigureLocalization, _angleGaugeType, gaugeImage, fileSet, channel);
    }

    @Override
    public void close() throws IOException {
        _reader.close();
    }

    private GaugeFigureIOException _buildGaugeFigureIOException(String visualizationSession, int channel,
            ICapturedImageFileSet capturedImageFileSet) {
        return new GaugeFigureIOException.Builder(GaugeFigureIOException._READ_ERR)
                .visualizationSession(visualizationSession).channel(channel).fileSet(capturedImageFileSet)
                .angleGaugeType(_angleGaugeType).localization(_gaugeFigureLocalization).build();
    }
}