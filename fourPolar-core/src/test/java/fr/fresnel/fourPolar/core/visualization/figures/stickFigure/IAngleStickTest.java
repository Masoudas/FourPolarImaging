package fr.fresnel.fourPolar.core.visualization.figures.stickFigure;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.util.DPoint;

public class IAngleStickTest {
    @Test
    public void stickPixels_AngleStickImplementation_SinglePointStick_ReturnsPositionOfSticks() {
        boolean equal = true;
        for (int x = 1; x < 5; x++) {
            for (int y = 1; y < 5; y++) {
                for (float slopeAngle = 0; slopeAngle < Math.PI; slopeAngle += Math.PI / 180 * 5) {
                    DPoint pose = new DPoint(x, y);

                    IAngleStick angleStick = new AngleStick(pose, slopeAngle, 1, 1, null);
                    IAngleStickIterator iterator = angleStick.getIterator();

                    equal &= iterator.next().equals(pose);
                }
            }
        }

        assertTrue(equal);
    }

    @Test
    public void stickPixels_AngleStickImplementation_Slope0to180SamePoseLen5_StickPassesPose() {
        boolean equal = true;
        DPoint pose = new DPoint(50, 50);

        for (float slopeAngle = 0; slopeAngle < Math.PI; slopeAngle += Math.PI / 180) {
            IAngleStick angleStick = new AngleStick(pose, slopeAngle, 5, 1, null);
            IAngleStickIterator iterator = angleStick.getIterator();

            boolean stickEquals = false;
            while (iterator.hasNext()){
                if (iterator.next().equals(pose)){
                    stickEquals = true;
                    break;
                }
            }
            
            equal &= stickEquals;
        }

        assertTrue(equal);
    }

    @Test
    public void stickPixels_AngleStickImplementation_Slope0to180SamePoseSameLen_PixelDistanceToLineLessThan1Pixel() {
        boolean distanceAcceptable = true;
        DPoint pose = new DPoint(50, 50);

        for (float slopeAngle = 0; slopeAngle < Math.PI; slopeAngle += Math.PI / 180) {
            IAngleStick angleStick = new AngleStick(pose, slopeAngle, 30, 1, null);
            IAngleStickIterator iterator = angleStick.getIterator();

            while (iterator.hasNext()){       
                distanceAcceptable &= pixelDistance(slopeAngle, pose, iterator.next()) < Math.sqrt(2);        
            }
            
        }
        
        assertTrue(distanceAcceptable);
    }

    private double pixelDistance(float slopeAngle, DPoint position, DPoint point) {
        // Suppose line equation is given by -ax + ax_0 - y_0 + y = 0
        double a = Math.tan(slopeAngle);
        return Math.abs(-a * point.x + point.y + a*position.x - position.y) / Math.sqrt(a * a + 1);
        
    }

}