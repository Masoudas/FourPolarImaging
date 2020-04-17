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
        IShapeIteraror iterator = shape.getIterator();

        assertTrue(_checkPointInsideMask(box, iterator));

    }

    @Test
    public void closed2DBox_Rotate_ReturnsCorrectCoordinates() {
        IShape shape = new ShapeFactory().closedBox(new long[] { 0, 0 }, new long[] { 1, 2 });

        IShape rotated90 = shape.transform2D(new long[] { 0, 0 }, Math.PI / 2);
        WritableBox box1 = GeomMasks.closedBox(new double[] { -2, 0 }, new double[] { 0, 1 });
        assertTrue(_checkPointInsideMask(box1, rotated90.getIterator()));

        IShape rotated270 = shape.transform2D(new long[] { 0, 0 }, -Math.PI / 2);
        WritableBox box2 = GeomMasks.closedBox(new double[] { 0, -1 }, new double[] { 2, 0 });
        assertTrue(_checkPointInsideMask(box2, rotated270.getIterator()));
    }

    @Test
    public void closed2DBox_Shift_ReturnsCorrectCoordinates() {
        IShape shape = new ShapeFactory().closedBox(new long[] { 0, 0 }, new long[] { 1, 2 });

        IShape translated50 = shape.transform2D(new long[] { 50, 50 }, 0);
        WritableBox box1 = GeomMasks.closedBox(new double[] { 50, 50 }, new double[] { 50 + 1, 50 + 2 });
        assertTrue(_checkPointInsideMask(box1, translated50.getIterator()));

        IShape translated100 = shape.transform2D(new long[] { 100, 100 }, 0);
        WritableBox box2 = GeomMasks.closedBox(new double[] { 100, 100 }, new double[] { 100 + 1, 100 + 2 });
        assertTrue(_checkPointInsideMask(box2, translated100.getIterator()));
    }

    @Test
    public void closed2DBox_TranslateAndRotate_ReturnsCorrectCoordinates() {
        IShape shape = new ShapeFactory().closedBox(new long[] { 0, 0 }, new long[] { 1, 2 });

        IShape translated50rotate90 = shape.transform2D(new long[] { 50, 50 }, Math.PI / 2);
        WritableBox box1 = GeomMasks.closedBox(new double[] { 50 - 2, 50 - 0 }, new double[] { 50 + 0, 50 + 1 });
        assertTrue(_checkPointInsideMask(box1, translated50rotate90.getIterator()));

        IShape translated50rotate270 = shape.transform2D(new long[] { 50, 50 }, -Math.PI / 2);
        WritableBox box2 = GeomMasks.closedBox(new double[] { 50 - 0, 50 - 1 }, new double[] { 50 + 2, 50 + 0 });
        assertTrue(_checkPointInsideMask(box2, translated50rotate270.getIterator()));
    }

    @Test
    public void closed2DBox_HigherDimensionTranslateAndRotate_ReturnsCorrectCoordinates() {
        IShape shape = new ShapeFactory().closedBox(new long[] { 0, 0, 2, 2, 2 }, new long[] { 1, 2, 2, 2, 2 });

        IShape translated50rotate90 = shape.transform2D(new long[] { 50, 50 }, Math.PI / 2);
        WritableBox box1 = GeomMasks.closedBox(new double[] { 50 - 2, 50 - 0, 2, 2, 2 },
                new double[] { 50 + 0, 50 + 1, 2, 2, 2 });
        assertTrue(_checkPointInsideMask(box1, translated50rotate90.getIterator()));

        IShape translated50rotate270 = shape.transform2D(new long[] { 50, 50 }, -Math.PI / 2);
        WritableBox box2 = GeomMasks.closedBox(new double[] { 50 - 0, 50 - 1, 2, 2, 2 },
                new double[] { 50 + 2, 50 + 0, 2, 2, 2 });
        assertTrue(_checkPointInsideMask(box2, translated50rotate270.getIterator()));
    }

    @Test
    public void closed3DBox_zRotate_ReturnsCorrectCoordinates() {
        IShape shape = new ShapeFactory().closedBox(new long[] { 0, 0, 0 }, new long[] { 1, 2, 3 });

        IShape rotated90 = shape.transform3D(new long[] { 0, 0, 0 }, 0, Math.PI / 2, 0);
        WritableBox box1 = GeomMasks.closedBox(new double[] { -2, 0, 0 }, new double[] { 0, 1, 3 });
        assertTrue(_checkPointInsideMask(box1, rotated90.getIterator()));

        IShape rotated270 = shape.transform3D(new long[] { 0, 0, 0 }, 0, -Math.PI / 2, 0);
        WritableBox box2 = GeomMasks.closedBox(new double[] { 0, -1, 0 }, new double[] { 2, 0, 3 });
        assertTrue(_checkPointInsideMask(box2, rotated270.getIterator()));

    }

    @Test
    public void closed3DBox_xRotate_ReturnsCorrectCoordinates() {
        IShape shape = new ShapeFactory().closedBox(new long[] { 0, 0, 0 }, new long[] { 1, 2, 3 });

        IShape rotated90 = shape.transform3D(new long[] { 0, 0, 0 }, Math.PI / 2, 0, 0);
        WritableBox box1 = GeomMasks.closedBox(new double[] { 0, -3, 0 }, new double[] { 1, 0, 2 });
        assertTrue(_checkPointInsideMask(box1, rotated90.getIterator()));

        IShape rotated270 = shape.transform3D(new long[] { 0, 0, 0 }, -Math.PI / 2, 0, 0);
        WritableBox box2 = GeomMasks.closedBox(new double[] { 0, 0, -2 }, new double[] { 1, 3, 0 });
        assertTrue(_checkPointInsideMask(box2, rotated270.getIterator()));
    }

    @Test
    public void closed3DBox_xRotation90zrotation90_ReturnsCorrectCoordinates() {
        IShape shape = new ShapeFactory().closedBox(new long[] { 0, 0, 0 }, new long[] { 1, 2, 3 });

        IShape rotated9090 = shape.transform3D(new long[] { 0, 0, 0 }, Math.PI / 2, Math.PI / 2, 0);
        WritableBox box1 = GeomMasks.closedBox(new double[] { 0, 0, 0 }, new double[] { 3, 1, 2 });
        assertTrue(_checkPointInsideMask(box1, rotated9090.getIterator()));

        IShape rotated270270 = shape.transform3D(new long[] { 0, 0, 0 }, -Math.PI / 2, -Math.PI / 2, 0);
        WritableBox box2 = GeomMasks.closedBox(new double[] { 0, -1, -2 }, new double[] { 3, 0, 0 });
        assertTrue(_checkPointInsideMask(box2, rotated270270.getIterator()));

        IShape rotated27090 = shape.transform3D(new long[] { 0, 0, 0 }, -Math.PI / 2, Math.PI / 2, 0);
        WritableBox box3 = GeomMasks.closedBox(new double[] { -3, 0, -2 }, new double[] { 0, 1, 0 });
        assertTrue(_checkPointInsideMask(box3, rotated27090.getIterator()));

        IShape rotated90270 = shape.transform3D(new long[] { 0, 0, 0 }, Math.PI / 2, -Math.PI / 2, 0);
        WritableBox box4 = GeomMasks.closedBox(new double[] { -3, -1, 0 }, new double[] { 0, 0, 2 });
        assertTrue(_checkPointInsideMask(box4, rotated90270.getIterator()));

    }

    @Test
    public void closed3DBox_translate_ReturnsCorrectCoordinates() {
        IShape shape = new ShapeFactory().closedBox(new long[] { 0, 0, 0 }, new long[] { 1, 2, 3 });

        IShape translated50 = shape.transform3D(new long[] { 50, 50, 50 }, 0, 0, 0);
        WritableBox box1 = GeomMasks.closedBox(new double[] { 50, 50, 50 }, new double[] { 50 + 1, 50 + 2, 50 + 3 });
        assertTrue(_checkPointInsideMask(box1, translated50.getIterator()));

        IShape translated100 = shape.transform3D(new long[] { 100, 100, 100 }, 0, 0, 0);
        WritableBox box2 = GeomMasks.closedBox(new double[] { 100, 100, 100 },
                new double[] { 100 + 1, 100 + 2, 100 + 3 });
        assertTrue(_checkPointInsideMask(box2, translated100.getIterator()));

    }

    @Test
    public void closed3DBox_RotateAndTranslate_ReturnsCorrectCoordinates() {
        IShape shape = new ShapeFactory().closedBox(new long[] { 0, 0, 0 }, new long[] { 1, 2, 3 });

        IShape rotated9090 = shape.transform3D(new long[] { 50, 50, 50 }, Math.PI / 2, Math.PI / 2, 0);
        WritableBox box1 = GeomMasks.closedBox(new double[] { 50, 50, 50 }, new double[] { 50 + 3, 50 + 1, 50 + 2 });
        assertTrue(_checkPointInsideMask(box1, rotated9090.getIterator()));

        IShape rotated270270 = shape.transform3D(new long[] { 50, 50, 50 }, -Math.PI / 2, -Math.PI / 2, 0);
        WritableBox box2 = GeomMasks.closedBox(new double[] { 50 + 0, 50 - 1, 50 - 2 },
                new double[] { 50 + 3, 50 + 0, 50 + 0 });
        assertTrue(_checkPointInsideMask(box2, rotated270270.getIterator()));

        IShape rotated27090 = shape.transform3D(new long[] { 50, 50, 50 }, -Math.PI / 2, Math.PI / 2, 0);
        WritableBox box3 = GeomMasks.closedBox(new double[] { 50 - 3, 50 + 0, 50 - 2 },
                new double[] { 50 + 0, 50 + 1, 50 + 0 });
        assertTrue(_checkPointInsideMask(box3, rotated27090.getIterator()));

        IShape rotated90270 = shape.transform3D(new long[] { 50, 50, 50 }, Math.PI / 2, -Math.PI / 2, 0);
        WritableBox box4 = GeomMasks.closedBox(new double[] { 50 - 3, 50 - 1, 50 + 0 },
                new double[] { 50 + 0, 50 + 0, 50 + 2 });
        assertTrue(_checkPointInsideMask(box4, rotated90270.getIterator()));
    }

    @Test
    public void closed3DBox_RotateAndTranslateHigherDimension_ReturnsCorrectCoordinates() {
        IShape shape = new ShapeFactory().closedBox(new long[] { 0, 0, 0, 2, 2 }, new long[] { 1, 2, 3, 2, 2 });

        IShape rotated9090 = shape.transform3D(new long[] { 50, 50, 50 }, Math.PI / 2, Math.PI / 2, 0);
        WritableBox box1 = GeomMasks.closedBox(new double[] { 50, 50, 50, 2, 2 },
                new double[] { 50 + 3, 50 + 1, 50 + 2, 2, 2 });
        assertTrue(_checkPointInsideMask(box1, rotated9090.getIterator()));

        IShape rotated270270 = shape.transform3D(new long[] { 50, 50, 50 }, -Math.PI / 2, -Math.PI / 2, 0);
        WritableBox box2 = GeomMasks.closedBox(new double[] { 50 + 0, 50 - 1, 50 - 2, 2, 2 },
                new double[] { 50 + 3, 50 + 0, 50 + 0, 2, 2 });
        assertTrue(_checkPointInsideMask(box2, rotated270270.getIterator()));

        IShape rotated27090 = shape.transform3D(new long[] { 50, 50, 50 }, -Math.PI / 2, Math.PI / 2, 0);
        WritableBox box3 = GeomMasks.closedBox(new double[] { 50 - 3, 50 + 0, 50 - 2, 2, 2 },
                new double[] { 50 + 0, 50 + 1, 50 + 0, 2, 2 });
        assertTrue(_checkPointInsideMask(box3, rotated27090.getIterator()));

        IShape rotated90270 = shape.transform3D(new long[] { 50, 50, 50 }, Math.PI / 2, -Math.PI / 2, 0);
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

        IShapeIteraror iterator = shape.getIterator();
        assertTrue(_checkPointInsideMask(polygon2d, iterator));
    }

    @Test
    public void isInside_CheckForShapeAndRotatedShape_ReturnsCorrectResult() {
        IShape shape = new ShapeFactory().closedBox(new long[] { 0, 0 }, new long[] { 1, 2 });
        IShape rotated90 = shape.transform2D(new long[] { 0, 0 }, Math.PI / 2);
        IShape rotatedback = rotated90.transform2D(new long[] { 0, 0 }, -Math.PI / 2);

        assertTrue(shape.isInside(new long[] { 1, 1 }));
        assertTrue(rotated90.isInside(new long[] { -1, 1 }));
        assertTrue(!shape.isInside(new long[] { -1, 1 }));
        assertTrue(!rotated90.isInside(new long[] { 1, 1 }));
        assertTrue(!rotated90.isInside(new long[] { 1, 1, 1, 1 }));
        assertTrue(rotatedback.isInside(new long[] { 1, 1 }));
    }
    

    private boolean _checkPointInsideMask(RealMaskRealInterval box, IShapeIteraror iterator) {
        boolean equals = true;
        while (iterator.hasNext()) {
            double[] point = Arrays.stream(iterator.next()).asDoubleStream().toArray();
            PointMask pointMask = GeomMasks.pointMask(point);
            equals &= box.test(pointMask);

        }
        return equals;
    }
}