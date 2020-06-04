package fr.fresnel.fourPolar.io.visualization.figures.registration;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.core.visualization.figures.polarization.IPolarizationImageSetComposites;

/**
 * An interface for reading the {@link IPolarizationImageSetComposites} from the
 * disk.
 */
public interface IRegistrationCompositeFiguresReader {
    /**
     * Write the given composites to the folder designated to the processed (bead)
     * images, as defined by
     * {@link PathFactoryOfProject#getFolder_ProcessedBeadImages}.
     * 
     * @param root4PProject is the location of the 4Polar folder of the project
     *                      {@see PathFactoryOfProject}.
     * @param channel       is the channel number.
     * @throws IOException thrown in case of low-level problems.
     * 
     * @return the composites for the given channel number.
     */
    public IPolarizationImageSetComposites read(File root4PProject, int channel) throws IOException;

    /**
     * Close all resources associated with this reader.
     * 
     * @throws IOException
     */
    public void close() throws IOException;
}