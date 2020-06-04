package fr.fresnel.fourPolar.io.visualization.figures.polarization;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.visualization.figures.polarization.IPolarizationImageSetComposites;

/**
 * An interface for reading the {@link IPolarizationImageSetComposites} from the
 * disk.
 */
public interface IPolarizationImageSetCompositesReader {
    /**
     * Read the composites of registration image corresponding to the given channel,
     * with the path defined by
     * {@link PathFactoryOfProject#getFolder_ProcessedBeadImages}.
     * 
     * @param root4PProject is the location of the 4Polar folder of the project
     *                      {@see PathFactoryOfProject}.
     * @param channel       is the channel number.
     * @throws IOException thrown in case of low-level problems.
     * 
     * @return the composites for the given channel number.
     */
    public IPolarizationImageSetComposites readRegistrationComposite(File root4PProject, int channel)
            throws IOException;

    /**
     * Read the composite corresponding to the given fileSet from the given
     * visualization session.
     * 
     * @return
     * @throws IOException
     */
    public IPolarizationImageSetComposites read(File root4PProject, String visualizationSession,
            int channel, ICapturedImageFileSet fileSet) throws IOException;

    /**
     * Close all resources associated with this reader.
     * 
     * @throws IOException
     */
    public void close() throws IOException;
}