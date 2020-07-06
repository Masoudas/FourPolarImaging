package fr.fresnel.fourPolar.core.image.generic.AWTModel;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;

public class BufferedImageFactory implements ImageFactory {

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

}