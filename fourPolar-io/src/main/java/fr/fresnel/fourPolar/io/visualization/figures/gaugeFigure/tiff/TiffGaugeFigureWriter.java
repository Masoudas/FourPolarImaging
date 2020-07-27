package fr.fresnel.fourPolar.io.visualization.figures.gaugeFigure.tiff;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.ARGB8;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.GaugeFigure;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.IGaugeFigure;
import fr.fresnel.fourPolar.io.exceptions.visualization.gaugeFigure.GaugeFigureIOException;
import fr.fresnel.fourPolar.io.image.generic.ImageWriter;
import fr.fresnel.fourPolar.io.image.generic.tiff.TiffImageWriterFactory;
import fr.fresnel.fourPolar.io.visualization.figures.gaugeFigure.IGaugeFigureWriter;

public class TiffGaugeFigureWriter implements IGaugeFigureWriter {
    private static final String _LOW_LEVEL_IO_MESSAGE = "Can't write the gauge figure due to low-level IO issues";

    private ImageWriter<ARGB8> _writer;

    /**
     * Caches the image type supplied the last time. This would allow us to create
     * only one instance of writer if image type does not change.
     */
    private ImageFactory _cachedImageType;

    public TiffGaugeFigureWriter() {

    }

    @Override
    public void write(File root4PProject, String visualizationSession, IGaugeFigure gaugeFigure)
            throws GaugeFigureIOException {
        if (!(gaugeFigure instanceof GaugeFigure)) {
            throw new IllegalArgumentException(
                    "The given gauge figure is not a pixel figure, and can't be saved with this writer.");
        }
        this._createWriter(gaugeFigure);

        File pathToFigure = this._getPathToFigure(root4PProject, visualizationSession, gaugeFigure);
        this._createParentFolder(pathToFigure);

        try {
            _writeGaugeFigure(pathToFigure, (GaugeFigure) gaugeFigure);
        } catch (IOException e) {
            throw _createGaugeFigureIOException(visualizationSession, gaugeFigure, _LOW_LEVEL_IO_MESSAGE);
        }

    }

    private void _writeGaugeFigure(File pathToFigure, GaugeFigure gaugeFigure) throws IOException {
        this._writer.write(pathToFigure, gaugeFigure.getImage());
    }

    private File _getPathToFigure(File root4PProject, String visualizationSession, IGaugeFigure gaugeFigure) {
        return TiffGaugeFigurePathUtil.createGaugeFigurePath(root4PProject, visualizationSession, gaugeFigure);
    }

    private void _createParentFolder(File pathToFigure) {
        if (!pathToFigure.exists()) {
            pathToFigure.mkdirs();
        }
    }

    @Override
    public void close() throws IOException {
        this._writer.close();
    }

    /**
     * If image type has not changed, use the previous writer instance. Otherwise,
     * create a new one.
     */
    private void _createWriter(IGaugeFigure gaugeFigure) {
        ImageFactory factoryType = ((GaugeFigure) gaugeFigure).getImage().getFactory();

        if (factoryType != this._cachedImageType) {
            _writer = TiffImageWriterFactory.getWriter(factoryType, ARGB8.zero());

        }
    }

    private GaugeFigureIOException _createGaugeFigureIOException(String visualizationSession, IGaugeFigure gaugeFigure,
            String message) {
        return new GaugeFigureIOException(message, gaugeFigure, visualizationSession);
    }

}