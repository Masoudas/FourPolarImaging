package fr.fresnel.fourPolar.core.util.shape;

import java.util.Arrays;
import java.util.Objects;

import fr.fresnel.fourPolar.core.physics.axis.AxisOrder;
import fr.fresnel.fourPolar.core.util.shape.IShapeIterator;

/**
 * Helper class for iterating over Scaled shape. See {@link ShapeUtils}.
 */
class ScaledShapeIterator implements IShapeIterator {

    final private IShapeIterator _itr;
    final private long[] _max;
    private long[] _coords;
    final private int _shapeDim;

    private long _sumHigherDims;
    private long _currentSumHigherDims;

    public ScaledShapeIterator(IShape shape, AxisOrder newAxisOrder, long[] max) {
        this._max = max;
        this._coords = new long[shape.shapeDim() + max.length];
        this._coords[this._coords.length - 1] += 1;
        this._itr = shape.getIterator();
        this._shapeDim = shape.shapeDim();
        this._sumHigherDims = 1;
        for (int i = 0; i < max.length; i++) {
            this._sumHigherDims *= max[i] + 1;
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
        for (int i = 1; i < this._max.length; i++) {
            this._coords[this._shapeDim + i] += this._coords[this._shapeDim + i - 1] / (this._max[i - 1] + 1);
            this._coords[this._shapeDim + i - 1] %= (this._max[i - 1] + 1);
        }

        return this._coords.clone();
    }

    @Override
    public void reset() {
        this._itr.reset();
        Arrays.setAll(this._coords, (t) -> 0);
        this._currentSumHigherDims = this._sumHigherDims + 1;
    }
}
