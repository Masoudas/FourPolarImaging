package fr.fresnel.fourPolar.core.util.shape;

import java.util.Arrays;
import java.util.stream.IntStream;

import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import ij.gui.Line;

/**
 * To ensure that the line passes through the demanded position, a negative and
 * a positive line is formed, where negative line starts from the end point to
 * the position, and positive line starts from position to the end.
 */
public class ImageJ1LineShape implements ILineShape {
    private final AxisOrder _axisOrder;
    private final double _slopeAngle;
    private final long _length;
    private final int _thickness;
    private final double[] _position;
    private final Line _negativeLine;
    private final Line _positiveLine;

    /**
     * Create a line with the given slope, length, thickness from the position. Note
     * that eventhough the line is created in the xy plane, it can be merged in
     * higher dimensions, meaning the position and axis order don't necessarily have
     * to be 2 dimensional and XY.
     * 
     * @param slopeAngle is the slope of the dipole in particular direction.
     * @param position   is the pixel position of the dipole, as [x, y, z, ...]
     * @param length     is the desired length of the line.
     * @param thickness  is the desired thickness of the line.
     * 
     */
    public static ILineShape create(double[] position, double slopeAngle, long length, int thickness,
            AxisOrder axisOrder) {
        return new ImageJ1LineShape(position, slopeAngle, length, thickness, axisOrder);
    }

    private ImageJ1LineShape(double[] position, double slopeAngle, long length, int thickness, AxisOrder axisOrder) {
        this._checkPositionAtLeast2D(position);
        this._checkLengthIsPositive(length);
        this._checkThicknessIsPositive(thickness);

        this._checkNumAxisEqualsPositionDim(position.length, axisOrder);

        this._axisOrder = axisOrder;
        this._position = position;
        this._slopeAngle = slopeAngle;
        this._thickness = thickness;
        this._length = length;

        double[] lineStartPoint = _calculateStartPoint(position, slopeAngle, length);
        _negativeLine = _createLine(lineStartPoint, position, thickness);

        double[] lineEndPoint = _calculateEndPoint(position, slopeAngle, length);
        _positiveLine = _createLine(position, lineEndPoint, thickness);

    }

    private void _checkThicknessIsPositive(int thickness) {
        if (thickness < 0) {
            throw new IllegalArgumentException("Thickness must be positive to create line.");
        }
    }

    private void _checkLengthIsPositive(long length) {
        if (length < 0) {
            throw new IllegalArgumentException("Length must be positive to create line.");
        }
    }

    private void _checkPositionAtLeast2D(double[] position) {
        if (position.length < 2) {
            throw new IllegalArgumentException("Line position must be at least 2D.");
        }
    }

    private void _checkNumAxisEqualsPositionDim(int positionLength, AxisOrder axisOrder) {
        if (axisOrder != AxisOrder.NoOrder && positionLength != axisOrder.numAxis) {
            throw new IllegalArgumentException("Number of axis and position must be equal to create a line.");
        }

    }

    private double[] _calculateStartPoint(double[] position, double slopeAngle, long length) {
        double slope = Math.tan(slopeAngle);
        double cosSlopeAngle = Math.cos(slopeAngle);

        double[] xyStart = new double[2];
        xyStart[0] = position[0] - cosSlopeAngle * ((length - 1) / 2);
        xyStart[1] = position[1] + (xyStart[0] - position[0]) * slope;

        return xyStart;
    }

    private double[] _calculateEndPoint(double[] position, double slopeAngle, long length) {
        double slope = Math.tan(slopeAngle);
        double cosSlopeAngle = Math.cos(slopeAngle);

        double[] xyEnd = new double[2];
        xyEnd[0] = position[0] + cosSlopeAngle * (length / 2);
        xyEnd[1] = position[1] + (xyEnd[0] - position[0]) * slope;

        return xyEnd;
    }

    private Line _createLine(double[] startPoint, double[] endPoint, int thickness) {
        Line line = new Line(startPoint[0], startPoint[1], endPoint[0], endPoint[1]);
        line.setStrokeWidth(thickness);

        return line;
    }

    @Override
    public IShapeIterator getIterator() {

        return null;
    }

    @Override
    public AxisOrder axisOrder() {
        return _axisOrder;
    }

    @Override
    public int shapeDim() {
        return _position.length;
    }

    @Override
    public IShape rotate3D(double angle1, double angle2, double angle3, Rotation3DOrder rotation3dOrder) {
        throw new UnsupportedOperationException("Can't 3d rotate a 2D line");
    }

    @Override
    public IShape rotate2D(double angle) {
        double newSlope = _slopeAngle + angle;
        return create(_position, newSlope, _length, _thickness, _axisOrder);
    }

    @Override
    public IShape translate(long[] translation) {
        this._checkNumAxisEqualsPositionDim(translation.length, _axisOrder);

        double[] translatedPosition = IntStream.range(0, translation.length)
                .mapToDouble(i -> translation[i] + _position[i]).toArray();
        return create(translatedPosition, _slopeAngle, _length, _thickness, _axisOrder);
    }

    @Override
    public boolean isInside(long[] point) {
        if (point.length != this._position.length) {
            return false;
        }

        boolean containsXyPoint = _positiveLine.containsPoint(point[0], point[1])
                || _negativeLine.containsPoint(point[0], point[0]);
        boolean higherDimensionsEqual = IntStream.range(2, point.length).allMatch(dim -> point[dim] == _position[dim]);
        return containsXyPoint && higherDimensionsEqual;
    }

    @Override
    public IShape and(IShape shape) {
        
        return null;
    }

    @Override
    public long[] lineStart() {
        return new long[] { _negativeLine.x1, _negativeLine.y1 };
    }

    @Override
    public long[] lineEnd() {
        return new long[] { _negativeLine.x2, _negativeLine.y2 };
    }
}