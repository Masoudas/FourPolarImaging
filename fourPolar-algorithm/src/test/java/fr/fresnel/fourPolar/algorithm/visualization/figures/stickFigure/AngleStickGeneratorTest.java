package fr.fresnel.fourPolar.algorithm.visualization.figures.stickFigure;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.algorithm.exceptions.visualization.figures.stickFigure.AngleStickUndefined;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationVector;
import fr.fresnel.fourPolar.core.util.DPoint;
import fr.fresnel.fourPolar.core.util.colorMap.ColorMap;
import fr.fresnel.fourPolar.core.util.colorMap.ColorMapFactory;
import fr.fresnel.fourPolar.core.visualization.figures.stickFigure.IAngleStickIterator;

public class AngleStickGeneratorTest {
    ColorMap cMap = ColorMapFactory.create(ColorMapFactory.IMAGEJ_RED);

    RGB16 black = new RGB16(0, 0, 0);
    RGB16 red = new RGB16(255, 0, 0);

    @Test
    public void createRhoStick_SinglePointStick_ReturnsPositionOfSticks() throws AngleStickUndefined {
        boolean equal = true;
        int len = 1;
        for (int x = 1; x < 5; x++) {
            for (int y = 1; y < 5; y++) {
                for (float rho = 0; rho < Math.PI; rho += Math.PI / 180 * 5) {
                    DPoint pose = new DPoint(x, y);
                    OrientationVector vec = new OrientationVector(rho, 0, 0);

                    IAngleStickIterator iterator = new AngleStickGenerator(cMap).getRhoStick(vec, pose, len, 1)
                            .getIterator();

                    // Note that the position is returned twice, for negative and positive line.
                    while (iterator.hasNext()) {
                        equal &= iterator.next().equals(pose);
                    }

                }
            }
        }

        assertTrue(equal);
    }

    @Test
    public void createDeltaStick_SinglePointStick_ReturnsPositionOfSticks() throws AngleStickUndefined {
        boolean equal = true;
        int len = 1;
        for (int x = 1; x < 5; x++) {
            for (int y = 1; y < 5; y++) {
                for (float delta = 0; delta < Math.PI; delta += Math.PI / 180 * 5) {
                    DPoint pose = new DPoint(x, y);
                    OrientationVector vec = new OrientationVector(0, delta, 0);

                    IAngleStickIterator iterator = new AngleStickGenerator(cMap).getDeltaStick(vec, pose, len, 1)
                            .getIterator();

                    // Note that the position is returned twice, for negative and positive line.
                    while (iterator.hasNext()) {
                        equal &= iterator.next().equals(pose);
                    }

                }
            }
        }

        assertTrue(equal);
    }

    @Test
    public void createEtaStick_SinglePointStick_ReturnsPositionOfSticks() throws AngleStickUndefined {
        boolean equal = true;
        int len = 1;
        for (int x = 1; x < 5; x++) {
            for (int y = 1; y < 5; y++) {
                for (float eta = 0; eta < Math.PI; eta += Math.PI / 180 * 5) {
                    DPoint pose = new DPoint(x, y);
                    // Note that actually this stick is independent of eta angle.
                    OrientationVector vec = new OrientationVector(0, 0, eta);

                    IAngleStickIterator iterator = new AngleStickGenerator(cMap).getEtaStick(vec, pose, len, 1)
                            .getIterator();

                    // Note that the position is returned twice, for negative and positive line.
                    while (iterator.hasNext()) {
                        equal &= iterator.next().equals(pose);
                    }

                }
            }
        }

        assertTrue(equal);
    }

    @Test
    public void createRhoStick_0to180SamePoseSameLen_PixelDistanceToLineLessThan1Pixel() throws AngleStickUndefined {
        boolean distanceAcceptable = true;
        DPoint pose = new DPoint(50, 50);
        int len = 10;
        int thickness = 1;

        for (float rho = 0; rho < Math.PI; rho += Math.PI / 180) {
            OrientationVector vec = new OrientationVector(rho, 0, 0);

            IAngleStickIterator iterator = new AngleStickGenerator(cMap).getRhoStick(vec, pose, len, thickness)
                    .getIterator();

            while (iterator.hasNext()) {
                distanceAcceptable &= pixelDistance(rho, pose, iterator.next()) < Math.sqrt(2);
            }
        }

        assertTrue(distanceAcceptable);
    }

