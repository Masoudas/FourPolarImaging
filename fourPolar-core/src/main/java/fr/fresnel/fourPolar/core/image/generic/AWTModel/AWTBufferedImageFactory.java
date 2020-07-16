package fr.fresnel.fourPolar.core.image.generic.AWTModel;

import java.awt.image.BufferedImage;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;

/**
 * Creates an image interface implemented using the {@link BufferedImage} of AWT
 * as backend.
 */
public class AWTBufferedImageFactory implements ImageFactory {

    @SuppressWarnings("unchecked")
    @Override
    public <T extends PixelType> Image<T> create(IMetadata metadata, T pixelType) throws IllegalArgumentException {
        switch (pixelType.getType()) {
            case UINT_16:
                return (Image<T>) new UINT16BufferedImage(metadata, this, UINT16.zero());

            default:
                throw new IllegalArgumentException("No implementation was found for the requested type.");
        }
    }

    /**
     * Construct the image directly by providing the planes. Planes are indexed in
     * the order they're provided.
     * 
     * @param <T>       is the pixel type
     * @param metadata  is the metadata of the image.
     * @param pixelType is the pixel type of the image.
     * @param planes    are the planes.
     * @return an image instance with buffered image as backend.
     * 
     * @throws IllegalArgumentException if a plane does not have the same dimension
     *                                  as given by the metadata, or if number of
     *                                  planes in metadata does not match the number
     *                                  of planes,
     */
    @SuppressWarnings("unchecked")
    public <T extends PixelType> Image<T> createFromPlanes(IMetadata metadata, T pixelType, BufferedImage[] planes) {
        switch (pixelType.getType()) {
            case UINT_16:
                return (Image<T>) new UINT16BufferedImage(metadata, this, UINT16.zero(), planes);

            default:
                throw new IllegalArgumentException("No implementation was found for the requested type.");
        }
    }

}