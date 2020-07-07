package fr.fresnel.fourPolar.core.image.vector.batikModel;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;

import fr.fresnel.fourPolar.core.util.shape.IBoxShape;
import fr.fresnel.fourPolar.core.util.shape.ILineShape;
import fr.fresnel.fourPolar.core.util.shape.IPointShape;
import fr.fresnel.fourPolar.core.util.shape.IShape;

/**
 * A set of static methods for converting an {@link IShape} to java AWT shapes.
 */
class ShapeConverter {
    private ShapeConverter() {
        throw new AssertionError();
    }

    /**
     * Converts a point shape to a {@link Point}. The dimension of the point has be
     * at least two. Only the first two coordinates are wrapped in the returned
     * point.
     * 
     * @param pointShape is the point shape.
     * @return an awt point shape containing the first two coordinates of the point
     *         shape.
     * 
     * @throws IllegalArgumentException if the point shape is not at least 2D.
     */
    public static Point convertPoint(IPointShape pointShape) {
        _checkShapeIs2D(pointShape);

        long[] point = pointShape.point();
        return new Point((int) point[0], (int) point[1]);
    }

    /**
     * Converts a box shape to a {@link Rectangle}. The dimension of the point has
     * be at least two. Only the first two coordinates are wrapped in the returned
     * rectangle shape.
     * 
     * @param boxShape is the box shape.
     * @return an awt point shape containing the first two coordinates of the point
     *         shape.
     * 
     * @throws IllegalArgumentException if the box shape is not at least 2D.
     */
    public static Rectangle convertBox(IBoxShape boxShape) {
        _checkShapeIs2D(boxShape);

        long[] min = boxShape.min();
        long[] max = boxShape.max();
        return new Rectangle((int) min[0], (int) min[1], (int) (max[0] - min[0]), (int) (max[1] - min[1]));
    }

    /**
     * Converts a line shape to a {@link Line2D}. Only the first two coordinates of
     * the line are wrapped in the returned line shape.
     * 
     * @param boxShape is the box shape.
     * @return an awt point shape containing the first two coordinates of the point
     *         shape.
     * 
     * @throws IllegalArgumentException if the box shape is not at least 2D.
     */
    public static Line2D convertLine(ILineShape lineShape) {
        _checkShapeIs2D(lineShape);

        long[] lineStart = lineShape.lineStart();
        long[] lineEnd = lineShape.lineEnd();
        return new Line2D.Double(lineStart[0], lineStart[1], lineEnd[0], lineEnd[1]);

    }

    private static void _checkShapeIs2D(IShape shape) {
        if (shape.shapeDim() < 2) {
            throw new IllegalArgumentException(
                    "The shape to be drawn using batik library has to be at least two dimensional.");
        }
    }
}