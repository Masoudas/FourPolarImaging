package fr.fresnel.fourPolar.core.util.shape;

import java.util.Arrays;

import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;

public class ShapeFactory {
    private ShapeFactory() {
        throw new AssertionError();
    }

    /**
     * Generates a box from min to max. The box is closed in the sense that it
     * contains the boundary. Note min and max and axisOrder must have the same
     * dimension. Moreover, 1D box is not acceptable.
     * 
     * 
     * @param min       is the minimum coordinate.
     * @param max       is the maximum coordinate.
     * @param axisOrder is the axis composition associated with the shape.
     * 
     * @throws IllegalArgumentException for any violation of conditions mentioned
     *                                  above.
     */
    public static IBoxShape closedBox(long[] min, long[] max, AxisOrder axisOrder) {
        return ImgLib2BoxShape.create(min, max, axisOrder);
    }

    /**
     * Creates a polygon2D shape, with x and y as coordinates. We expect at least
     * three points to create a polygon, eventough this object can be tricked to
     * create 1D shapes (for example, if all three points are the same).
     * 
     * @param x is the x coordinate of the vertices.
     * @param y is the y coordinate of the vertices.
     * 
     * @throws IllegalArgument exception if at least three points are not supplied.
     * @return
     */
    public static IPolygon2DShape closedPolygon2D(long[] x, long[] y) {
        return ImgLib2Polygon2DShape.create(x, y);
    }

    public static IPointShape point(long[] point, AxisOrder axisOrder) {
        return ImgLib2PointShape.create(point, axisOrder);
    }

    /**
     * Create a line with the given slope, length, thickness from the position. The
     * length of line on each side of position is approximately len/2. Note that
     * even though the line is created in the xy plane, it can be merged in higher
     * dimensions, meaning the position and axis order don't necessarily have to be
     * 2 dimensional and XY.
     * 
     * @param slopeAngle is the slope of the line in radian.
     * @param position   is the position of the line.
     * @param length     is the desired length of the line.
     * @param thickness  is the desired thickness of the line.
     * @param axisOrder  is the axis order associated with position.
     * 
     */
    public static ILineShape line2DShape(long[] position, double slopeAngle, long length, int thickness, AxisOrder axisOrder) {
        double[] positionAsDouble = Arrays.stream(position).mapToDouble(t -> t).toArray();
        return ImageJ1LineShape.create(positionAsDouble, slopeAngle, length, thickness, axisOrder);
    }

}