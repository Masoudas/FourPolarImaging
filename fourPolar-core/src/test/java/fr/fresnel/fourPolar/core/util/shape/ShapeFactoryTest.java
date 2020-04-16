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
    public void closed2DBox_Rotation90_ReturnsCorrectCoordinates() {
        long[] center = { 0, 0 };

        IShape shape = new ShapeFactory().closed2DBox(center, 4, 2, Math.PI / 2);
        WritableBox box = GeomMasks.closedBox(new double[] { -1, -2 }, new double[] { 1, 2 });

        IShapeIteraror iterator = shape.getIterator();
        assertTrue(_checkPointInsideMask(box, iterator));
    }

    @Test
    public void closed2DBox_WithShift_ReturnsCorrectCoordinates() {
        long[] center = { 50, 50 };

        IShape shape = new ShapeFactory().closed2DBox(center, 4, 2, 0);
        WritableBox box = GeomMasks.closedBox(new double[] { 50 - 2, 50 - 1 }, new double[] { 50 + 2, 50 + 1 });

        IShapeIteraror iterator = shape.getIterator();
        assertTrue(_checkPointInsideMask(box, iterator));
    }

    @Test
    public void closed2DBox_Rotation90WithShift_ReturnsCorrectCoordinates() {
        long[] center = { 50, 50 };

        IShape shape = new ShapeFactory().closed2DBox(center, 4, 2, Math.PI / 2);
        WritableBox box = GeomMasks.closedBox(new double[] { 50 - 1, 50 - 2 }, new double[] { 50 + 1, 50 + 2 });

        IShapeIteraror iterator = shape.getIterator();
        assertTrue(_checkPointInsideMask(box, iterator));
    }

    @Test
    public void closed3DBox_zRotation90_ReturnsCorrectCoordinates() {
        long[] center = { 0, 0, 0 };

        IShape shape = new ShapeFactory().closed3DBox(center, 4, 2, 6, Math.PI / 2, 0);
        WritableBox box = GeomMasks.closedBox(new double[] { -2, -3, -1 }, new double[] { 2, 3, 1 });

        IShapeIteraror iterator = shape.getIterator();
        assertTrue(_checkPointInsideMask(box, iterator));

    }

    @Test
    public void closed3DBox_xRotation90_ReturnsCorrectCoordinates() {
        long[] center = { 0, 0, 0 };

        IShape shape = new ShapeFactory().closed3DBox(center, 4, 2, 6, 0, Math.PI / 2);
        WritableBox box = GeomMasks.closedBox(new double[] { -1, -2, -3 }, new double[] { 1, 2, 3 });

        IShapeIteraror iterator = shape.getIterator();
        assertTrue(_checkPointInsideMask(box, iterator));

    }

    @Test
    public void closed3DBox_xRotation90zrotation90_ReturnsCorrectCoordinates() {
        long[] center = { 0, 0, 0 };

        IShape shape = new ShapeFactory().closed3DBox(center, 4, 2, 6, Math.PI / 2, Math.PI / 2);
        WritableBox box = GeomMasks.closedBox(new double[] { -3, -2, -1 }, new double[] { 3, 2, 1 });

        IShapeIteraror iterator = shape.getIterator();
        assertTrue(_checkPointInsideMask(box, iterator));

    }

    @Test
    public void closed3DBox_50Shift_ReturnsCorrectCoordinates() {
        long[] center = { 50, 50, 50 };

        IShape shape = new ShapeFactory().closed3DBox(center, 4, 2, 4, 0, 0);
        WritableBox box = GeomMasks.closedBox(new double[] { 50 - 2, 50 - 1, 50 - 2 },
                new double[] { 50 + 2, 50 + 1, 50 + 2 });

        IShapeIteraror iterator = shape.getIterator();
        assertTrue(_checkPointInsideMask(box, iterator));

    }

    @Test
    public void closed3DBox_xRotation90With50Shift_ReturnsCorrectCoordinates() {
        long[] center = { 50, 50, 50 };

        IShape shape = new ShapeFactory().closed3DBox(center, 4, 2, 6, Math.PI / 2, 0);
        WritableBox box = GeomMasks.closedBox(new double[] { 50 - 2, 50 - 3, 50 - 1 },
                new double[] { 50 + 2, 50 + 3, 50 + 1 });

        IShapeIteraror iterator = shape.getIterator();
        assertTrue(_checkPointInsideMask(box, iterator));
    }

    @Test
    public void closed3DBox_zRotation90With50Shift_ReturnsCorrectCoordinates() {
        long[] center = { 50, 50, 50 };

        IShape shape = new ShapeFactory().closed3DBox(center, 4, 2, 6, 0, Math.PI / 2);
        WritableBox box = GeomMasks.closedBox(new double[] { 50 - 1, 50 - 2, 50 - 3 },
                new double[] { 50 + 1, 50 + 2, 50 + 3 });

        IShapeIteraror iterator = shape.getIterator();
        assertTrue(_checkPointInsideMask(box, iterator));
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

    public void polygon2D() {

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