package fr.fresnel.fourPolar.core.image.generic.AWTModel;

import java.awt.image.BufferedImage;
import java.util.Objects;
import java.util.stream.IntStream;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.IPixelRandomAccess;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.metadata.MetadataUtil;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;

/**
 * An abstract class for implementing the image interface using
 * {@link BufferedImage}. This class shall be extended for each
 * {@link PixelType}.
 * <p>
 * Note that the system for accessing the buffered images is through a 1D array,
 * where each element of the array corresponds to one plane, as discussed
 * further in {@link #_createBuffreredImageArray()}.
 * 
 * @param <T> is the pixel type.
 */
abstract class AWTBufferedImage<T extends PixelType> implements Image<T> {
    protected final AWTBufferedImagePlane[] _images;
    protected final IMetadata _metadata;
    private final ImageFactory _factory;

    /**
     * A helper vector indicating how many planes are present per dimension. For
     * example, if dim = [2, 2, 3, 4], and given the first two axis indicate the the
     * xy plane, we conclude that 3 planes are present for the 3rd dim and 12 for
     * the fourth. Note that the first two elements are always set to zero for
     * convenience.
     */
    private final long[] _nPlanesPerDim;

    private final long _totalNumPlanes;

    private final long[] _imageDim;

    protected AWTBufferedImage(IMetadata metadata, ImageFactory factory, T pixelType) {
        Objects.requireNonNull(metadata, "Metadata can't be null");
        Objects.requireNonNull(factory, "factory can't be null");

        _metadata = metadata;
        _factory = factory;
        _imageDim = metadata.getDim();

        _nPlanesPerDim = _nPlanesPerDim();
        _images = _createBuffreredImageArray(metadata, pixelType);
        _totalNumPlanes = MetadataUtil.getNPlanes(metadata);

    }

    /**
     * Create a buffered image for each plane of the given dimension. Assume that
     * image is 2 * 2 * 3 * 2. Then an array of size 3 * 2 is created, where each
     * buffered image would be 2 * 2 in size.
     * 
     * @param dim is the dimension of the image.
     * @return an array of buffered image plane that belong to each plane.
     */
    private AWTBufferedImagePlane[] _createBuffreredImageArray(IMetadata metadata, PixelType pixelType) {
        int numPlanes = MetadataUtil.getNPlanes(metadata);
        int xdim = (int) metadata.getDim()[0];
        int ydim = (int) metadata.getDim()[0];

        AWTBufferedImagePlane[] _images = new AWTBufferedImagePlane[numPlanes];

        IntStream.range(0, _images.length).forEach(
                planeIndex -> _images[planeIndex] = new AWTBufferedImagePlane(planeIndex + 1, xdim, ydim, pixelType));
        return _images;
    }

    /**
     * Returns the buffered image of the demanded plane. Calculate plane number
     * using {@link #getPlaneNumber()}.
     */
    public AWTBufferedImagePlane getImagePlane(int planeNumber) {
        if (planeNumber < 1 || planeNumber > _totalNumPlanes) {
            throw new IllegalArgumentException(
                    "plane number should be greater than zero and less than total number of planes.");
        }
        return _images[planeNumber - 1];

    }

    private long[] _nPlanesPerDim() {
        if (_imageDim.length <= 2) {
            return new long[] { 1 };
        }


        // Create the same vector size. Note that by default, the first two dims
        // have zero planes.
        long[] nPlanesPerDim = new long[_imageDim.length];
        nPlanesPerDim[2] = _imageDim[2];
        for (int dim = 3; dim < nPlanesPerDim.length; dim++) {
            nPlanesPerDim[dim] = nPlanesPerDim[dim - 1] * _imageDim[dim];
        }

        return nPlanesPerDim;
    }

    /**
     * @return the plane number this position belongs to starting from 1. It's the
     *         responsibility of the caller to make sure that the position is in the
     *         image boundary.
     */
    public int getPlaneNumber(long[] position) {
        if (position.length != _imageDim.length)
        if (this._totalNumPlanes == 1) {
            return 1;
        }

        // One is added, because image planes start from one.
        int offSet = (int) position[2];
        return offSet + (int) IntStream.range(2, position.length)
                .mapToLong(i -> position[i] * _nPlanesPerDim[i - 1]).sum() + 1;

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