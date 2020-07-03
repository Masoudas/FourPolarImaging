package fr.fresnel.fourPolar.core.image.generic.AWTImageModel;

import java.awt.image.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.IPixelRandomAccess;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.AWTImageModel.type.BufferedImageTypes;
import fr.fresnel.fourPolar.core.image.generic.metadata.MetadataUtil;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;

/**
 * An abstract class for implementing the image interface using
 * {@link BufferedImage}. This class shall be extended for each
 * {@link PixelType}. Note that buffered images are implemented as an array,
 * where each element of the array corresponds to one plane, as discussed
 * further in {@link #_createBuffreredImageArray()}.
 * 
 * @param <T> is the pixel type.
 */
abstract class AWTBufferedImage<T extends PixelType> implements Image<T> {
    protected final BufferedImage[] _images;
    protected final IMetadata _metadata;
    private final ImageFactory _factory;

    /**
     * A helper vector indicating how many planes are present per dimension. For
     * example, if dim = [2, 2, 3, 4], 3 planes are present for the 3rd dim and 12
     * for the fourth.
     */
    private final long[] _nPlanesPerDim;

    protected AWTBufferedImage(IMetadata metadata, BufferedImageTypes imageType, ImageFactory factory) {
        _images = _createBuffreredImageArray(metadata, imageType);
        _metadata = metadata;
        _nPlanesPerDim = _nPlanesPerDim();
        _factory = factory;

    }

    /**
     * Create a buffered image for each plane of the given dimension. Assume that
     * image is 2 * 2 * 3 * 2. Then an array of size 3 * 2 is created, where each
     * buffered image would be 2 * 2 in size.
     * 
     * @param dim is the dimension of the image.
     * @return an array of buffered image belonging to each plane.
     */
    private BufferedImage[] _createBuffreredImageArray(IMetadata metadata, BufferedImageTypes imageType) {
        int numPlanes = MetadataUtil.getNPlanes(metadata);
        int xdim = (int) metadata.getDim()[0];
        int ydim = (int) metadata.getDim()[0];

        BufferedImage[] _images = new BufferedImage[numPlanes];

        IntStream.range(0, _images.length)
                .forEach(i -> _images[i] = new BufferedImage(xdim, ydim, imageType.getBufferedType()));
        return _images;
    }

    /**
     * Returns the plane that is associated with this plane dimension. Example:
     * Suppose image is XYCZT and [2, 2, 1, 3, 2] and we write [0, 2, 1]. Then the
     * plane that is returned is the plane number 1 * 2 * 1.
     */
    public BufferedImage _getImagePlane(long... planeDim) {
        if (planeDim.length != _images.length) {
            throw new IllegalArgumentException("planeDim length should be equal to image dimension - 2");
        }

        long[] dim = _metadata.getDim();
        if (IntStream.range(0, planeDim.length).anyMatch(i -> planeDim[i] > dim[i + 2])) {
            throw new IllegalArgumentException("plane does not exist.");
        }

        int offSet = (int) planeDim[0];
        int planeIndex = offSet + (int) IntStream.range(0, planeDim.length)
                .mapToLong(i -> (planeDim[i] + 1) * _nPlanesPerDim[i - 1]).sum();
        return _images[planeIndex];

    }

    private long[] _nPlanesPerDim() {
        if (_metadata.getDim().length <= 2) {
            return new long[] { 1 };
        }

        long[] dim = _metadata.getDim();
        long[] nPlanesPerDim = new long[dim.length - 2];

        nPlanesPerDim[0] = dim[2];
        for (int i = 1; i < nPlanesPerDim.length; i++) {
            nPlanesPerDim[i] = nPlanesPerDim[i - 1] * dim[i];
        }

        return nPlanesPerDim;
    }

    @Override
    abstract public IPixelCursor<T> getCursor();

    @Override
    abstract public IPixelCursor<T> getCursor(long[] bottomCorner, long[] len) throws IllegalArgumentException;

    @Override
    abstract public IPixelRandomAccess<T> getRandomAccess();

    @Override
    public ImageFactory getFactory() {
        return _factory;
    }

    @Override
    public IMetadata getMetadata() {
        return _metadata;
    }

}