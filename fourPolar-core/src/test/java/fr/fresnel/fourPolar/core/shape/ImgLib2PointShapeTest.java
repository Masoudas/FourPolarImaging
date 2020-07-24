package fr.fresnel.fourPolar.core.shape;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;

public class ImgLib2PointShapeTest {
    @Test
    public void init_PointDimDifferesFromAxisOrder_ThrowsIllegalArgumentExceptions() {
        assertThrows(IllegalArgumentException.class, () -> {
            ImgLib2PointShape.create(new long[] { 1 }, AxisOrder.XY);
        });

    }

    @Test
    public void point_PointsOfDifferentDim_ShapeReturnsCorrectPointAndDimension() {
        long[] point1 = { 1 };
        IPointShape pointShape1 = ImgLib2PointShape.create(point1, AxisOrder.NoOrder);

        long[] point2 = { 1, 2, 3 };
        IPointShape pointShape2 = ImgLib2PointShape.create(point2, AxisOrder.XYC);

        assertArrayEquals(pointShape1.point(), point1);
        assertTrue(pointShape1.shapeDim() == 1 && pointShape1.axisOrder() == AxisOrder.NoOrder);

        assertArrayEquals(pointShape2.point(), point2);
        assertTrue(pointShape2.shapeDim() == 3 && pointShape2.axisOrder() == AxisOrder.XYC);

    }

}