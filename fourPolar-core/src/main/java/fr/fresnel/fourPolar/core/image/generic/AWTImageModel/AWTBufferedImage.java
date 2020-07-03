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
     * Returns the buffered image of the demanded plane. Calculate plane number
     * using {@link #getPlaneNumber()}.
     */
    public BufferedImage getImagePlane(int planeNumber) {
        if (planeNumber < 0 || planeNumber > _totalNumPlanes) {
            throw new IllegalArgumentException(
                    "plane number should be greater than zero and less than total number of planes.");
        }
        return _images[planeNumber];

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

    /**
     * Returns the plane number this position belongs to starting from 0.
     */
    public int getPlaneNumber(long[] position) {
        if (this._totalNumPlanes == 1){
            return 0;
        }

        int offSet = (int) position[0];
        return offSet + (int) IntStream.range(2, position.length)
                .mapToLong(i -> (position[i] + 1) * _nPlanesPerDim[i - 1]).sum();

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