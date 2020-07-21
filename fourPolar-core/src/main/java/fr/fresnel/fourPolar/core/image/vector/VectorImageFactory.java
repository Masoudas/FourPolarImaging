package fr.fresnel.fourPolar.core.image.vector;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;

/**
 * An interface for creating a {@link VectorImage}.
 */
public interface VectorImageFactory {
    /**
     * Create a new vector image based on the given metadata.
     * 
     * @param metadata is the image metadata.
     * @return the vector image.
     */
    public VectorImage create(IMetadata metadata);
}