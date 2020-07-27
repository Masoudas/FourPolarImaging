package fr.fresnel.fourPolar.io.visualization.figures.gaugeFigure.vector.svg;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import fr.fresnel.fourPolar.core.image.vector.VectorImageFactory;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.IGaugeFigure;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.vectorFigure.VectorGaugeFigure;
import fr.fresnel.fourPolar.io.exceptions.image.vector.VectorImageIOIssues;
import fr.fresnel.fourPolar.io.exceptions.visualization.gaugeFigure.GaugeFigureIOException;
import fr.fresnel.fourPolar.io.image.vector.VectorImageWriter;
import fr.fresnel.fourPolar.io.image.vector.svg.SVGVectorImageWriterFactory;
import fr.fresnel.fourPolar.io.visualization.figures.gaugeFigure.GaugeFigurePathUtil;
import fr.fresnel.fourPolar.io.visualization.figures.gaugeFigure.IGaugeFigureWriter;

/**
 * A concrete implementation of {@link IGaugeFigureWriter} that writes a vector
 * gauge figure to the root specified by
 * {@link GaugeFigurePathUtil#createRoot()} usign an instance of
 * {@link VectorImageWriter}.
 */
public class SVGVectorGaugeFigureWriter implements IGaugeFigureWriter {
    private VectorImageWriter _writer;

    /**
     * Caches the image type supplied the last time. This would allow us to create
     * only one instance of writer if image type does not change.
     */
    private VectorImageFactory _cachedImageType;

    @Override
    public void write(File root4PProject, String visualizationSession, IGaugeFigure gaugeFigure)
            throws GaugeFigureIOException {
        Objects.requireNonNull(root4PProject, "root4PProject can't be null");
        Objects.requireNonNull(visualizationSession, "visualizationSession can't be null");
        Objects.requireNonNull(gaugeFigure, "gaugeFigure can't be null");

        _createWriter(gaugeFigure);
        File rootFolder = _createGaugeFiguresRootFolder(root4PProject, visualizationSession, gaugeFigure);

        try {
            _writeGaugeFigure((VectorGaugeFigure) gaugeFigure, rootFolder);
        } catch (VectorImageIOIssues e) {
            throw _buildException(visualizationSession, gaugeFigure);
        }
    }

    @Override
    public void close() throws IOException {
        _writer.close();
    }

    private File _createGaugeFiguresRootFolder(File root4PProject, String visualizationSession,
            IGaugeFigure gaugeFigure) {
        File rootFolder = GaugeFigurePathUtil.createRoot(root4PProject, visualizationSession, gaugeFigure);
        if (!rootFolder.exists()) {
            rootFolder.mkdirs();
        }

        return rootFolder;
    }

    private void _writeGaugeFigure(VectorGaugeFigure gaugeFigure, File rootFolder) throws VectorImageIOIssues {
        _writer.write(rootFolder, _getImageName(gaugeFigure), gaugeFigure.getVectorImage());
    }

    private String _getImageName(IGaugeFigure gaugeFigure) {
        return GaugeFigurePathUtil.getGaugeFigureName(gaugeFigure);
    }

    /**
     * If image type has not changed, use the previous writer instance. Otherwise,
     * create a new one.
     */
    private void _createWriter(IGaugeFigure gaugeFigure) {
        VectorImageFactory factoryType = ((VectorGaugeFigure) gaugeFigure).getVectorImage().getFactory();

        if (factoryType != this._cachedImageType) {
            _writer = SVGVectorImageWriterFactory.getWriter(factoryType);
            _cachedImageType = factoryType;
        }
    }

    private GaugeFigureIOException _buildException(String visualizationSession, IGaugeFigure figure) {
        return new GaugeFigureIOException.Builder(GaugeFigureIOException._WRITE_ERR)
                .visualizationSession(visualizationSession).channel(figure.getChannel()).fileSet(figure.getFileSet())
                .angleGaugeType(figure.getGaugeType()).localization(figure.getLocalization()).build();
    }
}