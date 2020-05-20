package fr.fresnel.fourPolar.core.util.transform;

/**
 * An affine transform over a 2D space. Note that the underlying matrix is 2*3.
 */
public class AffineTransform2D implements AffineTransform {
    private final double[][] _matrix;

    public AffineTransform2D(){
        this._matrix = new double[2][3];
    }

    public void set(int row, int column, double value) {
        this._matrix[row][column] = value;
    }

    @Override
    public int dim() {
        return 2;
    }

    @Override
    public double get(int row, int column) {
        return this._matrix[row][column];
    }
}