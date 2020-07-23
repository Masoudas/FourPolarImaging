package fr.fresnel.fourPolar.core.util.shape;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import net.imglib2.roi.geom.GeomMasks;
import net.imglib2.roi.geom.real.PointMask;
import net.imglib2.roi.geom.real.WritableBox;

public class ImageJ1LineShapeIteratorTest {
    @Test
    public void iterate_2DLineFrom00Slope0Length2_ReturnsCorrectPoints() {
        double[] pose = { 0, 0 };
        double slope = 0;
        long length = 2;
        int thickness = 1;
        AxisOrder axisOrder = AxisOrder.NoOrder;

        ILineShape line = ImageJ1LineShape.create(pose, slope, length, thickness, axisOrder);
        WritableBox box1 = GeomMasks.closedBox(new double[] { -1, 0 }, new double[] { 1, 0 });

        assertTrue(_iteratorIsOnTheLine(line.getIterator(), box1));
    }

    @Test
    public void iterate_2DLineFrom000Slope0Length2_ReturnsCorrectPoints() {
        double[] pose = { 0, 0, 0 };
        double slope = 0;
        long length = 2;
        int thickness = 1;
        AxisOrder axisOrder = AxisOrder.NoOrder;

        ILineShape line = ImageJ1LineShape.create(pose, slope, length, thickness, axisOrder);
        WritableBox box1 = GeomMasks.closedBox(new double[] { -1, 0, 0 }, new double[] { 1, 0, 0 });

        assertTrue(_iteratorIsOnTheLine(line.getIterator(), box1));
    }

    private boolean _iteratorIsOnTheLine(IShapeIterator iterator, WritableBox box) {
        if (!iterator.hasNext()){
            return false;
        }

        boolean isOnTheLine = true;
        for (; iterator.hasNext() && isOnTheLine; ) {
            PointMask pointMask = GeomMasks.pointMask(Arrays.stream(iterator.next()).mapToDouble(t->t).toArray());
            isOnTheLine = box.test(pointMask); 
        }
        
        return isOnTheLine;
    }
}