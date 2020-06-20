package fr.fresnel.fourPolar.core.util.transform;

import net.imglib2.realtransform.AffineTransform2D;

/**
 * An affine transform over a 2D space. Note that the underlying matrix is 2*3.
 * The original matrix is identity with last column set to zero.
 */
public class Affine2D implements AffineTransform {
    private AffineTransform2D _matrix;

    public Affine2D() {
        this._matrix = new AffineTransform2D();
    }

    public void set(int row, int column, double value) {
        if (row >= 2 || column >= 3) {
            throw new IllegalArgumentException("Element does not exist.");
        }

        this._matrix.set(value, row, column);
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
        try{
            this._matrix.inverse();
        } catch (RuntimeException e){
            return false;
        }

        return true;

    }

}