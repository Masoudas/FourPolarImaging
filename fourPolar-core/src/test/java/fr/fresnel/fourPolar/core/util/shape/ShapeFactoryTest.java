package fr.fresnel.fourPolar.core.util.shape;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import net.imglib2.roi.RealMaskRealInterval;
import net.imglib2.roi.geom.GeomMasks;
import net.imglib2.roi.geom.real.PointMask;
import net.imglib2.roi.geom.real.Polygon2D;
import net.imglib2.roi.geom.real.WritableBox;

public class ShapeFactoryTest {

    @Test
    public void closedBox_RectangularBox_ReturnsCorrectPoints() {
        long[] min = { 1, 1, 1 };
        long[] max = { 2, 2, 2 };
        IShape shape = new ShapeFactory().closedBox(min, max);

        WritableBox box = GeomMasks.closedBox(new double[] { 1, 1, 1 }, new double[] { 2, 2, 2 });
        IShapeIterator iterator = shape.getIterator();

        assertTrue(_checkPointInsideMask(box, iterator));

    }

    @Test
    public void closed2DBox_Rotate_ReturnsCorrectCoordinates() {
        IShape shape = new ShapeFactory().closedBox(new long[] { 0, 0 }, new long[] { 1, 2 });

        shape.translate(new long[] { 0, 0 });
        shape.rotate(0, Math.PI / 2, 0);
        IShape rotated90 = shape.getTransformedShape();
        IShapeIterator iterator = rotated90.getIterator();
        WritableBox box1 = GeomMasks.closedBox(new double[] { -2, 0 }, new double[] { 0, 1 });
        assertTrue(_checkPointInsideMask(box1, iterator));

        shape.translate(new long[] { 0, 0 });
        shape.rotate(0, -Math.PI / 2, 0);
        IShape rotated270 = shape.getTransformedShape();
        WritableBox box2 = GeomMasks.closedBox(new double[] { 0, -1 }, new double[] { 2, 0 });
        assertTrue(_checkPointInsideMask(box2, rotated270.getIterator()));
    }

    @Test
    public void closed2DBox_Shift_ReturnsCorrectCoordinates() {
        IShape shape = new ShapeFactory().closedBox(new long[] { 0, 0 }, new long[] { 1, 2 });

        shape.rotate(0, 0, 0);
        shape.translate(new long[] { 50, 50 });
        IShape translated50 = shape.getTransformedShape();
        WritableBox box1 = GeomMasks.closedBox(new double[] { 50, 50 }, new double[] { 50 + 1, 50 + 2 });
        assertTrue(_checkPointInsideMask(box1, translated50.getIterator()));

        shape.rotate(0, 0, 0);
        shape.translate(new long[] { 100, 100 });
        IShape translated100 = shape.getTransformedShape();
        WritableBox box2 = GeomMasks.closedBox(new double[] { 100, 100 }, new double[] { 100 + 1, 100 + 2 });
        assertTrue(_checkPointInsideMask(box2, translated100.getIterator()));
    }

    @Test
    public void closed2DBox_TranslateAndRotate_ReturnsCorrectCoordinates() {
        IShape shape = new ShapeFactory().closedBox(new long[] { 0, 0 }, new long[] { 1, 2 });

        shape.rotate(0, Math.PI / 2, 0);
        shape.translate(new long[] { 50, 50 });
        IShape translated50rotate90 = shape.getTransformedShape();
        WritableBox box1 = GeomMasks.closedBox(new double[] { 50 - 2, 50 - 0 }, new double[] { 50 + 0, 50 + 1 });
        assertTrue(_checkPointInsideMask(box1, translated50rotate90.getIterator()));

        shape.rotate(0, -Math.PI / 2, 0);
        shape.translate(new long[] { 50, 50 });
        IShape translated50rotate270 = shape.getTransformedShape();
        WritableBox box2 = GeomMasks.closedBox(new double[] { 50 - 0, 50 - 1 }, new double[] { 50 + 2, 50 + 0 });
        assertTrue(_checkPointInsideMask(box2, translated50rotate270.getIterator()));
    }

