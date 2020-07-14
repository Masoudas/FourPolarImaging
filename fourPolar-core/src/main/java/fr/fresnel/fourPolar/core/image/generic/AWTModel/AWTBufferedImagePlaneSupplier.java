package fr.fresnel.fourPolar.core.image.generic.AWTModel;

import java.awt.image.BufferedImage;
import fr.fresnel.fourPolar.core.image.ImagePlaneSupplier;
import fr.fresnel.fourPolar.core.image.generic.AWTModel.type.BufferedImageTypes;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;

class AWTBufferedImagePlaneSupplier implements ImagePlaneSupplier<BufferedImage> {
    private final int _xdim;
    private final int _ydim;
    private final BufferedImageTypes _bufferedImageType;

    /**
     * Initialize the supplier to provide instances of planes of the given x and y
     * size and pixel type.
     * 
     * @param xdim    is the x dimension of the plane.
     * @param ydim    is the y dimension of the plane.
     * @param pixelType is the pixel type this buffered image should correspond to.
     */
    public AWTBufferedImagePlaneSupplier(int xdim, int ydim, PixelType pixelType) {
        _xdim = xdim;
        _ydim = ydim;
        _bufferedImageType = _getBufferedImageTypeFromPixelType(pixelType);
    }

    private BufferedImageTypes _getBufferedImageTypeFromPixelType(PixelType pixelType) {
        return BufferedImageTypes.convertPixelTypes(pixelType.getType());
    }

    @Override
    public BufferedImage get() {
        return new BufferedImage(_xdim, _ydim, _bufferedImageType.getBufferedType());
    }

}