package fr.fresnel.fourPolar.core.util.shape;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.shape.ImgLib2Shape;
import fr.fresnel.fourPolar.core.shape.ImgLib2ShapeIterator;
import net.imglib2.roi.geom.GeomMasks;
import net.imglib2.roi.geom.real.PointMask;
import net.imglib2.roi.geom.real.WritableBox;

/**
 * Note that translation and rotation are tested with the box shape.
 */
public class ImgLib2ShapeTest {
    @Test
    public void init_ShapeDimAndAxisNumUnequal_ThrowsIllegalArgumentException() {
        PointMask point = GeomMasks.pointMask(new double[] { 1 });

        assertThrows(IllegalArgumentException.class, () -> {
            new ImgLib2Shape(point, AxisOrder.XY, 1);
        });
    }

    @Test
    public void isInside_PointsWithSameAndDifferentDimAsShape_ReturnsTrueAndFalseRespectively() {
        WritableBox box = GeomMasks.closedBox(new double[] { 0, 0 }, new double[] { 1, 1 });

        ImgLib2Shape shape = new ImgLib2Shape(box, AxisOrder.XY, 1);

        assertTrue(shape.isInside(new long[] { 0, 0 }));
        assertTrue(!shape.isInside(new long[] { 2, 2 }));

        assertThrows(IllegalArgumentException.class, () -> {
            shape.isInside(new long[] { 1 });
        });
    }

    @Test
    public void getIterator_ReturnsImgLi2ShapeIterator() {
        WritableBox box = GeomMasks.closedBox(new double[] { 0, 0 }, new double[] { 1, 1 });

        ImgLib2Shape shape = new ImgLib2Shape(box, AxisOrder.XY, 2);

        assertTrue(shape.getIterator() instanceof ImgLib2ShapeIterator);
    }

}
