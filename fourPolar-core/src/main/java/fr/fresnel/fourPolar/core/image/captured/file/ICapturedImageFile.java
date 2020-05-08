package fr.fresnel.fourPolar.core.image.captured.file;

import java.io.File;

/**
 * An interface indicating the basic information associated with a captured image file.
 */
public interface ICapturedImageFile {
    /**
     * Returns the channels (in ascending order) this captured image is associated with.
     */
    public int[] channels();

    /**
     * The image file associated with this captured image.
     * @return
     */
    public File file();
}