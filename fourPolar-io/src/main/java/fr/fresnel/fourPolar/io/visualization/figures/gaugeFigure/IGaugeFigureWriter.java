package fr.fresnel.fourPolar.io.visualization.figures.gaugeFigure;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.IGaugeFigure;
import fr.fresnel.fourPolar.io.exceptions.visualization.gaugeFigure.GaugeFigureIOException;

/**
 * An interface for writing a {@link IGaugeFigure} to disk. The root folder for
 * where the gauge figure is written is constructed using
 * {@link GaugeFigurePathUtil#createGaugeFigurePath()} and the file name using
 * {@link GaugeFigurePathUtil#getGaugeFigureName()}.
 * 
 */
public interface IGaugeFigureWriter {
    /**
     * Write the given gauge figure.
     * 
     * @param root4PProject        is the location of the 4Polar folder of the
     *                             project {@see PathFactoryOfProject}.
     * @param visualizationSession is the name of the visualization session to which
     *                             this gauge figure belong.
     * @param capturedImageFileSet is the captured image set to which this gauge
     *                             figure belong.
     * @throws GaugeFigureIOException thrown in case of IO issues.
     * 
     * @return the composites for the given channel number.
     */
    public void write(File root4PProject, String visualizationSession, IGaugeFigure gaugeFigure)
            throws GaugeFigureIOException;

    /**
     * Close all resources associated with this writer.
     */
    public void close() throws IOException;

}