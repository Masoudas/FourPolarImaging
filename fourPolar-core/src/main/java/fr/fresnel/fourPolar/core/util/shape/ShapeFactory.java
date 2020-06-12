package fr.fresnel.fourPolar.core.util.shape;

import java.util.Objects;
import java.util.stream.IntStream;

import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;

public class ShapeFactory {
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
    public IBoxShape closedBox(long[] min, long[] max, AxisOrder axisOrder) {
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
    public IPolygon2DShape closedPolygon2D(long[] x, long[] y) {
        return ImgLib2Polygon2DShape.create(x, y);
    }

    public IPointShape point(long[] point, AxisOrder axisOrder) {
        return ImgLib2PointShape.create(point, axisOrder);
    }

}