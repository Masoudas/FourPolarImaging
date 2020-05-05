package fr.fresnel.fourPolar.core.util.shape;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;

public class ScaledShapeTest {
    @Test
    public void oneDim_returnUpToDimension() throws IllegalAccessException {
        IShape shape = new ShapeFactory().closedBox(new long[] { 0, 0 }, new long[] { 1, 1 }, AxisOrder.XY);
        long[] max = { 1 };

        IShapeIterator iterator = new ScaledShape(shape, AxisOrder.XYZ, max).getIterator();

        int counter = 0;
        while (iterator.hasNext()) {
            long[] pos = iterator.next();
            counter++;
            System.out.println(pos[0] + " " + pos[1] + " " + pos[2]);

        }

        assertTrue(counter == 8);

    }


    @Test
    public void twoDim_returnUpToDimension() throws IllegalAccessException {
        IShape shape = new ShapeFactory().closedBox(new long[] { 0, 0 }, new long[] { 1, 1 }, AxisOrder.XY);
        long[] max = { 1, 1 };
        IShapeIterator iterator = new ScaledShape(shape, AxisOrder.XYZ, max).getIterator();

        int counter = 0;
        while (iterator.hasNext()) {
            long[] pos = iterator.next();
            counter++;
            System.out.println(pos[0] + " " + pos[1] + " " + pos[2] + " " + pos[3]);

        }

        assertTrue(counter == 16);

    }

    @Test
    public void threeDimWithSingleDimInMiddle_returnUpToDimension() throws IllegalAccessException {
        IShape shape = new ShapeFactory().closedBox(new long[] { 0, 0 }, new long[] { 1, 1 }, AxisOrder.XY);
        long[] max = { 3, 2, 1 };

        IShapeIterator iterator = new ScaledShape(shape, AxisOrder.XYZ, max).getIterator();

        int counter = 0;
        while (iterator.hasNext()) {
            long[] pos = iterator.next();
            counter++;
            System.out.println(pos[0] + " " + pos[1] + " " + pos[2] + " " + pos[3] + " " + pos[4]);

        }

        assertTrue(counter == 4 * 4 * 3 * 2);

    }

    @Test
    public void getIterator_ScaleDimensionZero_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            IShape shape = new ShapeFactory().closedBox(new long[] { 0, 0 }, new long[] { 1, 1 }, AxisOrder.XY);
            long[] max = { 0, 3, 2 };
            IShapeIterator iterator = new ScaledShape(shape, AxisOrder.XYZ, max).getIterator();
        });
    }

}