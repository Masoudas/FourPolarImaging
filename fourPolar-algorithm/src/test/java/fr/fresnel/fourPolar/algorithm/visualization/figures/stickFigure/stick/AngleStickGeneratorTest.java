package fr.fresnel.fourPolar.algorithm.visualization.figures.stickFigure.stick;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.algorithm.exceptions.visualization.figures.stickFigure.AngleStickUndefined;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.util.colorMap.ColorMap;
import fr.fresnel.fourPolar.core.util.colorMap.ColorMapFactory;
import fr.fresnel.fourPolar.core.visualization.figures.stickFigure.stick.IAngleStickIterator;

public class AngleStickGeneratorTest {

    @Test
    public void generate2DStick_SinglePointStick_ReturnsPositionOfSticks() throws AngleStickUndefined {
        ColorMap cMap = ColorMapFactory.create(ColorMapFactory.IMAGEJ_RED);
        boolean equal = true;
        int len = 1;
        int thickness = 1;
        for (int x = 1; x < 5; x++) {
            for (int y = 1; y < 5; y++) {
                for (double angle = 0; angle < Math.PI; angle += Math.PI / 180 * 5) {
                    long[] pose = {x, y};

                    IAngleStickIterator iterator = new AngleStickGenerator(cMap).generate2DStick(
                        angle, angle, angle, pose, len, thickness).getIterator();

                    // Note that the position is returned twice, for negative and positive line.
                    while (iterator.hasNext()) {
                        equal &=  Arrays.equals(iterator.next(), pose);
                    }

                }
            }
        }

        assertTrue(equal);
    }


    @Test
    public void generate2DStick_Angle0to180VaryingPose_PixelDistanceToLineLessThan1Pixel() throws AngleStickUndefined {
        boolean distanceAcceptable = true;
        ColorMap cMap = ColorMapFactory.create(ColorMapFactory.IMAGEJ_RED);
        long[] pose = { 50, 50 };
        int len = 10;
        int thickness = 1;

        for (int poseOffset = 0; poseOffset < 50; poseOffset++) {
            pose[0] = pose[0] + poseOffset;
            pose[1] = pose[0] + poseOffset;
            
            for (double angle = 0; angle < Math.PI; angle += Math.PI / 180) {    
                IAngleStickIterator iterator = new AngleStickGenerator(cMap)
                        .generate2DStick(angle, 0, 0, pose, len, thickness).getIterator();

                while (iterator.hasNext()) {
                    distanceAcceptable &= pixelDistance(angle, pose, iterator.next()) < Math.sqrt(2);
                }
            }

        }

        assertTrue(distanceAcceptable);
    }

    @Test
    public void generate2DStick_RedColorMapAndColorAngleMinAndMax_ColorsAreWhiteAndRed() throws AngleStickUndefined {
        ColorMap cMap = ColorMapFactory.create(ColorMapFactory.IMAGEJ_RED);
        RGB16 black = new RGB16(0, 0, 0);
        RGB16 red = new RGB16(255, 0, 0);

        long[] pose = { 0, 0 };
        int len = 10;
        int thickness = 1;

        double minAngle = 0;
        double maxAngle = Math.PI;

        AngleStickGenerator generator = new AngleStickGenerator(cMap);
        RGB16 color0 = generator.generate2DStick(0, minAngle, maxAngle, pose, len, thickness).getColor();
        RGB16 color180 = generator.generate2DStick(0, maxAngle, maxAngle, pose, len, thickness).getColor();

        assertTrue(color0.getR() == black.getR() && color0.getG() == black.getG() && color0.getB() == black.getB()
                && color180.getR() == red.getR() && color180.getG() == red.getG() && color180.getB() == red.getB());

    }

    private double pixelDistance(double slopeAngle, long[] position, long[] point) {
        // Suppose line equation is given by -ax + ax_0 - y_0 + y = 0
        double a = Math.tan(slopeAngle);
        return Math.abs(-a * point[0] + point[1] + a * position[0] - position[1]) / Math.sqrt(a * a + 1);

    }

}