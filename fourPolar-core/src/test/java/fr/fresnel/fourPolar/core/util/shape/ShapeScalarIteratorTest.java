package fr.fresnel.fourPolar.core.util.shape;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ShapeScalarIteratorTest {
    @Test
    public void oneDim_returnUpToDimension() throws IllegalAccessException {
        IShape shape = new ShapeFactory().closedBox(new long[] { 0, 0 }, new long[] { 1, 1 });
        long[] scaleDimension = { 3, 3, 2 };
        IShapeIterator iterator = ShapeScalarIterator.getIterator(shape, scaleDimension);

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
        IShape shape = new ShapeFactory().closedBox(new long[] { 0, 0 }, new long[] { 1, 1 });
        long[] scaleDimension = { 3, 3, 2, 2 };
        IShapeIterator iterator = ShapeScalarIterator.getIterator(shape, scaleDimension);

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
        IShape shape = new ShapeFactory().closedBox(new long[] { 0, 0 }, new long[] { 1, 1 });
        long[] scaleDimension = { 3, 3, 1, 3, 2 };
        IShapeIterator iterator = ShapeScalarIterator.getIterator(shape, scaleDimension);

        int counter = 0;
        while (iterator.hasNext()) {
            long[] pos = iterator.next();
            counter++;
            System.out.println(pos[0] + " " + pos[1] + " " + pos[2] + " " + pos[3] + " " + pos[4]);
            
        }

        assertTrue(counter == 4 * 3 * 2);

    }

    @Test
    public void getIterator_ScaleDimensionZero_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            IShape shape = new ShapeFactory().closedBox(new long[] { 0, 0 }, new long[] { 1, 1 });
            long[] scaleDimension = { 3, 3, 0, 3, 2 };
            IShapeIterator iterator = ShapeScalarIterator.getIterator(shape, scaleDimension);
        });
    }

}