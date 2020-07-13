package fr.fresnel.fourPolar.core.image;

/**
 * An interface for accessing the planes of
 * 
 * @param <T>
 */
public interface ImagePlaneAccesser<T> {
    /**
     * Returns the image plane this particular position belongs to. It's the
     * responsibilty of the caller to ensure that this position is consistent with
     * the dimension of the image.
     * 
     * @param position is the position.
     * @return the index of the plane this position belongs to.
     */
    public int getPlaneIndex(long[] position);

    /**
     * Returns the plane corresponding to the given index.
     * 
     * @param planeIndex is the index of the plane, which is greater than zero.
     * @throws IndexOutOfBoundsException if the plane index is less than zero or does
     *                                  not exist.
     */
    public T getImagePlane(int planeIndex);
}
