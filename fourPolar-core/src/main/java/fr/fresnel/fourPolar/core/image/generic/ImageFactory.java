package fr.fresnel.fourPolar.core.image.generic;

import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;

/**
 * Interface for factories of Image implementations.
 */
public interface ImageFactory {
    /**
     * Create an image, with the given dimensions and type. All {@link IMetadata}
     * fields would be set to default with this method.
     * 
     * @return
     */
    public <T extends PixelType> Image<T> create(long[] dim, T pixelType);

    /**
     * Create an image based on its metadata.
     * 
     * @param metadata is the metadata of the desired image.
     * 
     * @throws IllegalArgumentException if there's inconsitency between the metadata
     *                                  and pixelType
     */
    public <T extends PixelType> Image<T> create(IMetadata metadata) throws IllegalArgumentException;

}