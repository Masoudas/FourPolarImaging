package fr.fresnel.fourPolar.core.image.generic.AWTImageModel;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.IPixelRandomAccess;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.AWTImageModel.type.BufferedImageTypes;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;

public class UINT16BufferedImage extends AWTBufferedImage<UINT16> {

    protected UINT16BufferedImage(IMetadata metadata, ImageFactory factory) {
        super(metadata, BufferedImageTypes.TYPE_USHORT_GRAY, factory);
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