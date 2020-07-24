package fr.fresnel.fourPolar.core.image;

import java.util.Objects;
import java.util.stream.IntStream;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.util.image.generic.metadata.MetadataUtil;

/**
 * An interface that models image as instances of {@link ImagePlane} and allows
 * access through image plane index using the {@link ImagePlaneAccessor}.
 * 
 * @param <T> is the backend for image each plane.
 */
public abstract class PlanarImageModel<T> implements ImagePlaneAccessor<T> {
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
     * 
     * @throws IllegalArgumentException if the dimension vector in metadata not have
     *                                  at least two dimensions
     */
    protected PlanarImageModel(IMetadata metadata, ImagePlaneSupplier<T> planeSupplier) {
        Objects.requireNonNull(metadata, "metadata can't be null");
        Objects.requireNonNull(planeSupplier, "supplier can't be null");

        if (metadata.getDim().length < 2) {
            throw new IllegalArgumentException("Can't create a planar image with less than two dimensions.");
        }

        _totalNumPlanes = _getNumPlanesFromMetadata(metadata);
        _imageDim = metadata.getDim();
        _nPlanesPerDim = getNumPlanesPerDimension(metadata);

        _planes = _createImageArray(metadata, planeSupplier);
    }

    /**
     * Instantiates the planar model, by directly providing the image planes as an
     * array.
     * 
     * @param metadata is the metadata of the image.
     * @param planes   is the array of planes.
     * 
     * @throws IllegalArgumentException if number of planes in metadata does not
     *                                  match the number of planes, or a plane size
     *                                  is not equal to the metadata size.
     */
    protected PlanarImageModel(IMetadata metadata, T[] planes) {
        Objects.requireNonNull(metadata, "metadata can't be null");
        Objects.requireNonNull(planes, "planes can't be null");

        if (MetadataUtil.getNPlanes(metadata) != planes.length) {
            throw new IllegalArgumentException(
                    "number of planes in metadata does not correspond to the length of planes array.");
        }
        _checkPlanesHaveSameDimensionAsMetadata(metadata, planes);

        _totalNumPlanes = _getNumPlanesFromMetadata(metadata);
        _imageDim = metadata.getDim();
        _nPlanesPerDim = getNumPlanesPerDimension(metadata);

        _planes = _wrapPlanesToImagePlane(planes);
    }

    /**
     * A method that checks all the planes provided by the caller have the same
     * dimension as the plane dimension provided by the metadata, and throw an
     * IllegalArgumentException otherwise. This method is used inside the
     * {@link #PlanarImageModel()} constructor.
     * 
     * @param metadata is the metadata of the image.
     * @param planes   are the planes provided for construction of the object.
     */
    protected abstract void _checkPlanesHaveSameDimensionAsMetadata(IMetadata metadata, T[] planes)
            throws IllegalArgumentException;

    @SuppressWarnings("unchecked")
    private ImagePlane<T>[] _wrapPlanesToImagePlane(T[] planes) {
        ImagePlane<T>[] wrappedPlanes = new ImagePlane[planes.length];
        for (int i = 0; i < planes.length; i++) {
            wrappedPlanes[i] = new DefaultImagePlane<T>(planes[i], i + 1);
        }
        return wrappedPlanes;
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
     * @return number of planes per dimension.
     */
    private long[] getNumPlanesPerDimension(IMetadata metadata) {
        return MetadataUtil.numPlanesPerDimension(metadata);
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

    @Override
    public int numPlanes() {
        return _totalNumPlanes;
    }
}
