package fr.fresnel.fourPolar.core.util.shape;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.physics.axis.AxisOrder;
import net.imglib2.Cursor;
import net.imglib2.roi.Masks;
import net.imglib2.roi.RealMaskRealInterval;
import net.imglib2.roi.Regions;
import net.imglib2.roi.geom.GeomMasks;
import net.imglib2.roi.geom.real.PointMask;
import net.imglib2.roi.geom.real.WritableBox;
import net.imglib2.util.Intervals;
import net.imglib2.view.Views;

public class ShapeTest {
    @Test
    public void rotate2D_closed2DBox_ReturnsCorrectCoordinates() {
        IShape shape = new ShapeFactory().closedBox(new long[] { 0, 0 }, new long[] { 1, 2 }, AxisOrder.XY);

        shape.rotate2D(Math.PI / 2);
        IShapeIterator iterator = shape.getIterator();
        WritableBox box1 = GeomMasks.closedBox(new double[] { -2, 0 }, new double[] { 0, 1 });
        assertTrue(_checkPointInsideMask(box1, iterator));

        shape.resetToOriginalShape();
        shape.rotate2D(-Math.PI / 2);
        WritableBox box2 = GeomMasks.closedBox(new double[] { 0, -1 }, new double[] { 2, 0 });
        assertTrue(_checkPointInsideMask(box2, shape.getIterator()));
    }

    @Test
    public void translate_closed2DBox__ReturnsCorrectCoordinates() {
        IShape shape = new ShapeFactory().closedBox(new long[] { 0, 0 }, new long[] { 1, 2 }, AxisOrder.XY);

        shape.translate(new long[] { 50, 50 });
        WritableBox box1 = GeomMasks.closedBox(new double[] { 50, 50 }, new double[] { 50 + 1, 50 + 2 });
        assertTrue(_checkPointInsideMask(box1, shape.getIterator()));

        shape.resetToOriginalShape();
        shape.translate(new long[] { 100, 100 });
        WritableBox box2 = GeomMasks.closedBox(new double[] { 100, 100 }, new double[] { 100 + 1, 100 + 2 });
        assertTrue(_checkPointInsideMask(box2, shape.getIterator()));
    }

    @Test
    public void translateAndRotate_closed2DBox_ReturnsCorrectCoordinates() {
        IShape shape = new ShapeFactory().closedBox(new long[] { 0, 0 }, new long[] { 1, 2 }, AxisOrder.XY);

        shape.rotate2D(Math.PI / 2);
        shape.translate(new long[] { 50, 50 });
        WritableBox box1 = GeomMasks.closedBox(new double[] { 50 - 2, 50 - 0 }, new double[] { 50 + 0, 50 + 1 });
        assertTrue(_checkPointInsideMask(box1, shape.getIterator()));

        shape.resetToOriginalShape();
        shape.rotate2D(-Math.PI / 2);
        shape.translate(new long[] { 50, 50 });
        WritableBox box2 = GeomMasks.closedBox(new double[] { 50 - 0, 50 - 1 }, new double[] { 50 + 2, 50 + 0 });
        assertTrue(_checkPointInsideMask(box2, shape.getIterator()));
    }

    @Test
    public void translateAndRotate_HigherDimensionClosed2DBox_ReturnsCorrectCoordinates() {
        IShape shape = new ShapeFactory().closedBox(new long[] { 0, 0, 2, 2, 2 }, new long[] { 1, 2, 2, 2, 2 },
                AxisOrder.XYCZT);

        shape.rotate2D(Math.PI / 2);
        shape.translate(new long[] { 50, 50, 0, 0, 0 });
        WritableBox box1 = GeomMasks.closedBox(new double[] { 50 - 2, 50 - 0, 2, 2, 2 },
                new double[] { 50 + 0, 50 + 1, 2, 2, 2 });
        assertTrue(_checkPointInsideMask(box1, shape.getIterator()));

        shape.resetToOriginalShape();
        shape.rotate2D(-Math.PI / 2);
        shape.translate(new long[] { 50, 50, 0, 0, 0 });
        WritableBox box2 = GeomMasks.closedBox(new double[] { 50 - 0, 50 - 1, 2, 2, 2 },
                new double[] { 50 + 2, 50 + 0, 2, 2, 2 });
        assertTrue(_checkPointInsideMask(box2, shape.getIterator()));
    }

