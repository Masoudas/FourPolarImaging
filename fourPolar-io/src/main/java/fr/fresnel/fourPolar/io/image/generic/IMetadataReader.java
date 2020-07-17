package fr.fresnel.fourPolar.io.image.generic;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.io.exceptions.image.generic.metadata.MetadataParseError;

/**
 * An interface for reading the metadata of an image, without actually opening
 * the entire image. Depending on the implementation, metadata can be read from
 * an image file (like png), or from a separate file (like text, yaml).
 */
public interface IMetadataReader {
    /**
     * Read metadata from the given path
     * 
     * @param path is the path to metadata source.
     * 
     * @throws MetadataParseError in case the metadata can't be parsed. Note that
     *                            undefined axis does not raise this exception.
     * @return the metadata of the image.
     */
    public IMetadata read(File path) throws MetadataParseError;

    /**
     * Close all resources associated with this reader.
     */
    public void close() throws IOException;

}