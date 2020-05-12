package fr.fresnel.fourPolar.core.image.generic;

import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;

/**
 * Interface for factories of Image implementations.
 */
public interface ImageFactory {
    /**
     * Create an image based on its metadata.
     * 
     * @param metadata is the metadata of the desired image.
     * 
     * @throws IllegalArgumentException if there's inconsitency between the metadata
     *                                  and pixelType
     */
    public <T extends PixelType> Image<T> create(IMetadata metadata, T pixelType) throws IllegalArgumentException;

}