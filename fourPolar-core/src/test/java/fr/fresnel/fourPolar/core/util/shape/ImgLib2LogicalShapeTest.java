package fr.fresnel.fourPolar.core.util.shape;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import net.imglib2.roi.geom.GeomMasks;
import net.imglib2.roi.geom.real.PointMask;

public class ImgLib2LogicalShapeTest {
    @Test
    public void createAndedShape_AndTwoPoints_ReturnsSamePointOrNull() {
        PointMask point1 = GeomMasks.pointMask(new double[] { 1, 1 });
        PointMask point2 = GeomMasks.pointMask(new double[] { 1, 2 });

        IShape anded1 = ImgLib2LogicalShape.createAndedShape(new ImgLib2Shape(point1, AxisOrder.XY),
                new ImgLib2Shape(point1, AxisOrder.XY));

        assertArrayEquals(anded1.getIterator().next(), new long[] { 1, 1 });

        IShape anded2 = ImgLib2LogicalShape.createAndedShape(new ImgLib2Shape(point1, AxisOrder.XY),
        new ImgLib2Shape(point2, AxisOrder.XY));

        assertTrue(!anded2.getIterator().hasNext());


    }

}