package fr.fresnel.fourPolar.core.image.generic;

import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;

/**
 * Interface for factories of Image implementations.
 */
public interface ImageFactory {
    /**
     * Create an image, with the given dimensions and type. All {@link IMetadata}
     * fields would be set to default.
     * 
     * @return
     */
    public <T extends PixelType> Image<T> create(long[] dim, T pixelType);


    public <T extends PixelType> Image<T> create(long[] dim, T pixelType, IMetadata metadata);

}