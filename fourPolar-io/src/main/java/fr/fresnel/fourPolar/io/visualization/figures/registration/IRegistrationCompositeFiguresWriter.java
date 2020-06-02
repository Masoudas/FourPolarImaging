package fr.fresnel.fourPolar.io.visualization.figures.registration;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.core.visualization.figures.registration.IRegistrationCompositeFigures;

/**
 * An interface for writing the {@link IRegistrationCompositeFigures} to disk.
 */
public interface IRegistrationCompositeFiguresWriter {
    /**
     * Write the given composites to the folder designated to the processed (bead)
     * images, as defined by {@see PathFactoryOfProject#}.
     * 
     * @param root4PProject is the location of the 4Polar folder of the project
     *                      {@see PathFactoryOfProject}.
     * @param composites    are the composites of registered to be written to disk.
     * @throws IOException thrown in case of low-level problems.
     */
    public void write(File root4PProject, IRegistrationCompositeFigures composites) throws IOException;

    /**
     * Close all resources associated with this reader.
     * 
     * @throws IOException
     */
    public void close() throws IOException;
}