package fr.fresnel.fourPolar.core.image;

import java.util.Objects;
import java.util.function.Supplier;
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
    private final long[] _nPlanesPerDim;

    private final int _totalNumPlanes;

    protected final ImagePlane<T>[] _planes;

    private final long[] _imageDim;

    protected PlanarImageModel(Supplier<T> supplier, IMetadata metadata) {
        Objects.requireNonNull(metadata, "Metadata can't be null");
        Objects.requireNonNull(supplier, "supplier can't be null");

        _totalNumPlanes = _getNumPlanesFromMetadata(metadata);
        _imageDim = metadata.getDim();
        _nPlanesPerDim = _nPlanesPerDim();
    }

    private int _getNumPlanesFromMetadata(IMetadata metadata) {
        return MetadataUtil.getNPlanes(metadata);
    }

    /**
     * Create a buffered image for each plane of the given dimension. Assume that
     * image is 2 * 2 * 3 * 2. Then an array of size 3 * 2 is created, where each
     * buffered image would be 2 * 2 in size.
     * 
     * @param dim is the dimension of the image.
     * @return an array of buffered image plane that belong to each plane.
     */
    private T[] _createBuffreredImageArray(IMetadata metadata, Supplier<T> planeSupplier) {
        ImagePlane<T>[] _images = new ImagePlane[_totalNumPlanes];

        IntStream.range(0, _images.length).forEach(
                planeIndex -> _images[planeIndex] = planeSupplier.get());
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

}
