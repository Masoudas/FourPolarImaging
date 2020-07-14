package fr.fresnel.fourPolar.core.image.generic.AWTModel;

import fr.fresnel.fourPolar.core.image.generic.pixel.IPixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.Pixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;

/**
 * An implementation of random access for accessing bufferd images of uint16
 * type.
 */
class UINT16PixelRandomAccess extends AWTBufferedImagePixelRandomAccess<UINT16> {
    /**
     * A cached pixel that will be returned to the user when asked for pixel value.
     */
    private final Pixel<UINT16> _cachedPixel;

    /**
     * A cached vector used for setting the pixel value through the raster of
     * buffered image.
     */
    private final int[] _bufferedImagePixelVal;

    public UINT16PixelRandomAccess(AWTBufferedImage<UINT16> bufferedImage) {
        super(bufferedImage);
        _cachedPixel = new Pixel<UINT16>(new UINT16(0));
        _bufferedImagePixelVal = new int[1];
    }

    @Override
    public void setPixel(IPixel<UINT16> pixel) throws ArrayIndexOutOfBoundsException {
        _cachedPlane.getPlane().getRaster().setPixel((int) _position[0], (int) _position[1],
                new int[] { pixel.value().get() });
    }

    @Override
    public IPixel<UINT16> getPixel() throws ArrayIndexOutOfBoundsException {
        _cachedPlane.getPlane().getRaster().getPixel((int) _position[0], (int) _position[1], _bufferedImagePixelVal);
        _cachedPixel.value().set(_bufferedImagePixelVal[0]);
        return _cachedPixel;
    }

}