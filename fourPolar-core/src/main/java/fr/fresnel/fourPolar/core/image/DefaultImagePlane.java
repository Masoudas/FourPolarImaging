package fr.fresnel.fourPolar.core.image;

import java.util.Objects;

/**
 * A default implementation of {@link ImagePlane}, which simply works as a
 * wrapper for the type T, together with the associated plane index.
 * 
 * @param <T> is the image plane type.
 */
class DefaultImagePlane<T> implements ImagePlane<T> {
    private final int _planeIndex;
    private final T _imagePlane;
    public DefaultImagePlane(T imagePlane, int planeIndex) {
        Objects.requireNonNull(imagePlane);
        _checkImagePlaneIsGreaterThanZero(planeIndex);

        _planeIndex = planeIndex;
        _imagePlane = imagePlane;
    }

    public void _checkImagePlaneIsGreaterThanZero(int planeIndex) {
        if (planeIndex < 1) {
            throw new IndexOutOfBoundsException("plane index must be greater than zero.");
        }
    }

    @Override
    public int planeIndex() {
        return _planeIndex;
    }

    @Override
    public T getPlane() {
        return _imagePlane;
    }

}