    @Test
    public void rotate3d_closed3DBoxZAxis_ReturnsCorrectCoordinates() {
        IShape shape = new ShapeFactory().closedBox(new long[] { 0, 0, 0 }, new long[] { 1, 2, 3 }, AxisOrder.XYZ);

        shape.rotate3D(Math.PI / 2, 0, 0, new int[] { 2, 0, 1 });
        WritableBox box1 = GeomMasks.closedBox(new double[] { -2, 0, 0 }, new double[] { 0, 1, 3 });
        assertTrue(_checkPointInsideMask(box1, shape.getIterator()));

        shape.resetToOriginalShape();
        shape.rotate3D(-Math.PI / 2, 0, 0, new int[] { 2, 1, 0 });
        WritableBox box2 = GeomMasks.closedBox(new double[] { 0, -1, 0 }, new double[] { 2, 0, 3 });
        assertTrue(_checkPointInsideMask(box2, shape.getIterator()));

    }

    @Test
    public void rotate3D_closed3DBoxZAxisRotateAxisOrderXYCZT_ReturnsCorrectCoordinates() {
        IShape shapeNewOrder = new ShapeFactory().closedBox(new long[] { 0, 0, 0, 0, 0 }, new long[] { 1, 2, 0, 3, 0 },
                AxisOrder.XYCZT);

        shapeNewOrder.rotate3D(Math.PI / 2, 0, 0, new int[] { 2, 0, 1 });
        WritableBox boxNewOrder1 = GeomMasks.closedBox(new double[] { -2, 0, 0, 0, 0 }, new double[] { 0, 1, 0, 3, 0 });
        assertTrue(_checkPointInsideMask(boxNewOrder1, shapeNewOrder.getIterator()));

        shapeNewOrder.resetToOriginalShape();
        shapeNewOrder.rotate3D(-Math.PI / 2, 0, 0, new int[] { 2, 1, 0 });
        WritableBox boxNewOrder2 = GeomMasks.closedBox(new double[] { 0, -1, 0, 0, 0 }, new double[] { 2, 0, 0, 3, 0 });
        assertTrue(_checkPointInsideMask(boxNewOrder2, shapeNewOrder.getIterator()));

    }

    @Test
    public void rotate3d_closed3DBoxXAxis_ReturnsCorrectCoordinates() {
        IShape shape = new ShapeFactory().closedBox(new long[] { 0, 0, 0 }, new long[] { 1, 2, 3 }, AxisOrder.XYZ);

        shape.rotate3D(Math.PI / 2, 0, 0, new int[] { 0, 2, 1 });
        WritableBox box1 = GeomMasks.closedBox(new double[] { 0, -3, 0 }, new double[] { 1, 0, 2 });
        assertTrue(_checkPointInsideMask(box1, shape.getIterator()));

        shape.resetToOriginalShape();
        shape.rotate3D(-Math.PI / 2, 0, 0, new int[] { 0, 1, 2 });
        WritableBox box2 = GeomMasks.closedBox(new double[] { 0, 0, -2 }, new double[] { 1, 3, 0 });
        assertTrue(_checkPointInsideMask(box2, shape.getIterator()));
    }

    @Test
    public void rotate3D_closed3DBoxXRotateAxisOrderXYCZT_ReturnsCorrectCoordinates() {
        IShape shape = new ShapeFactory().closedBox(new long[] { 0, 0, 0, 0, 0 }, new long[] { 1, 2, 0, 3, 0 },
                AxisOrder.XYCZT);

        shape.rotate3D(Math.PI / 2, 0, 0, new int[] { 0, 2, 1 });
        WritableBox box1 = GeomMasks.closedBox(new double[] { 0, -3, 0, 0, 0 }, new double[] { 1, 0, 0, 2, 0 });
        assertTrue(_checkPointInsideMask(box1, shape.getIterator()));

        shape.resetToOriginalShape();
        shape.rotate3D(-Math.PI / 2, 0, 0, new int[] { 0, 1, 2 });
        WritableBox box2 = GeomMasks.closedBox(new double[] { 0, 0, 0, -2, 0 }, new double[] { 1, 3, 0, 0, 0 });
        assertTrue(_checkPointInsideMask(box2, shape.getIterator()));
    }

