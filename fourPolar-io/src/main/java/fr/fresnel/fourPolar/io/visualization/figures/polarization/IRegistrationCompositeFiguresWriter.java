package fr.fresnel.fourPolar.io.visualization.figures.polarization;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.core.visualization.figures.polarization.IPolarizationImageSetComposites;

/**
 * An interface for writing the {@link IPolarizationImageSetComposites} to disk.
 */
public interface IRegistrationCompositeFiguresWriter {
    /**
     * Write the given composite in a visualization session.
     * 
     * @param root4PProject        is the location of the 4Polar folder of the
     *                             project {@see PathFactoryOfProject}.
     * @param visualizationSession is the visualization session name.
     * @param composites           are the composites of registered to be written to
     *                             disk.
     * @throws IOException thrown in case of low-level problems.
     */
    public void write(File root4PProject, String visualizationSession, IPolarizationImageSetComposites composites)
            throws IOException;

    /**
     * Write the given composites to the folder designated to the processed
     * registration images, as defined by
     * {@link PathFactoryOfProject#getFolder_ProcessedBeadImages}.
     * 
     * @param root4PProject
     * @param composites
     * @throws IOException
     */
    public void writeAsRegistrationComposite(File root4PProject, IPolarizationImageSetComposites composites)
            throws IOException;

    /**
     * Close all resources associated with this reader.
     * 
     * @throws IOException
     */
    public void close() throws IOException;
}