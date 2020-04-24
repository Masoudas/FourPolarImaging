package fr.fresnel.fourPolar.core.util.shape;

import java.util.Arrays;
import java.util.Objects;

import fr.fresnel.fourPolar.core.util.shape.IShapeIterator;

/**
 * Helper class for iterating over Scaled shape. See {@link ShapeUtils}.
 */
class ShapeScalarIterator implements IShapeIterator {
    public static IShapeIterator getIterator(IShape shape, long[] scaleDimension) {
        Objects.requireNonNull(shape, "Shape cannot be null");
        Objects.requireNonNull(scaleDimension, "scaleDimension cannot be null");

        if (shape.shapeDim() >= scaleDimension.length) {
            return shape.getIterator();
        } else {
            return new ShapeScalarIterator(shape.getIterator(), shape.spaceDim(), scaleDimension);
        }
    }

    final private IShapeIterator _itr;
    final private long[] _scaleDim;
    private long[] _coords;
    final private int _shapeDim;

    private long _sumHigherDims;
    private long _currentSumHigherDims;

    private ShapeScalarIterator(IShapeIterator shapeIterator, int shapeDim, long[] scaleDimension) {
        if (Arrays.stream(scaleDimension, shapeDim, scaleDimension.length).min().getAsLong() <= 0) {
            throw new IllegalArgumentException("Scale dimension cannot be zero or negative");
        }
        
        this._scaleDim = scaleDimension;
        this._coords = scaleDimension.clone();
        this._coords[this._coords.length - 1] += 1;
        this._itr = shapeIterator;
        this._shapeDim = shapeDim;
        this._sumHigherDims = 1;
        for (int i = shapeDim; i < scaleDimension.length; i++) {
            this._sumHigherDims *= scaleDimension[i];
        }
        this._currentSumHigherDims = this._sumHigherDims + 1;
    }

    @Override
    public boolean hasNext() {
        boolean shapeHasPose = this._itr.hasNext();
        boolean currentPoseIsFinished = this._currentSumHigherDims >= this._sumHigherDims;

        if (currentPoseIsFinished && shapeHasPose) {
            this._currentSumHigherDims = 0;
            currentPoseIsFinished = false;
            Arrays.setAll(this._coords, (t) -> 0);

            long[] shapePosition = this._itr.next();
            this._coords[shapePosition.length] = -1;
            System.arraycopy(shapePosition, 0, this._coords, 0, shapePosition.length);
        }

        return !currentPoseIsFinished;
    }

    @Override
    public long[] next() {
        this._coords[this._shapeDim]++;
        this._currentSumHigherDims++;
        for (int i = this._shapeDim + 1; i < this._scaleDim.length; i++) {
            this._coords[i] += this._coords[i - 1] / this._scaleDim[i - 1];
            this._coords[i - 1] %= this._scaleDim[i - 1];
        }

        return this._coords;
    }

    @Override
    public void reset() {
        this._itr.reset();
        Arrays.setAll(this._coords, (t) -> 0);
    }
}
