package fr.fresnel.fourPolar.io.image.polarization;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;

/**
 * An interface for writing a {@link IPolarizationImageSet}. Note that several images can
 * be written with the same interface.
 */
public interface IPolarizationImageSetWriter {
    /**
     * Writes the image set to the destinations inherent in the {@link IPolarizationImageSet}.
     * @param imageSet
     */
    public void write(File root4PProject, IPolarizationImageSet imageSet) throws IOException;
    
    /**
     * Close all resources associated with this writer.
     */
    public void close() throws IOException;
    
}
