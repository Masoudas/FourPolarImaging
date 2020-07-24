package fr.fresnel.fourPolar.core.shape;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import net.imglib2.Cursor;
import net.imglib2.roi.Masks;
import net.imglib2.roi.RealMaskRealInterval;
import net.imglib2.roi.Regions;
import net.imglib2.roi.geom.GeomMasks;
import net.imglib2.roi.geom.real.PointMask;
import net.imglib2.roi.geom.real.Polygon2D;
import net.imglib2.roi.geom.real.WritableBox;
import net.imglib2.util.Intervals;
import net.imglib2.view.Views;

public class ShapeFactoryTest {

    @Test
    public void closedBox_RectangularBox_ReturnsCorrectPoints() {
        long[] min = { 1, 1, 1 };
        long[] max = { 2, 2, 2 };
        IShape shape = ShapeFactory.closedBox(min, max, AxisOrder.XYZ);

        WritableBox box = GeomMasks.closedBox(new double[] { 1, 1, 1 }, new double[] { 2, 2, 2 });
        IShapeIterator iterator = shape.getIterator();

        assertTrue(_checkPointInsideMask(box, iterator));

    }


    @Test
    public void closedPolygon2d_TriangleShape_ReturnsCorrectCoordinates() {
        long[] x = { 0, 2, 2 };
        long[] y = { 0, 0, 2 };

        IShape shape = ShapeFactory.closedPolygon2D(x, y);
        Polygon2D polygon2d = GeomMasks.closedPolygon2D(new double[] { 0, 2, 2 }, new double[] { 0, 0, 2 });

        IShapeIterator iterator = shape.getIterator();
        assertTrue(_checkPointInsideMask(polygon2d, iterator));
    }

    @Test
    public void isInside_CheckForShapeAndRotatedShape_ReturnsCorrectResult() {
        IShape shape = ShapeFactory.closedBox(new long[] { 0, 0 }, new long[] { 1, 2 }, AxisOrder.XY);
        shape.rotate2D(Math.PI / 2);

        assertTrue(!shape.isInside(new long[] { 1, 1 }));
        assertTrue(shape.isInside(new long[] { -1, 1 }));
    }

    @Test
    public void and_AndTwoRectangles_ReturnsAndedShape() {
        IShape shapeWithin = ShapeFactory.closedBox(new long[] { 0, 0, 0 }, new long[] { 1, 2, 2 }, AxisOrder.XYZ);
        IShape shapeOutside = ShapeFactory.closedBox(new long[] { 0, 0, 0 }, new long[] { 4, 4, 4 }, AxisOrder.XYZ);

        shapeWithin.and(shapeOutside);

        WritableBox andedShape = GeomMasks.closedBox(new double[] { 0, 0, 0 }, new double[] { 1, 2, 2 });
        assertTrue(_checkPointInsideMask(andedShape, shapeWithin.getIterator()));

    }

    @Test
    public void and_AndBoxWithRectangle_ReturnsRectangle() {
        IShape box = ShapeFactory.closedBox(new long[] { 0, 0, 0 }, new long[] { 1, 2, 2 }, AxisOrder.XYZ);
        IShape rectangle = ShapeFactory.closedBox(new long[] { 0, 0, 0 }, new long[] { 1, 1, 0 }, AxisOrder.XYZ);

        rectangle.and(box);

        WritableBox andedShape = GeomMasks.closedBox(new double[] { 0, 0, 0 }, new double[] { 1, 1, 0 });
        assertTrue(_checkPointInsideMask(andedShape, rectangle.getIterator()));

    }

    @Test
    public void and_AndTwoNonOverlappingRectangles_IteratorHasNoElements() {
        IShape shapeWithin = ShapeFactory.closedBox(new long[] { 0, 0 }, new long[] { 1, 2 }, AxisOrder.XY);
        IShape shapeOutside = ShapeFactory.closedBox(new long[] { 2, 2 }, new long[] { 4, 4 }, AxisOrder.XY);

        IShape andedShape = shapeWithin.and(shapeOutside);

        assertTrue(!andedShape.getIterator().hasNext());

    }

    @Test
    public void isInside_LesserDimensionPointThanShapeDim_FalseIsReturned() {
        IShape shape2D = ShapeFactory.closedBox(new long[] { 0, 0 }, new long[] { 1, 2 }, AxisOrder.XY);
        assertThrows(IllegalArgumentException.class, ()->{shape2D.isInside(new long[] { 1 });});
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