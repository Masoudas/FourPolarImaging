package fr.fresnel.fourPolar.core.image.generic.AWTModel;

import java.awt.image.BufferedImage;

import fr.fresnel.fourPolar.core.image.generic.AWTModel.type.BufferedImageTypes;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;

/**
 * Models a plane of the {@link AWTBufferedImage}. The plane index starts from
 * zero.
 */
class AWTBufferedImagePlane {
    final private int _planeIndex;
    final private BufferedImage _imagePlane;

    public AWTBufferedImagePlane(int planeIndex, int xdim, int ydim, PixelType pixelType) {
        assert planeIndex > 0 : "planeIndex should be greater than zero";
        assert xdim > 0 && ydim > 0 : "image dimension should be greater than zero";
        assert pixelType != null : "imageType should not be null";

        _planeIndex = planeIndex;
        _imagePlane = _createBufferedImage(xdim, ydim, pixelType);
    }

    public BufferedImage _createBufferedImage(int xdim, int ydim, PixelType pixelType) {
        BufferedImageTypes bufferedImageType = BufferedImageTypes.convertPixelTypes(pixelType.getType());
        return new BufferedImage(xdim, ydim, bufferedImageType.getBufferedType());
    }

    /**
     * @return the plane index associated with this image.
     */
    public int planeIndex() {
        return _planeIndex;
    }

    /**
     * @return the underlying image.
     */
    public BufferedImage getImage() {
        return _imagePlane;
    }
}