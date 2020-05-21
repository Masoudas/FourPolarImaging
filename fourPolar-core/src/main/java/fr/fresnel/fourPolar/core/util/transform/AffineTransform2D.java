package fr.fresnel.fourPolar.core.util.transform;

/**
 * An affine transform over a 2D space. Note that the underlying matrix is 2*3.
 */
public class AffineTransform2D implements AffineTransform {
    private double[][] _matrix;

    public AffineTransform2D() {
        this._matrix = new double[2][3];
    }

    public void set(int row, int column, double value) {
        this._matrix[row][column] = value;
    }

    public void set(double[][] matrix) {
        if (matrix.length != 2 || matrix[0].length != 3) {
            throw new IllegalArgumentException("Matrix is not 2*3");
        }

        this._matrix = matrix;
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