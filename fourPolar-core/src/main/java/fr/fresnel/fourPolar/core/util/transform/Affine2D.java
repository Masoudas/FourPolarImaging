package fr.fresnel.fourPolar.core.util.transform;

import net.imglib2.realtransform.AffineTransform2D;

/**
 * An affine transform over a 2D space. Note that the underlying matrix is 2*3.
 * The original matrix is identity with last column set to zero.
 */
public class Affine2D implements AffineTransform {
    private AffineTransform2D _matrix;

    /**
     * The threshold that determines whether a matrix is invertible or not (if
     * determinant is closer to this than zero, we declare matrix as
     * non-invertible.)
     */
    private final double _detThreshold;

    public Affine2D() {
        this._matrix = new AffineTransform2D();
        this._detThreshold = 1e-5;
    }

    public Affine2D(double detThreshold) {
        this._detThreshold = detThreshold;
    }

    public void set(int row, int column, double value) {
        if (row >= 2 || column >= 3) {
            throw new IllegalArgumentException("Element does not exist.");
        }

        try {
            this._matrix.set(value, row, column);
        } catch (RuntimeException e) {
            // Unfortunately, everytime the matrix is updated, if it's not invertible, we
            // throw
            // this exception. We need to catch it to allow elemnt-wise setting of
            // parameters.
        }
    }

    public void set(double[][] matrix) {
        if (matrix.length != 2 || matrix[0].length != 3) {
            throw new IllegalArgumentException("Matrix is not 2*3");
        }

        try {
            this._matrix.set(matrix);
        } catch (RuntimeException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public int dim() {
        return 2;
    }

    @Override
    public double get(int row, int column) {
        if (row >= 2 || column >= 3) {
            throw new IllegalArgumentException("Element does not exist.");
        }
        return this._matrix.get(row, column);
    }

    @Override
    public double[][] get() {
        double[][] matrix = new double[2][3];
        this._matrix.toMatrix(matrix);

        return matrix;
    }

    @Override
    public boolean isInvertible() {
        double det = this._calculateDeterminant();
        if (Math.abs(det) < _detThreshold) {
            return false;
        } else {
            return true;
        }

    }

    private double _calculateDeterminant() {
        return _matrix.get(0, 0) * _matrix.get(1, 1) - _matrix.get(1, 0) * _matrix.get(0, 1);
    }

}