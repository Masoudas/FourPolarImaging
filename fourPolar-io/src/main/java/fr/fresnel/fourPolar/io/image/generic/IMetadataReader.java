package fr.fresnel.fourPolar.io.image.generic;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.io.exceptions.image.generic.metadata.MetadataParseError;

/**
 * An interface for reading the metadata of an image, without actually opening
 * the entire image.
 */
public interface IMetadataReader {
    /**
     * Read metadata of the image file.
     * 
     * @param imageFile
     * 
     * @throws MetadataParseError in case the metadata of the image can't be parsed.
     *                            Note that undefined axis does not raise this
     *                            exception.
     * @throws IOException        in case of low level IO issues.
     * @return the metadata of the image.
     */
    public IMetadata read(File imageFile) throws IOException, MetadataParseError;

    /**
     * Close all resources associated with this reader.
     */
    public void close() throws IOException;

}