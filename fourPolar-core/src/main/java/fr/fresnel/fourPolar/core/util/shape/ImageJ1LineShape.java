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
    private final int _spaceDim;
    private final AxisOrder _axisOrder;
    private final double _slopeAngle;
    private final long _length;
    private final int _thickness;
    private final double[] _position;
    private final Line _line;

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
     * @param axisOrder  is the axis order associated with position.
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

        this._spaceDim = position.length;
        this._axisOrder = axisOrder;
        this._position = position;
        this._slopeAngle = slopeAngle;
        this._thickness = thickness;
        this._length = length;

        double[] lineStartPoint = _calculateStartPoint(position, slopeAngle, length);
        double[] lineEndPoint = _calculateEndPoint(position, slopeAngle, length);

        _line = _createLine(lineStartPoint, lineEndPoint, thickness);
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
        double sinSlopeAngle = Math.sin(slopeAngle);
        double cosSlopeAngle = Math.cos(slopeAngle);

        double[] xyStart = new double[2];
        xyStart[0] = position[0] - cosSlopeAngle * length / 2d;
        xyStart[1] = position[1] - sinSlopeAngle * length / 2d;

        return xyStart;
    }

    private double[] _calculateEndPoint(double[] position, double slopeAngle, long length) {
        double sinSlopeAngle = Math.sin(slopeAngle);
        double cosSlopeAngle = Math.cos(slopeAngle);

        double[] xyEnd = new double[2];
        xyEnd[0] = position[0] + cosSlopeAngle * length / 2d;
        xyEnd[1] = position[1] + sinSlopeAngle * length / 2d;

        return xyEnd;
    }

    private Line _createLine(double[] startPoint, double[] endPoint, int thickness) {
        Line line = new Line(startPoint[0], startPoint[1], endPoint[0], endPoint[1]);
        line.setStrokeWidth(thickness);

        return line;
    }

    @Override
    public IShapeIterator getIterator() {
        long[] positionAsLong = Arrays.stream(_position).mapToLong(t -> (long) t).toArray();
        return new ImageJ1LineShapeIterator(_line.iterator(), positionAsLong);
    }

    @Override
    public AxisOrder axisOrder() {
        return _axisOrder;
    }

    @Override
    public int shapeDim() {
        return 2;
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
        /**
         * If needed to implement this in the future, think about the case where line has a thickness too.
         * The contains method of Line does not work at all!
         */
        // if (point.length != this._position.length) {
        //     return false;
        // }
        //  x>=this.x && y>=this.y && x<this.x+width && y<this.y+height;
        // boolean containsXyPoint = _line.containsPoint(point[0], point[0]);
        // boolean higherDimensionsEqual = IntStream.range(2, point.length).allMatch(dim -> point[dim] == _position[dim]);
        // return containsXyPoint && higherDimensionsEqual;
        throw new UnsupportedOperationException("This operation is not supported for this shape");
    }

    @Override
    public IShape and(IShape shape) {
        // ShapeRoi shapeRoi = new ShapeRoi(new Line(0, 0, 0, 0)); Use this if you
        // needed this operation!
        throw new UnsupportedOperationException("This operation is not supported for this shape");
    }

    @Override
    public long[] lineStart() {
        long[] start = Arrays.stream(_position).mapToLong(t->(long)t).toArray();
        start[0] = _line.x1;
        start[1] = _line.y1;
        return start;
    }

    @Override
    public long[] lineEnd() {
        long[] end = Arrays.stream(_position).mapToLong(t->(long)t).toArray();
        end[0] = _line.x2;
        end[1] = _line.y2;
        return end;
    }

    @Override
    public int spaceDim() {
        return this._spaceDim;
    }
}