    @Test
    public void closed2DBox_HigherDimensionTranslateAndRotate_ReturnsCorrectCoordinates() {
        IShape shape = new ShapeFactory().closedBox(new long[] { 0, 0, 2, 2, 2 }, new long[] { 1, 2, 2, 2, 2 });

        shape.rotate(0, Math.PI / 2, 0);
        shape.translate(new long[] { 50, 50, 0, 0, 0 });
        IShape translated50rotate90 = shape.getTransformedShape();
        WritableBox box1 = GeomMasks.closedBox(new double[] { 50 - 2, 50 - 0, 2, 2, 2 },
                new double[] { 50 + 0, 50 + 1, 2, 2, 2 });
        assertTrue(_checkPointInsideMask(box1, translated50rotate90.getIterator()));

        shape.rotate(0, -Math.PI / 2, 0);
        shape.translate(new long[] { 50, 50, 0, 0, 0 });
        IShape translated50rotate270 = shape.getTransformedShape();
        WritableBox box2 = GeomMasks.closedBox(new double[] { 50 - 0, 50 - 1, 2, 2, 2 },
                new double[] { 50 + 2, 50 + 0, 2, 2, 2 });
        assertTrue(_checkPointInsideMask(box2, translated50rotate270.getIterator()));
    }

    @Test
    public void closed3DBox_zRotate_ReturnsCorrectCoordinates() {
        IShape shape = new ShapeFactory().closedBox(new long[] { 0, 0, 0 }, new long[] { 1, 2, 3 });

        shape.rotate(0, Math.PI / 2, 0);
        shape.translate(new long[] { 0, 0, 0 });
        IShape rotated90 = shape.getTransformedShape();
        WritableBox box1 = GeomMasks.closedBox(new double[] { -2, 0, 0 }, new double[] { 0, 1, 3 });
        assertTrue(_checkPointInsideMask(box1, rotated90.getIterator()));

        shape.rotate(0, -Math.PI / 2, 0);
        shape.translate(new long[] { 0, 0, 0 });
        IShape rotated270 = shape.getTransformedShape();
        WritableBox box2 = GeomMasks.closedBox(new double[] { 0, -1, 0 }, new double[] { 2, 0, 3 });
        assertTrue(_checkPointInsideMask(box2, rotated270.getIterator()));

    }

    @Test
    public void closed3DBox_xRotate_ReturnsCorrectCoordinates() {
        IShape shape = new ShapeFactory().closedBox(new long[] { 0, 0, 0 }, new long[] { 1, 2, 3 });

        shape.rotate(Math.PI / 2, 0, 0);
        shape.translate(new long[] { 0, 0, 0 });
        IShape rotated90 = shape.getTransformedShape();
        WritableBox box1 = GeomMasks.closedBox(new double[] { 0, -3, 0 }, new double[] { 1, 0, 2 });
        assertTrue(_checkPointInsideMask(box1, rotated90.getIterator()));

        shape.rotate(-Math.PI / 2, 0, 0);
        shape.translate(new long[] { 0, 0, 0 });
        IShape rotated270 = shape.getTransformedShape();
        WritableBox box2 = GeomMasks.closedBox(new double[] { 0, 0, -2 }, new double[] { 1, 3, 0 });
        assertTrue(_checkPointInsideMask(box2, rotated270.getIterator()));
    }

    @Test
    public void closed3DBox_xRotation90zrotation90_ReturnsCorrectCoordinates() {
        IShape shape = new ShapeFactory().closedBox(new long[] { 0, 0, 0 }, new long[] { 1, 2, 3 });

        shape.rotate(Math.PI / 2, Math.PI / 2, 0);
        shape.translate(new long[] { 0, 0, 0 });
        IShape rotated9090 = shape.getTransformedShape();
        WritableBox box1 = GeomMasks.closedBox(new double[] { 0, 0, 0 }, new double[] { 3, 1, 2 });
        assertTrue(_checkPointInsideMask(box1, rotated9090.getIterator()));

        shape.rotate(-Math.PI / 2, -Math.PI / 2, 0);
        shape.translate(new long[] { 0, 0, 0 });
        IShape rotated270270 = shape.getTransformedShape();
        WritableBox box2 = GeomMasks.closedBox(new double[] { 0, -1, -2 }, new double[] { 3, 0, 0 });
        assertTrue(_checkPointInsideMask(box2, rotated270270.getIterator()));

        shape.rotate(-Math.PI / 2, Math.PI / 2, 0);
        shape.translate(new long[] { 0, 0, 0 });
        IShape rotated27090 = shape.getTransformedShape();
        WritableBox box3 = GeomMasks.closedBox(new double[] { -3, 0, -2 }, new double[] { 0, 1, 0 });
        assertTrue(_checkPointInsideMask(box3, rotated27090.getIterator()));

        shape.rotate(Math.PI / 2, -Math.PI / 2, 0);
        shape.translate(new long[] { 0, 0, 0 });
        IShape rotated90270 = shape.getTransformedShape();
        WritableBox box4 = GeomMasks.closedBox(new double[] { -3, -1, 0 }, new double[] { 0, 0, 2 });
        assertTrue(_checkPointInsideMask(box4, rotated90270.getIterator()));

    }

