package fr.fresnel.fourPolar.core.image;

import java.util.Objects;
import java.util.stream.IntStream;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.util.image.metadata.MetadataUtil;

/**
 * An interface that models image as instances of {@link ImagePlane} and allows
 * access through image plane index using the {@link ImagePlaneAccesser}.
 * 
 * @param <T> is the backend for image each plane.
 */
public abstract class PlanarImageModel<T> implements ImagePlaneAccesser<T> {
    private static final int _FIRST_PLANE_INDEX = 1;
   
    private final long[] _nPlanesPerDim;
    private final int _totalNumPlanes;
    private final long[] _imageDim;

    protected final ImagePlane<T>[] _planes;

    /**
     * Instantiates the planar model using the metadata of the image together with a
     * supplier for image planes. The plane supplier interface would be responsible
     * for creating instances of planes of type T.
     * 
     * @param metadata      is the metadata of the image.
     * @param planeSupplier is the supplier for creating new instances of plane.
     */
    protected PlanarImageModel(IMetadata metadata, ImagePlaneSupplier<T> planeSupplier) {
        Objects.requireNonNull(metadata, "Metadata can't be null");
        Objects.requireNonNull(planeSupplier, "supplier can't be null");

        _totalNumPlanes = _getNumPlanesFromMetadata(metadata);
        _imageDim = metadata.getDim();
        _nPlanesPerDim = _nPlanesPerDim();

        _planes = _createImageArray(metadata, planeSupplier);
    }

    private int _getNumPlanesFromMetadata(IMetadata metadata) {
        return MetadataUtil.getNPlanes(metadata);
    }

    /**
     * Create an instance of plane (or T) for each plane.
     * 
     * Example: Assume that image the is 2 * 2 * 3 * 2. Then an array of size 3 * 2
     * is created.
     * 
     * @param metadata      is the metadata of the image.
     * @param planeSupplier is the supplier of the image planes.
     * @return an array of buffered image plane that belong to each plane.
     */
    @SuppressWarnings("unchecked")
    private ImagePlane<T>[] _createImageArray(IMetadata metadata, ImagePlaneSupplier<T> planeSupplier) {
        ImagePlane<T>[] _images = new ImagePlane[_totalNumPlanes];

        IntStream.range(0, _images.length).forEach(
                planeIndex -> _images[planeIndex] = new DefaultImagePlane<T>(planeSupplier.get(), planeIndex + 1));
        return _images;
    }

    /**
     * A helper vector indicating how many planes are present per dimension. For
     * example, if dim = [2, 2, 3, 4], and given the first two axis indicate the the
     * xy plane, we conclude that 3 planes are present for the 3rd dim and 12 for
     * the fourth. Note that the first two elements are always set to zero for
     * convenience.
     */
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
     * Returns the image plane. Calculates plane number using
     * {@link #getPlaneNumber()}.
     */
    @Override
    public ImagePlane<T> getImagePlane(int planeNumber) {
        if (planeNumber < 1 || planeNumber > _totalNumPlanes) {
            throw new IllegalArgumentException(
                    "plane number should be greater than zero and less than total number of planes.");
        }
        return _planes[planeNumber - 1];

    }

    @Override
    public int getPlaneIndex(long[] position) {
        if (position.length != _imageDim.length) {
            throw new IllegalArgumentException("position does not have same dimension as image.");
        }

        if (this._totalNumPlanes == 1) {
            return 1;
        }

        int offSet = (int) position[2];
        return offSet
                + (int) IntStream.range(2, position.length).mapToLong(i -> position[i] * _nPlanesPerDim[i - 1]).sum()
                + _FIRST_PLANE_INDEX;

    }

    /**
     * Returns the total number of planes.
     */
    public int numPlanes() {
        return _totalNumPlanes;
    }
}
