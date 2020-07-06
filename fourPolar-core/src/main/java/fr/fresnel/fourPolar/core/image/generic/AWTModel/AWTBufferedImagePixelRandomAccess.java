package fr.fresnel.fourPolar.core.image.generic.AWTModel;

import fr.fresnel.fourPolar.core.image.generic.IPixelRandomAccess;
import fr.fresnel.fourPolar.core.image.generic.pixel.IPixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;

/**
 * An abstract implementation of random access for accessing bufferd images
 * type. Note that this iterator returns the same pixel instance in the
 * {@link #getPixel()} method. All derived subclasses should correspond to one
 * {@link PixelType}, and their only job would be to implement
 * {@link #getPixel()} and {@link #setPixel()}.
 */
abstract class AWTBufferedImagePixelRandomAccess<T extends PixelType> implements IPixelRandomAccess<T> {
    /**
     * The buffered image this random access belongs to.
     */
    private final AWTBufferedImage<T> _bufferedImage;

    /**
     * We cache the current plane requested by the user to avoid calling the same
     * plane several times.
     */
    protected AWTBufferedImagePlane _cachedPlane;

    protected long[] _position;

    public AWTBufferedImagePixelRandomAccess(AWTBufferedImage<T> bufferedImage) {
        assert bufferedImage != null : "BufferedImage can't be null";

        _bufferedImage = bufferedImage;        
        _cachedPlane = _setFirstPlaneAsCachedPlane();

    }

    private AWTBufferedImagePlane _setFirstPlaneAsCachedPlane() {
        return _bufferedImage.getImagePlane(1);
    }

    @Override
    public void setPosition(long[] position) {
        _position = position;

        int newPlaneIndex = _bufferedImage.getPlaneNumber(position);
        if (newPlaneIndex == _cachedPlane.planeIndex()) {
            return;
        }

        _cachedPlane = _bufferedImage.getImagePlane(newPlaneIndex);
    }

    @Override
    public abstract void setPixel(IPixel<T> pixel);

    @Override
    public abstract IPixel<T> getPixel();

}