    @Test
    public void closed3DBox_translate_ReturnsCorrectCoordinates() {
        IShape shape = new ShapeFactory().closedBox(new long[] { 0, 0, 0 }, new long[] { 1, 2, 3 });

        shape.rotate(0, 0, 0);
        shape.translate(new long[] { 50, 50, 50 });
        IShape translated50 = shape.getTransformedShape();
        WritableBox box1 = GeomMasks.closedBox(new double[] { 50, 50, 50 }, new double[] { 50 + 1, 50 + 2, 50 + 3 });
        assertTrue(_checkPointInsideMask(box1, translated50.getIterator()));

        shape.rotate(0, 0, 0);
        shape.translate(new long[] { 100, 100, 100 });
        IShape translated100 = shape.getTransformedShape();
        WritableBox box2 = GeomMasks.closedBox(new double[] { 100, 100, 100 },
                new double[] { 100 + 1, 100 + 2, 100 + 3 });
        assertTrue(_checkPointInsideMask(box2, translated100.getIterator()));

    }

    @Test
    public void closed3DBox_RotateAndTranslate_ReturnsCorrectCoordinates() {
        IShape shape = new ShapeFactory().closedBox(new long[] { 0, 0, 0 }, new long[] { 1, 2, 3 });

        shape.rotate(Math.PI / 2, Math.PI / 2, 0);
        shape.translate(new long[] { 50, 50, 50 });
        IShape rotated9090 = shape.getTransformedShape();
        WritableBox box1 = GeomMasks.closedBox(new double[] { 50, 50, 50 }, new double[] { 50 + 3, 50 + 1, 50 + 2 });
        assertTrue(_checkPointInsideMask(box1, rotated9090.getIterator()));

        shape.rotate(-Math.PI / 2, -Math.PI / 2, 0);
        shape.translate(new long[] { 50, 50, 50 });
        IShape rotated270270 = shape.getTransformedShape();
        WritableBox box2 = GeomMasks.closedBox(new double[] { 50 + 0, 50 - 1, 50 - 2 },
                new double[] { 50 + 3, 50 + 0, 50 + 0 });
        assertTrue(_checkPointInsideMask(box2, rotated270270.getIterator()));

        shape.rotate(-Math.PI / 2, Math.PI / 2, 0);
        shape.translate(new long[] { 50, 50, 50 });
        IShape rotated27090 = shape.getTransformedShape();
        WritableBox box3 = GeomMasks.closedBox(new double[] { 50 - 3, 50 + 0, 50 - 2 },
                new double[] { 50 + 0, 50 + 1, 50 + 0 });
        assertTrue(_checkPointInsideMask(box3, rotated27090.getIterator()));

        shape.rotate(Math.PI / 2, -Math.PI / 2, 0);
        shape.translate(new long[] { 50, 50, 50 });
        IShape rotated90270 = shape.getTransformedShape();
        WritableBox box4 = GeomMasks.closedBox(new double[] { 50 - 3, 50 - 1, 50 + 0 },
                new double[] { 50 + 0, 50 + 0, 50 + 2 });
        assertTrue(_checkPointInsideMask(box4, rotated90270.getIterator()));
    }

