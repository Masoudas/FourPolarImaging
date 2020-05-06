package fr.fresnel.fourPolar.io.image.generic;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;

/**
 * An interface for reading the metadata of an image, without actually opening
 * the entire image.
 */
public interface IMetadataReader {
    /**
     * Read metadata of the image file.
     * 
     * @param imageFile
     * @return
     */
    public IMetadata read(File imageFile) throws IOException;

    /**
     * Close all resources associated with this reader.
     */
    public void close() throws IOException;

}