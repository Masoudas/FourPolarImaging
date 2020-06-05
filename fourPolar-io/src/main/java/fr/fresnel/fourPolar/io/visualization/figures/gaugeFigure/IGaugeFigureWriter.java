package fr.fresnel.fourPolar.io.visualization.figures.gaugeFigure;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.IGaugeFigure;

public interface IGaugeFigureWriter {
    /**
     * Write the given composites to the folder designated to the processed (bead)
     * images, as defined by
     * {@link PathFactoryOfProject#getFolder_ProcessedBeadImages}.
     * 
     * @param root4PProject        is the location of the 4Polar folder of the
     *                             project {@see PathFactoryOfProject}.
     * @param visualizationSession is the name of the visualization session to which
     *                             this gauge figure belong.
     * @param capturedImageFileSet is the captured image set to which this gauge
     *                             figure belong.
     * @throws IOException thrown in case of low-level problems.
     * 
     * @return the composites for the given channel number.
     */
    public void write(File root4PProject, String visualizationSession, IGaugeFigure gaugeFigure);

    /**
     * Close all resources associated with this writer.
     */
    public void close() throws IOException;

}