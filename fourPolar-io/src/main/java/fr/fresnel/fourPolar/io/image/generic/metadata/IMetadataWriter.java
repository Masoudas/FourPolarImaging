package fr.fresnel.fourPolar.io.image.generic.metadata;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.io.exceptions.image.generic.metadata.MetadataIOIssues;

/**
 * An interface for writing {@link IMetadata}. This interface is intended for
 * writing metadata as a separate entity from the image this metadata is
 * associated with, as most image writer methods have internal methods for
 * writing their metadata.
 */
public interface IMetadataWriter {
    /**
     * Write the given metadata to the following root, and under the given name.
     * 
     * @param metadata is the metadata to be written to disk.
     * @param root     is the root folder of where the image should be written (must
     *                 not contain file name).
     * @param name     is the name under which metadata is written (must not contain
     *                 file extension).
     * 
     * @throws MetadataIOIssues in case the metadata can't be written to disk.
     */
    public void write(IMetadata metadata, File root, String name) throws MetadataIOIssues;

    /**
     * Closes all resources associated with this writer.
     * 
     * @throws IOException in case of io issues when closing resources.
     */
    public void close() throws IOException;

}