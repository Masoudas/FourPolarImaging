package fr.fresnel.fourPolar.core.image.vector.batikModel;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.util.shape.IBoxShape;
import fr.fresnel.fourPolar.core.util.shape.ILineShape;
import fr.fresnel.fourPolar.core.util.shape.IPointShape;
import fr.fresnel.fourPolar.core.util.shape.IShape;
import fr.fresnel.fourPolar.core.util.shape.IShapeIterator;
import fr.fresnel.fourPolar.core.util.shape.Rotation3DOrder;
import fr.fresnel.fourPolar.core.util.shape.ShapeFactory;

public class ShapeConverterTest {
    @Test
    public void convertPoint_Convert1DPoint_ThrowsIllegalArgumentException() {
        IPointShape pointShape = new ShapeFactory().point(new long[] { 1 }, AxisOrder.NoOrder);

        assertThrows(IllegalArgumentException.class, () -> ShapeConverter.convertPoint(pointShape));
    }

    @Test
    public void convertPoint_Convert2DPoint_CreatesCorrectPointShape() {
        IPointShape pointShape = new ShapeFactory().point(new long[] { 1, 1 }, AxisOrder.XY);

        Point point = ShapeConverter.convertPoint(pointShape);

        assertTrue(point.x == pointShape.point()[0] && point.y == pointShape.point()[1]);
    }

    @Test
    public void convertPoint_Convert3DPoint_PointOnlyHasFirstTwoDimension() {
        IPointShape pointShape = new ShapeFactory().point(new long[] { 1, 1, 0 }, AxisOrder.XYZ);

        Point point = ShapeConverter.convertPoint(pointShape);

        assertTrue(point.x == pointShape.point()[0] && point.y == pointShape.point()[1]);
    }

    @Test
    public void convertLine_Convert1DLine_ThrowsIllegalArgumentException() {
        ILineShape lineShape = new OneDimensionalLine(0, 1);

        assertThrows(IllegalArgumentException.class, () -> ShapeConverter.convertLine(lineShape));
    }

    @Test
    public void convertLine_Convert2DLineFromOriginTo22_CreatesCorrect2DLine() {
        ILineShape lineShape = new ShapeFactory().line2DShape(new long[] { 1, 1 }, Math.PI / 4, 1, 1, AxisOrder.XY);

        Line2D line = ShapeConverter.convertLine(lineShape);

        assertTrue(line.getX1() == lineShape.lineStart()[0] && line.getX2() == lineShape.lineStart()[1]
                && line.getY1() == lineShape.lineEnd()[1] && line.getY2() == lineShape.lineEnd()[1]);
    }

    @Test
    public void convertLine_Convert3DLineFromOriginTo220_CreatesCorrect2DLine() {
        ILineShape lineShape = new ShapeFactory().line2DShape(new long[] { 1, 1, 0 }, Math.PI / 4, 1, 1, AxisOrder.XYZ);

        Line2D line = ShapeConverter.convertLine(lineShape);

        assertTrue(line.getX1() == lineShape.lineStart()[0] && line.getX2() == lineShape.lineStart()[1]
                && line.getY1() == lineShape.lineEnd()[1] && line.getY2() == lineShape.lineEnd()[1]);
    }

    @Test
    public void convertBox_Convert2DBoxFromOriginTo11_ReturnsCorrectBox() {
        IBoxShape boxShape = new ShapeFactory().closedBox(new long[] { 0, 0 }, new long[] { 1, 1 }, AxisOrder.XY);

        Rectangle rectangle = ShapeConverter.convertBox(boxShape);

        assertTrue(rectangle.x == boxShape.min()[0] && rectangle.y == boxShape.min()[1]
                && rectangle.width == (boxShape.max()[0] - boxShape.min()[0])
                && rectangle.width == (boxShape.max()[1] - boxShape.min()[1]));
    }

    @Test
    public void convertBox_Convert3DBoxFromOriginTo111_ReturnsTheBoxInTheXyPlane() {
        IBoxShape boxShape = new ShapeFactory().closedBox(new long[] { 0, 0, 0 }, new long[] { 1, 1, 1 },
                AxisOrder.XYZ);

        Rectangle rectangle = ShapeConverter.convertBox(boxShape);

        assertTrue(rectangle.x == boxShape.min()[0] && rectangle.y == boxShape.min()[1]
                && rectangle.width == (boxShape.max()[0] - boxShape.min()[0])
                && rectangle.width == (boxShape.max()[1] - boxShape.min()[1]));
    }

}

class OneDimensionalLine implements ILineShape {
    private int lineStart;
    private int lineEnd;

    OneDimensionalLine(int lineStart, int lineEnd) {
        this.lineStart = lineStart;
        this.lineEnd = lineEnd;
    }

    @Override
    public IShapeIterator getIterator() {
        return null;
    }

    @Override
    public AxisOrder axisOrder() {
        return AxisOrder.NoOrder;
    }

    @Override
    public int shapeDim() {
        return 1;
    }

    @Override
    public IShape rotate3D(double angle1, double angle2, double angle3, Rotation3DOrder rotation3dOrder) {
        return null;
    }

    @Override
    public IShape rotate2D(double angle) {
        return null;
    }

    @Override
    public IShape translate(long[] translation) {
        return null;
    }

    @Override
    public boolean isInside(long[] point) {
        return false;
    }

    @Override
    public IShape and(IShape shape) {
        return null;
    }

    @Override
    public long[] lineStart() {
        return new long[] { lineStart };
    }

    @Override
    public long[] lineEnd() {
        return new long[] { lineEnd };
    }

}