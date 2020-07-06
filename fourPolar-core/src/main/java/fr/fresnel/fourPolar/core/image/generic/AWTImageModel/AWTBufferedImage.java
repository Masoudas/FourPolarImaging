package fr.fresnel.fourPolar.core.image.generic.AWTImageModel;

import java.awt.image.BufferedImage;
import java.util.stream.IntStream;

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

    protected AWTBufferedImage(IMetadata metadata, BufferedImageTypes imageType, ImageFactory factory) {
        _images = _createBuffreredImageArray(metadata, imageType);
        _metadata = metadata;
        _nPlanesPerDim = _nPlanesPerDim();
        _factory = factory;
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
    private AWTBufferedImagePlane[] _createBuffreredImageArray(IMetadata metadata, BufferedImageTypes imageType) {
        int numPlanes = MetadataUtil.getNPlanes(metadata);
        int xdim = (int) metadata.getDim()[0];
        int ydim = (int) metadata.getDim()[0];

        AWTBufferedImagePlane[] _images = new AWTBufferedImagePlane[numPlanes];

        IntStream.range(0, _images.length).forEach(
                planeIndex -> _images[planeIndex] = new AWTBufferedImagePlane(planeIndex, xdim, ydim, imageType));
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
        if (_metadata.getDim().length <= 2) {
            return new long[] { 1 };
        }

        long[] imgDim = getMetadata().getDim();

        // Create the same vector size. Note that by default, the first two dims
        // have zero planes.
        long[] nPlanesPerDim = new long[imgDim.length];
        nPlanesPerDim[2] = imgDim[2];
        for (int dim = 3; dim < nPlanesPerDim.length; dim++) {
            nPlanesPerDim[dim] = nPlanesPerDim[dim - 1] * imgDim[dim];
        }

        return nPlanesPerDim;
    }

    /**
     * @return the plane number this position belongs to starting from 1.
     */
    public int getPlaneNumber(long[] position) {
        if (this._totalNumPlanes == 1) {
            return 1;
        }

        // One is added, because image planes start from one.
        int offSet = (int) position[2];
        return offSet + (int) IntStream.range(2, position.length)
                .mapToLong(i -> (position[i] + 1) * _nPlanesPerDim[i - 1]).sum() + 1;

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