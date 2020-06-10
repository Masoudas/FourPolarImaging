package fr.fresnel.fourPolar.core.util.transform;

import net.imglib2.realtransform.AffineTransform3D;

/**
 * An affine transform over a 3D space. Note that the underlying matrix is 3*4.
 * The original matrix is identity with last column set to zero.
 */
public class Affine3D implements AffineTransform {
    private AffineTransform3D _matrix;

    public Affine3D() {
        this._matrix = new AffineTransform3D();
    }

    public void set(int row, int column, double value) {
        if (row >= 3 || column >= 4) {
            throw new IllegalArgumentException("Element does not exist.");
        }

        this._matrix.set(value, row, column);
    }

    public void set(double[][] matrix) {
        if (matrix.length != 3 || matrix[0].length != 4) {
            throw new IllegalArgumentException("Matrix is not 3*4");
        }

        try {
            this._matrix.set(matrix);
        } catch (RuntimeException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public int dim() {
        return 3;
    }

    @Override
    public double get(int row, int column) {
        if (row >= 3 || column >= 4) {
            throw new IllegalArgumentException("Element does not exist.");
        }

        return this._matrix.get(row, column);
    }
}
