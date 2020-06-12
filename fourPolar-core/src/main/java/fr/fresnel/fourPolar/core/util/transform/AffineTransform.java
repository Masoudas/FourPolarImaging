package fr.fresnel.fourPolar.core.util.transform;

/**
 * An interface for modelling an affine transform.
 */
public interface AffineTransform {
    /**
     * Returns the dimension of the affine transform (note that the underlying
     * matrix is n*(n+1)).
     */
    public int dim();

    /**
     * Get an element of the transform.
     */
    public double get(int row, int column);

    /**
     * Get a copy of the underlying affine transform matrix
     * 
     * @return
     */
    public double[][] get();

}