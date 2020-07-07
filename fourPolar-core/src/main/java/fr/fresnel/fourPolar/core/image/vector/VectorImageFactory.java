package fr.fresnel.fourPolar.core.image.vector;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;

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

    /**
     * Create a new vector image, setting its metadata and background image
     * according to the given image interface. Note that the image is set in the
     * vector image using the {@link VectorImage#setImage()} method. Note that the
     * background image will be independent of the image interface. That is, later
     * changes in the image interface does not affect the background image.
     * 
     * @param <T>   is the pixel type.
     * @param image is the image interface to be used by the vector image.
     * @return a vector image whose background image is set to the given image
     *         interface.
     */
    public <T extends PixelType> VectorImage create(Image<T> image, T pixelType);

}