    @Test
    public void rotate3D_closed3DBoxXRotation90zrotation90_ReturnsCorrectCoordinates() {
        IShape shape = new ShapeFactory().closedBox(new long[] { 0, 0, 0 }, new long[] { 1, 2, 3 }, AxisOrder.XYZ);

        shape.rotate3D(Math.PI / 2, Math.PI / 2, 0, new int[] { 0, 2, 1 });
        WritableBox box1 = GeomMasks.closedBox(new double[] { 0, 0, 0 }, new double[] { 3, 1, 2 });
        assertTrue(_checkPointInsideMask(box1, shape.getIterator()));

        shape.resetToOriginalShape();
        shape.rotate3D(-Math.PI / 2, -Math.PI / 2, 0, new int[] { 0, 2, 1 });
        WritableBox box2 = GeomMasks.closedBox(new double[] { 0, -1, -2 }, new double[] { 3, 0, 0 });
        assertTrue(_checkPointInsideMask(box2, shape.getIterator()));

        shape.resetToOriginalShape();
        shape.rotate3D(-Math.PI / 2, Math.PI / 2, 0, new int[] { 0, 2, 1 });
        WritableBox box3 = GeomMasks.closedBox(new double[] { -3, 0, -2 }, new double[] { 0, 1, 0 });
        assertTrue(_checkPointInsideMask(box3, shape.getIterator()));

        shape.resetToOriginalShape();
        shape.rotate3D(Math.PI / 2, -Math.PI / 2, 0, new int[] { 0, 2, 1 });
        WritableBox box4 = GeomMasks.closedBox(new double[] { -3, -1, 0 }, new double[] { 0, 0, 2 });
        assertTrue(_checkPointInsideMask(box4, shape.getIterator()));

    }

    @Test
    public void rotate3D_closed3DBoxXRotation90zrotation90AxisOrderXYCZT_ReturnsCorrectCoordinates() {
        IShape shape = new ShapeFactory().closedBox(new long[] { 0, 0, 0, 0, 0 }, new long[] { 1, 2, 1, 3, 1 },
                AxisOrder.XYCZT);

        shape.rotate3D(Math.PI / 2, Math.PI / 2, 0, new int[] { 0, 2, 1 });
        WritableBox box1 = GeomMasks.closedBox(new double[] { 0, 0, 0, 0, 0 }, new double[] { 3, 1, 1, 2, 1 });
        assertTrue(_checkPointInsideMask(box1, shape.getIterator()));

        shape.resetToOriginalShape();
        shape.rotate3D(-Math.PI / 2, -Math.PI / 2, 0, new int[] { 0, 2, 1 });
        WritableBox box2 = GeomMasks.closedBox(new double[] { 0, -1, 0, -2, 0 }, new double[] { 3, 0, 1, 0, 1 });
        assertTrue(_checkPointInsideMask(box2, shape.getIterator()));

        shape.resetToOriginalShape();
        shape.rotate3D(-Math.PI / 2, Math.PI / 2, 0, new int[] { 0, 2, 1 });
        WritableBox box3 = GeomMasks.closedBox(new double[] { -3, 0, 0, -2, 0 }, new double[] { 0, 1, 1, 0, 1 });
        assertTrue(_checkPointInsideMask(box3, shape.getIterator()));

        shape.resetToOriginalShape();
        shape.rotate3D(Math.PI / 2, -Math.PI / 2, 0, new int[] { 0, 2, 1 });
        WritableBox box4 = GeomMasks.closedBox(new double[] { -3, -1, 0, 0, 0 }, new double[] { 0, 0, 1, 2, 1 });
        assertTrue(_checkPointInsideMask(box4, shape.getIterator()));

    }

    @Test
    public void translate_Closed3DBox_ReturnsCorrectCoordinates() {
        IShape shape = new ShapeFactory().closedBox(new long[] { 0, 0, 0 }, new long[] { 1, 2, 3 }, AxisOrder.XYZ);

        shape.translate(new long[] { 50, 50, 50 });
        WritableBox box1 = GeomMasks.closedBox(new double[] { 50, 50, 50 }, new double[] { 50 + 1, 50 + 2, 50 + 3 });
        assertTrue(_checkPointInsideMask(box1, shape.getIterator()));

        shape.resetToOriginalShape();
        shape.translate(new long[] { 100, 100, 100 });
        WritableBox box2 = GeomMasks.closedBox(new double[] { 100, 100, 100 },
                new double[] { 100 + 1, 100 + 2, 100 + 3 });
        assertTrue(_checkPointInsideMask(box2, shape.getIterator()));

    }