    @Test
    public void closed3DBox_RotateAndTranslateHigherDimension_ReturnsCorrectCoordinates() {
        IShape shape = new ShapeFactory().closedBox(new long[] { 0, 0, 0, 2, 2 }, new long[] { 1, 2, 3, 2, 2 });

        shape.rotate(Math.PI / 2, Math.PI / 2, 0);
        shape.translate(new long[] { 50, 50, 50, 0, 0 });
        IShape rotated9090 = shape.getTransformedShape();
        WritableBox box1 = GeomMasks.closedBox(new double[] { 50, 50, 50, 2, 2 },
                new double[] { 50 + 3, 50 + 1, 50 + 2, 2, 2 });
        assertTrue(_checkPointInsideMask(box1, rotated9090.getIterator()));

        shape.rotate(-Math.PI / 2, -Math.PI / 2, 0);
        shape.translate(new long[] { 50, 50, 50, 0, 0 });
        IShape rotated270270 = shape.getTransformedShape();
        WritableBox box2 = GeomMasks.closedBox(new double[] { 50 + 0, 50 - 1, 50 - 2, 2, 2 },
                new double[] { 50 + 3, 50 + 0, 50 + 0, 2, 2 });
        assertTrue(_checkPointInsideMask(box2, rotated270270.getIterator()));

        shape.rotate(-Math.PI / 2, Math.PI / 2, 0);
        shape.translate(new long[] { 50, 50, 50, 0, 0 });
        IShape rotated27090 = shape.getTransformedShape();
        WritableBox box3 = GeomMasks.closedBox(new double[] { 50 - 3, 50 + 0, 50 - 2, 2, 2 },
                new double[] { 50 + 0, 50 + 1, 50 + 0, 2, 2 });
        assertTrue(_checkPointInsideMask(box3, rotated27090.getIterator()));

        shape.rotate(Math.PI / 2, -Math.PI / 2, 0);
        shape.translate(new long[] { 50, 50, 50, 0, 0 });
        IShape rotated90270 = shape.getTransformedShape();
        WritableBox box4 = GeomMasks.closedBox(new double[] { 50 - 3, 50 - 1, 50 + 0, 2, 2 },
                new double[] { 50 + 0, 50 + 0, 50 + 2, 2, 2 });
        assertTrue(_checkPointInsideMask(box4, rotated90270.getIterator()));
    }

    @Test
    public void closedPolygon2d_TriangleShape_ReturnsCorrectCoordinates() {
        long[] x = { 0, 2, 2 };
        long[] y = { 0, 0, 2 };

        IShape shape = new ShapeFactory().closedPolygon2D(x, y);
        Polygon2D polygon2d = GeomMasks.closedPolygon2D(new double[] { 0, 2, 2 }, new double[] { 0, 0, 2 });

        IShapeIterator iterator = shape.getIterator();
        assertTrue(_checkPointInsideMask(polygon2d, iterator));
    }

    @Test
    public void isInside_CheckForShapeAndRotatedShape_ReturnsCorrectResult() {
        IShape shape = new ShapeFactory().closedBox(new long[] { 0, 0 }, new long[] { 1, 2 });
        shape.rotate(0, Math.PI / 2, 0);
        shape.translate(new long[] { 0, 0 });
        IShape rotated90 = shape.getTransformedShape();
        rotated90.rotate(0, -Math.PI / 2, 0);
        rotated90.translate(new long[] { 0, 0 });
        IShape rotatedback = rotated90.getTransformedShape();

        assertTrue(shape.isInside(new long[] { 1, 1 }));
        assertTrue(rotated90.isInside(new long[] { -1, 1 }));
        assertTrue(!shape.isInside(new long[] { -1, 1 }));
        assertTrue(!rotated90.isInside(new long[] { 1, 1 }));
        assertTrue(!rotated90.isInside(new long[] { 1, 1, 1, 1 }));
        assertTrue(rotatedback.isInside(new long[] { 1, 1 }));
    }

    @Test
    public void and_AndTwoRectangles_ReturnsAndedShape() {
        IShape shapeWithin = new ShapeFactory().closedBox(new long[] { 0, 0 }, new long[] { 1, 2 });
        IShape shapeOutside = new ShapeFactory().closedBox(new long[] { 0, 0 }, new long[] { 4, 4 });

        IShape andResult = shapeWithin.and(shapeOutside);

        WritableBox andedShape = GeomMasks.closedBox(new double[] { 0, 0, }, new double[] { 1, 2 });
        assertTrue(_checkPointInsideMask(andedShape, andResult.getIterator()));

    }

    @Test
    public void and_AndTwoNonOverlappingRectangles_IteratorHasNoElements() {
        IShape shapeWithin = new ShapeFactory().closedBox(new long[] { 0, 0 }, new long[] { 1, 2 });
        IShape shapeOutside = new ShapeFactory().closedBox(new long[] { 2, 2 }, new long[] { 4, 4 });

        IShape andResult = shapeWithin.and(shapeOutside);

        assertTrue(!andResult.getIterator().hasNext());

    }

    private boolean _checkPointInsideMask(RealMaskRealInterval box, IShapeIterator iterator) {
        boolean equals = true;
        while (iterator.hasNext()) {
            double[] point = Arrays.stream(iterator.next()).asDoubleStream().toArray();
            PointMask pointMask = GeomMasks.pointMask(point);
            equals &= box.test(pointMask);

        }
        return equals;
    }
}