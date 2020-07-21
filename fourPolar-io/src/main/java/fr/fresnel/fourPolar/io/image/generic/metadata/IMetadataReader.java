package fr.fresnel.fourPolar.io.image.generic.metadata;

import java.io.File;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.io.exceptions.image.generic.metadata.MetadataIOIssues;

/**
 * An interface for reading the metadata of an image, without actually opening
 * the entire image. Depending on the implementation, metadata can be read from
 * an image file (like png), or from a separate file (like text, yaml).
 */
public interface IMetadataReader {
    /**
     * Read metadata from the given path
     * 
     * @param root     is the folder where this metadata will be written (excluding
     *                 file name)
     * @param fileName is the name of the file containing the metadata (without extension).
     * 
     * @throws MetadataIOIssues in case the metadata can't be parsed. Note that
     *                          undefined axis does not raise this exception.
     * @return the metadata instance read from the file.
     */
    public IMetadata read(File root, String fileName) throws MetadataIOIssues;

    /**
     * Close all resources associated with this reader.
     */
    public void close() throws MetadataIOIssues;

}