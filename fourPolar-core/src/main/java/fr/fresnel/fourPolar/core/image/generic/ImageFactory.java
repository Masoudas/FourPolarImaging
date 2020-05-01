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
     * Create an image, with the given dimensions, type and metadata.
     * 
     * @param dim       is the dimension of the image.
     * @param pixelType is the pixel type of the image
     * @param metadata  is the metadata of the image.

     * @throws IllegalArgumentException if there's inconsitency between the metadata
     *                                  and dimension.
     */
    public <T extends PixelType> Image<T> create(long[] dim, T pixelType, IMetadata metadata)
            throws IllegalArgumentException;

}