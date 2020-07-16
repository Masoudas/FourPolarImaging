package fr.fresnel.fourPolar.core.image.generic.AWTModel;

import java.awt.image.BufferedImage;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.IPixelRandomAccess;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;

class UINT16BufferedImage extends AWTBufferedImage<UINT16> {
    protected UINT16BufferedImage(IMetadata metadata, ImageFactory factory, UINT16 pixelType) {
        super(metadata, factory, pixelType);
    }

    /**
     * Construct by directly providing the image planes.
     * 
     * @param metadata    is the metadata of the image.
     * @param factory     is the factory instance.
     * @param pixelType   is the pixel type associated with the image.
     * @param imagePlanes are the image planes.
     * 
     * @throws IllegalArgumentException if a plane does not have the same dimension
     *                                  as given by the metadata.
     */
    protected UINT16BufferedImage(IMetadata metadata, ImageFactory factory, UINT16 pixelType, BufferedImage[] planes) {
        super(metadata, factory, pixelType);
    }

    @Override
    public IPixelCursor<UINT16> getCursor() {
        throw new UnsupportedOperationException("No cursor is defined for buffered image implementation.");
    }

    @Override
    public IPixelCursor<UINT16> getCursor(long[] bottomCorner, long[] len) throws IllegalArgumentException {
        throw new UnsupportedOperationException("No interval cursor is defined for buffered image implementation.");
    }

    @Override
    public IPixelRandomAccess<UINT16> getRandomAccess() {
        return new UINT16PixelRandomAccess(this);
    }

}