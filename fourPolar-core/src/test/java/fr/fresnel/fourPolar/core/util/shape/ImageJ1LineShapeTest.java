package fr.fresnel.fourPolar.core.util.shape;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;

public class ImageJ1LineShapeTest {
    @Test
    public void create_2DLineFrom00Slope0Length2_ReturnsCorrectStartAndEndPoint() {
        double[] pose = { 0, 0 };
        double slope = 0;
        long length = 2;
        int thickness = 1;
        AxisOrder axisOrder = AxisOrder.NoOrder;

        ILineShape line = ImageJ1LineShape.create(pose, slope, length, thickness, axisOrder);

        long[] expectedLineStart = { -1, 0 };
        long[] expectedLineEnd = { 1, 0 };
        assertArrayEquals(expectedLineStart, line.lineStart());
        assertArrayEquals(expectedLineEnd, line.lineEnd());
    }

    @Test
    public void create_2DLineFrom00Slope90Length2_ReturnsCorrectStartAndEndPoint() {
        double[] pose = { 0, 0 };
        double slope = Math.PI / 2;
        long length = 2;
        int thickness = 1;
        AxisOrder axisOrder = AxisOrder.NoOrder;

        ILineShape line = ImageJ1LineShape.create(pose, slope, length, thickness, axisOrder);

        long[] expectedLineStart = { 0, -1 };
        long[] expectedLineEnd = { 0, 1 };
        assertArrayEquals(expectedLineStart, line.lineStart());
        assertArrayEquals(expectedLineEnd, line.lineEnd());
    }

    @Test
    public void create_2DLineFrom00Slope180Length2_ReturnsCorrectStartAndEndPoint() {
        double[] pose = { 0, 0 };
        double slope = Math.PI;
        long length = 2;
        int thickness = 1;
        AxisOrder axisOrder = AxisOrder.NoOrder;

        ILineShape line = ImageJ1LineShape.create(pose, slope, length, thickness, axisOrder);

        long[] expectedLineStart = { 1, 0 };
        long[] expectedLineEnd = { -1, 0 };
        assertArrayEquals(expectedLineStart, line.lineStart());
        assertArrayEquals(expectedLineEnd, line.lineEnd());
    }

    @Test
    public void create_2DLineFrom00Slope45Length3_ReturnsCorrectStartAndEndPoint() {
        double[] pose = { 0, 0 };
        double slope = Math.PI / 4;
        long length = 3;
        int thickness = 1;
        AxisOrder axisOrder = AxisOrder.NoOrder;

        ILineShape line = ImageJ1LineShape.create(pose, slope, length, thickness, axisOrder);

        long[] expectedLineStart = { -1, -1 };
        long[] expectedLineEnd = { 1, 1 };
        assertArrayEquals(expectedLineStart, line.lineStart());
        assertArrayEquals(expectedLineEnd, line.lineEnd());
    }

    @Test
    public void create_2DLineFrom0000Slope0Length2_ReturnsCorrectStartAndEndPoint() {
        double[] pose = { 0, 0, 0 };
        double slope = 0;
        long length = 2;
        int thickness = 1;
        AxisOrder axisOrder = AxisOrder.NoOrder;

        ILineShape line = ImageJ1LineShape.create(pose, slope, length, thickness, axisOrder);

        long[] expectedLineStart = { -1, 0, 0 };
        long[] expectedLineEnd = { 1, 0, 0 };
        assertArrayEquals(expectedLineStart, line.lineStart());
        assertArrayEquals(expectedLineEnd, line.lineEnd());
    }

    @Test
    public void rotate_2DLineFrom00Slope0Length2By90_ReturnsCorrectStartAndEndPoint() {
        double[] pose = { 0, 0 };
        double slope = 0;
        long length = 2;
        int thickness = 1;
        AxisOrder axisOrder = AxisOrder.NoOrder;
        ILineShape line = ImageJ1LineShape.create(pose, slope, length, thickness, axisOrder);

        double rotation_angle = Math.PI / 2;
        ILineShape rotated_line = (ILineShape) line.rotate2D(rotation_angle);

        long[] expectedLineStart = { 0, -1 };
        long[] expectedLineEnd = { 0, 1 };
        assertArrayEquals(expectedLineStart, rotated_line.lineStart());
        assertArrayEquals(expectedLineEnd, rotated_line.lineEnd());
    }

    @Test
    public void rotate_2DLineFrom000Slope0Length2By90_ReturnsCorrectStartAndEndPoint() {
        double[] pose = { 0, 0, 0 };
        double slope = 0;
        long length = 2;
        int thickness = 1;
        AxisOrder axisOrder = AxisOrder.NoOrder;
        ILineShape line = ImageJ1LineShape.create(pose, slope, length, thickness, axisOrder);

        double rotation_angle = Math.PI / 2;
        ILineShape rotated_line = (ILineShape) line.rotate2D(rotation_angle);

        long[] expectedLineStart = { 0, -1, 0 };
        long[] expectedLineEnd = { 0, 1, 0 };
        assertArrayEquals(expectedLineStart, rotated_line.lineStart());
        assertArrayEquals(expectedLineEnd, rotated_line.lineEnd());
    }

    @Test
    public void translate_2DLineFrom00Slope0Length2by11_ReturnsCorrectStartAndEndPoint() {
        double[] pose = { 0, 0 };
        double slope = 0;
        long length = 2;
        int thickness = 1;
        AxisOrder axisOrder = AxisOrder.NoOrder;
        ILineShape line = ImageJ1LineShape.create(pose, slope, length, thickness, axisOrder);

        long[] translation = { 1, 1 };
        ILineShape rotated_line = (ILineShape) line.translate(translation);

        long[] expectedLineStart = { 0, 1 };
        long[] expectedLineEnd = { 2, 1 };
        assertArrayEquals(expectedLineStart, rotated_line.lineStart());
        assertArrayEquals(expectedLineEnd, rotated_line.lineEnd());
    }

    @Test
    public void translate_2DLineFrom000Slope0Length2by111_ReturnsCorrectStartAndEndPoint() {
        double[] pose = { 0, 0, 0 };
        double slope = 0;
        long length = 2;
        int thickness = 1;
        AxisOrder axisOrder = AxisOrder.NoOrder;
        ILineShape line = ImageJ1LineShape.create(pose, slope, length, thickness, axisOrder);

        long[] translation = { 1, 1, 1 };
        ILineShape rotated_line = (ILineShape) line.translate(translation);

        long[] expectedLineStart = { 0, 1, 1 };
        long[] expectedLineEnd = { 2, 1, 1 };
        assertArrayEquals(expectedLineStart, rotated_line.lineStart());
        assertArrayEquals(expectedLineEnd, rotated_line.lineEnd());
    }

}