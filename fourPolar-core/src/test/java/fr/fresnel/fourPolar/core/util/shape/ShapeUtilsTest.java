package fr.fresnel.fourPolar.core.util.shape;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import javassist.bytecode.analysis.Util;
import net.imglib2.roi.RealMaskRealInterval;
import net.imglib2.roi.geom.GeomMasks;
import net.imglib2.roi.geom.real.PointMask;
import net.imglib2.roi.geom.real.WritableBox;

public class ShapeUtilsTest {
    @Test
    public void and_AndTwoRectangles_ReturnsAndedShape() {
        IShape shapeWithin = new ShapeFactory().closedBox(new long[] { 0, 0, 0 }, new long[] { 1, 2, 2 });
        IShape shapeOutside = new ShapeFactory().closedBox(new long[] { 0, 0, 0 }, new long[] { 4, 4, 4 });

        ShapeUtils.and(shapeOutside, shapeWithin);

        WritableBox andedShape = GeomMasks.closedBox(new double[] { 0, 0, 0 }, new double[] { 1, 2, 2 });
        assertTrue(_checkPointInsideMask(andedShape, shapeWithin.getIterator()));

    }

    @Test
    public void and_AndBoxWithRectangle_ReturnsRectangle() {
        IShape box = new ShapeFactory().closedBox(new long[] { 0, 0, 0 }, new long[] { 1, 2, 2 });
        IShape rectangle = new ShapeFactory().closedBox(new long[] { 0, 0, 0 }, new long[] { 1, 1, 0 });

        ShapeUtils.and(box, rectangle);

        WritableBox andedShape = GeomMasks.closedBox(new double[] { 0, 0, 0 }, new double[] { 1, 1, 0 });
        assertTrue(_checkPointInsideMask(andedShape, rectangle.getIterator()));

    }

    @Test
    public void and_AndTwoNonOverlappingRectangles_IteratorHasNoElements() {
        IShape shapeWithin = new ShapeFactory().closedBox(new long[] { 0, 0 }, new long[] { 1, 2 });
        IShape shapeOutside = new ShapeFactory().closedBox(new long[] { 2, 2 }, new long[] { 4, 4 });

        ShapeUtils.and(shapeOutside, shapeWithin);

        assertTrue(!shapeWithin.getIterator().hasNext());

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