    @Test
    public void createDeltaStick_0to180SamePoseSameLen_PixelDistanceToLineLessThan1Pixel() throws AngleStickUndefined {
        boolean distanceAcceptable = true;
        DPoint pose = new DPoint(50, 50);
        int len = 10;
        int thickness = 1;

        for (float delta = 0; delta < Math.PI; delta += Math.PI / 180) {
            OrientationVector vec = new OrientationVector(0, delta, 0);

            IAngleStickIterator iterator = new AngleStickGenerator(cMap).getDeltaStick(vec, pose, len, thickness)
                    .getIterator();

            while (iterator.hasNext()) {
                distanceAcceptable &= pixelDistance(delta, pose, iterator.next()) < Math.sqrt(2);
            }
        }

        assertTrue(distanceAcceptable);
    }

    @Test
    public void createEtaStick_Rho0to180SamePoseSameLen_PixelDistanceToLineLessThan1Pixel() throws AngleStickUndefined {
        boolean distanceAcceptable = true;
        DPoint pose = new DPoint(50, 50);
        int len = 10;
        int thickness = 1;

        for (float rho = 0; rho < Math.PI; rho += Math.PI / 180) {
            OrientationVector vec = new OrientationVector(rho, 0, 0);

            IAngleStickIterator iterator = new AngleStickGenerator(cMap).getDeltaStick(vec, pose, len, thickness)
                    .getIterator();

            while (iterator.hasNext()) {
                distanceAcceptable &= pixelDistance(rho, pose, iterator.next()) < Math.sqrt(2);
            }
        }

        assertTrue(distanceAcceptable);
    }

    @Test
    public void createRhoStick_Rho0And180_ColorsAreWhiteAndRed() throws AngleStickUndefined {
        DPoint pose = new DPoint(50, 50);
        int len = 10;
        int thickness = 1;

        OrientationVector vec0 = new OrientationVector(0, 0, 0);
        OrientationVector vec180 = new OrientationVector(OrientationVector.MAX_Rho, 0, 0);

        RGB16 color0 = new AngleStickGenerator(cMap).getRhoStick(vec0, pose, len, thickness).getColor();
        RGB16 color180 = new AngleStickGenerator(cMap).getRhoStick(vec180, pose, len, thickness).getColor();

        assertTrue(color0.getR() == black.getR() && color0.getG() == black.getG() && color0.getB() == black.getB()
                && color180.getR() == red.getR() && color180.getG() == red.getG()
                && color180.getB() == red.getB());

    }

    @Test
    public void createDeltaStick_Delta0And180_ColorsAreWhiteAndRed() throws AngleStickUndefined {
        DPoint pose = new DPoint(50, 50);
        int len = 10;
        int thickness = 1;

        OrientationVector vec0 = new OrientationVector(0, 0, 0);
        OrientationVector vec180 = new OrientationVector(0, OrientationVector.MAX_Delta, 0);

        RGB16 color0 = new AngleStickGenerator(cMap).getDeltaStick(vec0, pose, len, thickness).getColor();
        RGB16 color180 = new AngleStickGenerator(cMap).getDeltaStick(vec180, pose, len, thickness).getColor();

        assertTrue(color0.getR() == black.getR() && color0.getG() == black.getG() && color0.getB() == black.getB()
                && color180.getR() == black.getR() && color180.getG() == black.getG()
                && color180.getB() == black.getB());

    }

    @Test
    public void createStick_NullAngle_RaisesAngleStickUndefined() {
        OrientationVector vec = new OrientationVector(Float.NaN, 0, 0);
        DPoint pose = new DPoint(50, 50);
        int len = 10;
        int thickness = 1;

        AngleStickGenerator generator = new AngleStickGenerator(cMap);
        assertThrows(AngleStickUndefined.class, () -> {generator.getRhoStick(vec, pose, len, thickness);});
        assertThrows(AngleStickUndefined.class, () -> {generator.getDeltaStick(vec, pose, len, thickness);});
        assertThrows(AngleStickUndefined.class, () -> {generator.getEtaStick(vec, pose, len, thickness);});
        
    }

    private double pixelDistance(float slopeAngle, DPoint position, DPoint point) {
        // Suppose line equation is given by -ax + ax_0 - y_0 + y = 0
        double a = Math.tan(slopeAngle);
        return Math.abs(-a * point.x + point.y + a * position.x - position.y) / Math.sqrt(a * a + 1);

    }

}