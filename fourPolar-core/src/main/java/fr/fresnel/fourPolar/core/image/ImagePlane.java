package fr.fresnel.fourPolar.core.image;

/**
 * An interface for modeling an image plane. An image plane is a 2D plane, whose
 * underlying entity can be pixels, or vectors.
 * 
 * @param <T> is the type of underlying plane (which could be a 2D pixel or
 *            vector object).
 */
public interface ImagePlane<T> {
    /**
     * @return the plane index associated with this image.
     */
    public int planeIndex();

    /**
     * @return the underlying image.
     */
    public T getPlane();
}