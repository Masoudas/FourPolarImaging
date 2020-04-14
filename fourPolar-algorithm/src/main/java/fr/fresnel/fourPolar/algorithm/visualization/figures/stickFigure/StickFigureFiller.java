package fr.fresnel.fourPolar.algorithm.visualization.figures.stickFigure;

import java.util.Objects;

import fr.fresnel.fourPolar.algorithm.exceptions.visualization.figures.stickFigure.AngleStickUndefined;
import fr.fresnel.fourPolar.algorithm.util.image.stats.ImageStatistics;
import fr.fresnel.fourPolar.algorithm.visualization.figures.stickFigure.stick.AngleStickGenerator;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.IPixelRandomAccess;
import fr.fresnel.fourPolar.core.image.generic.pixel.IPixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.image.orientation.IOrientationImage;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationVector;
import fr.fresnel.fourPolar.core.util.DPoint;
import fr.fresnel.fourPolar.core.util.colorMap.ColorMap;
import fr.fresnel.fourPolar.core.visualization.figures.stickFigure.IStickFigure;
import fr.fresnel.fourPolar.core.visualization.figures.stickFigure.stick.IAngleStick;
import fr.fresnel.fourPolar.core.visualization.figures.stickFigure.stick.IAngleStickIterator;

/**
 * Using this class, we can fill the stick figure with sticks.
 */
public class StickFigureFiller {
    /**
     * Using this method, we generate 2D rho sticks (@see AngleStickGenerator) for
     * all positions (all pixels) in the orientation image. All sticks will have the
     * same length and thickness with this method.
     * 
     * CAUTION: The chosen colormap must not have black or white colors, otherwise,
     * it will be misinterpreted as intensity (because the background is an SoI
     * image).
     * 
     * @param orientationImage is the desired orientation image.
     * @param stickFigure      is the stick figure that corresponds to this
     *                         orientation image (created using the
     *                         {@link ISoIImage} that corresponds to this
     *                         orientation image).
     * @param length           is the desired length of the stick in pixels, must be
     *                         greater than equal one.
     * @param colorMap         is the color map used for filling the sticks.
     * @param thickness        is the desired thickness of sticks, must be greater
     *                         than equal one.
     */

    public static void fillWith2DStick(final IOrientationImage orientationImage, final IStickFigure stickFigure,
            final int length, final int thickness, final ColorMap colorMap) {
        Objects.requireNonNull(orientationImage, "orientationImage cannot be null");
        Objects.requireNonNull(stickFigure, "stickFigure cannot be null");
        Objects.requireNonNull(colorMap, "colorMap cannot be null");

        switch (stickFigure.getType()) {
            case Rho2D:
                _loopOverTwoAngles(orientationImage, stickFigure, length, thickness, colorMap, OrientationAngle.rho,
                        OrientationAngle.rho, OrientationVector.MAX_Rho);

                break;

            case Delta2D:
                _loopOverTwoAngles(orientationImage, stickFigure, length, thickness, colorMap, OrientationAngle.rho,
                        OrientationAngle.delta, OrientationVector.MAX_Delta);

                break;

            case Eta2D:
                _loopOverTwoAngles(orientationImage, stickFigure, length, thickness, colorMap, OrientationAngle.rho,
                        OrientationAngle.eta, OrientationVector.MAX_Eta);

                break;

            default:
                break;
        }

    }

    /**
     * Using this method, we generate 2D rho sticks (@see AngleStickGenerator) for
     * all positions (all pixels) in the orientation image. All sticks will have the
     * same length and thickness with this method.
     * 
     * @param orientationImage is the desired orientation image.
     * @param stickFigure      is the stick figure that corresponds to this
     *                         orientation image (created using the
     *                         {@link ISoIImage} that corresponds to this
     *                         orientation image).
     * @param length           is the desired length of the stick in pixels, must be
     *                         greater than equal one.
     * @param colorMap         is the color map used for filling the sticks.
     * @param thickness        is the desired thickness of sticks, must be greater
     *                         than equal one.
     * @param slope            is the orientation angle to be used as slope.
     * @param color            is the orientation angle to be used as color.
     * @param maxColorAngle    is the maximum of color angle. 
     */
    private static void _loopOverTwoAngles(final IOrientationImage orientationImage, final IStickFigure stickFigure,
            final int length, final int thickness, final ColorMap colorMap, OrientationAngle slope,
            OrientationAngle color, final double maxColorAngle) {
        assert orientationImage != null : "orientation image cannot be null";
        assert stickFigure != null : "stick figure cannot be null";
        assert length > 0 : "length must be greater than zero.";
        assert thickness > 0 : "thickness must be greater than zero.";
        assert colorMap != null : "colormap cannot be null";

        final IPixelCursor<Float32> slopeAngleCursor = orientationImage.getAngleImage(slope).getImage().getCursor();
        final IPixelCursor<Float32> colorAngleCursor = orientationImage.getAngleImage(color).getImage().getCursor();
        final IPixelRandomAccess<RGB16> stickRA = stickFigure.getImage().getRandomAccess();
        final AngleStickGenerator stickGenerator = new AngleStickGenerator(colorMap);
        final DPoint planeDim = ImageStatistics.getPlaneDim(stickFigure.getImage());

        while (slopeAngleCursor.hasNext()) {
            final float slopeAngle = slopeAngleCursor.next().value().get();
            final float colorAngle = colorAngleCursor.next().value().get();

            final long[] position = slopeAngleCursor.localize();
            try {
                final IAngleStick angleStick = stickGenerator.generate2DStick(slopeAngle, colorAngle, maxColorAngle,
                        position, length, thickness, planeDim);

                final IAngleStickIterator stickIterator = angleStick.getIterator();
                final RGB16 stickColor = angleStick.getColor();

                while (stickIterator.hasNext()) {
                    final long[] stickPixelPosition = stickIterator.next();
                    boolean isPixelInRange = (stickPixelPosition[0] > 1 && stickPixelPosition[0] < planeDim.x)
                            && (stickPixelPosition[1] > 1 && stickPixelPosition[1] < planeDim.y);
                    if (isPixelInRange) {
                        stickRA.setPosition(stickPixelPosition);
                        final IPixel<RGB16> stickPixel = stickRA.getPixel();
                        stickPixel.value().set(stickColor.getR(), stickColor.getG(), stickColor.getB());
                        stickRA.setPixel(stickPixel);
                    }
                }

            } catch (final AngleStickUndefined e) {
                // Skip the postions where angle is NaN.
            }
        }

    }

}