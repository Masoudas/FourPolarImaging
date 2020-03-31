package fr.fresnel.fourPolar.algorithm.visualization.figures.stickFigure;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.visualization.figures.stickFigure.IAngleStickIterator;
import ij.gui.Line;

public class IAngleStickIteratorTest {
    @Test
    public void next_AngleStickIteratorImplementation_OneElementNegativeAndPostiveIterator_CounterWillBeTwo() {
        Line negativeLine = new Line(0, 0, 0, 0);
        Line positiveLine = new Line(0, 0, 0, 0);

        IAngleStickIterator iterator = new AngleStickIterator(negativeLine.iterator(), positiveLine.iterator());

        int counter = 0;

        while (iterator.hasNext()) {
            counter++;
            iterator.next();
        }

        assertTrue(counter == 2);
    }
}