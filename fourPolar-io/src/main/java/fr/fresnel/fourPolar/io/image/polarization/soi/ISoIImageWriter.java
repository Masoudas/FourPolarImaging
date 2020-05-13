package fr.fresnel.fourPolar.io.image.polarization.soi;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.core.image.soi.ISoIImage;

/**
 * An interface for writing {@link ISoIImageWriter}
 */
public interface ISoIImageWriter {
    /**
     * Writes the image to the destination determined by the {@ISoIImageFile}.
     * @param imageSet
     */
    public void write(File rootFolder, ISoIImage soiImage) throws IOException;
    
    /**
     * Close all resources associated with this writer.
     */
    public void close() throws IOException;
    
} 