package fr.fresnel.fourPolar.core.image.generic.AWTImageModel;

import java.awt.image.BufferedImage;
import java.util.stream.IntStream;

import fr.fresnel.fourPolar.core.image.generic.IPixelRandomAccess;
import fr.fresnel.fourPolar.core.image.generic.pixel.IPixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.Pixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;

/**
 * An implementation of random access for accessing bufferd images of uint16
 * type. Note that this iterator returns the same pixel instance in the
 * {@link #getPixel()} method.
 */
class UINT16PixelRandomAccess implements IPixelRandomAccess<UINT16> {
    private final AWTBufferedImage<UINT16> _bufferedImage;
    private int _numDim;

    /**
     * We cach the current plane requested by the user so that in iterations over
     * the same plane, we same some time.
     */
    private BufferedImage _cachedPlane;
    private int _cachedPlaneIndex;

    private final long[] _lastPosition;

    private long[] _position;

    private final Pixel<UINT16> _cachedPixel;

    private final int[] _bufferedImagePixelVal;

    public UINT16PixelRandomAccess(AWTBufferedImage<UINT16> bufferedImage) {
        _bufferedImage = bufferedImage;
        _numDim = bufferedImage.getMetadata().getDim().length;

        _lastPosition = new long[_numDim];
        
        _cachedPlaneIndex = 0;
        _cachedPlane = _bufferedImage.getImagePlane(_cachedPlaneIndex);

        _cachedPixel = new Pixel<UINT16>(new UINT16(0));
        _bufferedImagePixelVal = new int[1];
    }

    @Override
    public void setPosition(long[] position) {
        int newPlaneIndex = _bufferedImage.getPlaneNumber(position);
        _position = position;

        if (newPlaneIndex == _cachedPlaneIndex) {
            return;
        }

        _cachedPlaneIndex = newPlaneIndex;
        _cachedPlane = _bufferedImage.getImagePlane(newPlaneIndex);

    }


    @Override
    public void setPixel(IPixel<UINT16> pixel) throws ArrayIndexOutOfBoundsException {
        _cachedPlane.getRaster().setPixel((int) _position[0], (int) _position[1], new int[] { pixel.value().get() });
    }

    @Override
    public IPixel<UINT16> getPixel() throws ArrayIndexOutOfBoundsException {
        _cachedPlane.getRaster().getPixel((int)_position[0], (int)_position[1], _bufferedImagePixelVal);
        _cachedPixel.value().set(_bufferedImagePixelVal[0]);
        return _cachedPixel;
    }

}