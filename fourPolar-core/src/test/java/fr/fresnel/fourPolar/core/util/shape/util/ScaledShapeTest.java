package fr.fresnel.fourPolar.core.util.shape.util;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.shape.IShape;
import fr.fresnel.fourPolar.core.shape.IShapeIterator;
import fr.fresnel.fourPolar.core.shape.ShapeFactory;
import fr.fresnel.fourPolar.core.util.shape.ScaledShape;

public class ScaledShapeTest {
    @Test
    public void oneDimOnePoint_returnUpToDimension() throws IllegalAccessException {
        IShape shape = ShapeFactory.closedBox(new long[] { 0, 0 }, new long[] { 1, 1 }, AxisOrder.XY);
        long[] dim = { 1 };

        IShapeIterator iterator = new ScaledShape(shape, AxisOrder.XYZ, dim).getIterator();

        int counter = 0;
        while (iterator.hasNext()) {
            long[] pos = iterator.next();
            counter++;
            System.out.println(pos[0] + " " + pos[1] + " " + pos[2]);

        }

        assertTrue(counter == 4);

    }

    @Test
    public void oneDimTwoPoints_returnUpToDimension() throws IllegalAccessException {
        IShape shape = ShapeFactory.closedBox(new long[] { 0, 0 }, new long[] { 1, 1 }, AxisOrder.XY);
        long[] dim = { 2 };

        IShapeIterator iterator = new ScaledShape(shape, AxisOrder.XYZ, dim).getIterator();

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
        IShape shape = ShapeFactory.closedBox(new long[] { 0, 0 }, new long[] { 1, 1 }, AxisOrder.XY);
        long[] dim = { 2, 2 };
        IShapeIterator iterator = new ScaledShape(shape, AxisOrder.XYZ, dim).getIterator();

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
        IShape shape = ShapeFactory.closedBox(new long[] { 0, 0 }, new long[] { 1, 1 }, AxisOrder.XY);
        long[] dim = { 4, 3, 2 };

        IShapeIterator iterator = new ScaledShape(shape, AxisOrder.XYZ, dim).getIterator();

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
            IShape shape = ShapeFactory.closedBox(new long[] { 0, 0 }, new long[] { 1, 1 }, AxisOrder.XY);
            long[] dim = { 0, 4, 3 };
            new ScaledShape(shape, AxisOrder.XYZ, dim).getIterator();
        });
    }

}