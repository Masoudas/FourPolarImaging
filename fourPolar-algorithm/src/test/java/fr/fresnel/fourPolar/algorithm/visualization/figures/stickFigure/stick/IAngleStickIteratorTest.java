package fr.fresnel.fourPolar.algorithm.visualization.figures.stickFigure.stick;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.visualization.figures.stickFigure.stick.IAngleStickIterator;
import ij.gui.Line;

public class IAngleStickIteratorTest {
    @Test
    public void next_AngleStickIteratorImplementation_OneElementNegativeAndPostiveIterator_CounterWillBeTwo() {
        long[] position = {0, 0};
        Line negativeLine = new Line(0, 0, position[0], position[1]);
        Line positiveLine = new Line(position[0], position[1], 0, 0);

        IAngleStickIterator iterator = new Angle2DStickIterator(
            negativeLine.iterator(), positiveLine.iterator(), position);

        int counter = 0;
        while (iterator.hasNext()) {
            counter++;
            iterator.next();
        }

        assertTrue(counter == 2);
    }
}