    @Test
    public void rotateAndTranslate_ClosedBox3D_ReturnsCorrectCoordinates() {
        IShape shape = new ShapeFactory().closedBox(new long[] { 0, 0, 0 }, new long[] { 1, 2, 3 }, AxisOrder.XYZ);

        shape.rotate3D(Math.PI / 2, Math.PI / 2, 0, new int[] { 0, 2, 1 });
        shape.translate(new long[] { 50, 50, 50 });
        WritableBox box1 = GeomMasks.closedBox(new double[] { 50, 50, 50 }, new double[] { 50 + 3, 50 + 1, 50 + 2 });
        assertTrue(_checkPointInsideMask(box1, shape.getIterator()));

        shape.resetToOriginalShape();
        shape.rotate3D(-Math.PI / 2, -Math.PI / 2, 0, new int[] { 0, 2, 1 });
        shape.translate(new long[] { 50, 50, 50 });
        WritableBox box2 = GeomMasks.closedBox(new double[] { 50 + 0, 50 - 1, 50 - 2 },
                new double[] { 50 + 3, 50 + 0, 50 + 0 });
        assertTrue(_checkPointInsideMask(box2, shape.getIterator()));

        shape.resetToOriginalShape();
        shape.rotate3D(-Math.PI / 2, Math.PI / 2, 0, new int[] { 0, 2, 1 });
        shape.translate(new long[] { 50, 50, 50 });
        WritableBox box3 = GeomMasks.closedBox(new double[] { 50 - 3, 50 + 0, 50 - 2 },
                new double[] { 50 + 0, 50 + 1, 50 + 0 });
        assertTrue(_checkPointInsideMask(box3, shape.getIterator()));

        shape.resetToOriginalShape();
        shape.rotate3D(Math.PI / 2, -Math.PI / 2, 0, new int[] { 0, 2, 1 });
        shape.translate(new long[] { 50, 50, 50 });
        WritableBox box4 = GeomMasks.closedBox(new double[] { 50 - 3, 50 - 1, 50 + 0 },
                new double[] { 50 + 0, 50 + 0, 50 + 2 });
        assertTrue(_checkPointInsideMask(box4, shape.getIterator()));
    }

    @Test
    public void rotateAndTranslate_closed3DBoxAxisOrderXYCZT_ReturnsCorrectCoordinates() {
        IShape shape = new ShapeFactory().closedBox(new long[] { 0, 0, 0, 0, 0 }, new long[] { 1, 2, 1, 3, 1 },
                AxisOrder.XYCZT);

        shape.rotate3D(Math.PI / 2, Math.PI / 2, 0, new int[] { 0, 2, 1 });
        shape.translate(new long[] { 50, 50, 50, 50, 50 });
        WritableBox box1 = GeomMasks.closedBox(new double[] { 50, 50, 50, 50, 50 },
                new double[] { 50 + 3, 50 + 1, 51, 50 + 2, 51 });
        assertTrue(_checkPointInsideMask(box1, shape.getIterator()));

        shape.resetToOriginalShape();
        shape.rotate3D(-Math.PI / 2, -Math.PI / 2, 0, new int[] { 0, 2, 1 });
        shape.translate(new long[] { 50, 50, 50, 50, 50 });
        WritableBox box2 = GeomMasks.closedBox(new double[] { 50 + 0, 50 - 1, 50, 50 - 2, 50 },
                new double[] { 50 + 3, 50 + 0, 51, 50 + 0, 51 });
        assertTrue(_checkPointInsideMask(box2, shape.getIterator()));

        shape.resetToOriginalShape();
        shape.rotate3D(-Math.PI / 2, Math.PI / 2, 0, new int[] { 0, 2, 1 });
        shape.translate(new long[] { 50, 50, 50, 50, 50 });
        WritableBox box3 = GeomMasks.closedBox(new double[] { 50 - 3, 50 + 0, 50, 50 - 2, 50 },
                new double[] { 50 + 0, 50 + 1, 51, 50 + 0, 51 });
        assertTrue(_checkPointInsideMask(box3, shape.getIterator()));

        shape.resetToOriginalShape();
        shape.rotate3D(Math.PI / 2, -Math.PI / 2, 0, new int[] { 0, 2, 1 });
        shape.translate(new long[] { 50, 50, 50, 50, 50 });
        WritableBox box4 = GeomMasks.closedBox(new double[] { 50 - 3, 50 - 1, 50, 50 + 0, 50 },
                new double[] { 50 + 0, 50 + 0, 51, 50 + 2, 51 });
        assertTrue(_checkPointInsideMask(box4, shape.getIterator()));
    }

    private boolean _checkPointInsideMask(RealMaskRealInterval box, IShapeIterator iterator)
            throws NoSuchElementException {
        final Cursor<Void> iterableRegion = Regions.iterable(Views
                .interval(Views.raster(Masks.toRealRandomAccessible(box)), Intervals.largestContainedInterval(box)))
                .cursor();

        // If not equal, an exception is throws
        while (iterableRegion.hasNext() || iterator.hasNext()) {
            iterator.next();
            iterableRegion.next();
        }

        iterator.reset();
        boolean equals = true;
        while (iterator.hasNext()) {
            double[] point = Arrays.stream(iterator.next()).asDoubleStream().toArray();
            PointMask pointMask = GeomMasks.pointMask(point);
            equals &= box.test(pointMask);

        }
        return equals;
    }

}