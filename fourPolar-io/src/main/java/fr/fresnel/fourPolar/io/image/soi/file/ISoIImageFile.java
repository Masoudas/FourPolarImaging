package fr.fresnel.fourPolar.io.image.soi.file;

import java.io.File;

/**
 * An interface for accessing the file corresponding to an {@link SoIImage}.
 */
public interface ISoIImageFile {
    /**
     * @return the image file path of this soi image.
     */
    public File getFile();

    /**
     * @return channel number associated with this soi image.
     */
    public int channel();
}