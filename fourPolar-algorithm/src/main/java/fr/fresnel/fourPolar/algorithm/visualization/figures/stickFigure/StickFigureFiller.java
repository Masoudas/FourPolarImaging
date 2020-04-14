package fr.fresnel.fourPolar.algorithm.visualization.figures.stickFigure;

import java.util.Objects;

import fr.fresnel.fourPolar.algorithm.exceptions.visualization.figures.stickFigure.AngleStickUndefined;
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
import fr.fresnel.fourPolar.core.visualization.figures.stickFigure.stick.StickType;

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

        if (stickFigure.getType() == StickType.Rho2D) {
            OrientationAngle slopeAngle = OrientationAngle.rho;
            OrientationAngle colorAngle = OrientationAngle.rho;

            _fillWithRho2DStick(orientationImage, stickFigure, length, thickness, colorMap);
        } else if (stickFigure.getType() == StickType.Delta2D) {
            OrientationAngle slopeAngle = OrientationAngle.rho;
            OrientationAngle colorAngle = OrientationAngle.delta;

            _fillWithDelta2DStick(orientationImage, stickFigure, length, thickness, colorMap);
        } else {
            OrientationAngle slopeAngle = OrientationAngle.rho;
            OrientationAngle colorAngle = OrientationAngle.eta;

            _fillWithEta2DStick(orientationImage, stickFigure, length, thickness, colorMap);
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
     */
    private static void _fillWithRho2DStick(final IOrientationImage orientationImage, final IStickFigure stickFigure,
            final int length, final int thickness, final ColorMap colorMap) {
        final IPixelCursor<Float32> rhoCursor = orientationImage.getAngleImage(OrientationAngle.rho).getImage()
                .getCursor();
        final IPixelRandomAccess<RGB16> stickRA = stickFigure.getImage().getRandomAccess();
        final AngleStickGenerator stickGenerator = new AngleStickGenerator(colorMap);
        final DPoint planeDim = _getXYPlaneDimension(stickFigure);

        _loopOverOneAngle(length, thickness, rhoCursor, OrientationVector.MAX_Rho, stickRA, stickGenerator, planeDim);


    }

    /**
     * Using this method, we generate delta 2D sticks (@see AngleStickGenerator) for
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
     */
    private static void _fillWithDelta2DStick(final IOrientationImage orientationImage, final IStickFigure stickFigure,
            final int length, final int thickness, final ColorMap colorMap) {
        final IPixelCursor<Float32> rhoCursor = orientationImage.getAngleImage(OrientationAngle.rho).getImage()
                .getCursor();
        final IPixelCursor<Float32> deltaCursor = orientationImage.getAngleImage(OrientationAngle.delta).getImage()
                .getCursor();
        final IPixelRandomAccess<RGB16> stickRA = stickFigure.getImage().getRandomAccess();
        final AngleStickGenerator stickGenerator = new AngleStickGenerator(colorMap);
        final DPoint planeDim = _getXYPlaneDimension(stickFigure);

        _loopOverTwoAngles(length, thickness, rhoCursor, deltaCursor, OrientationVector.MAX_Delta, stickRA,
                stickGenerator, planeDim);
    }

    /**
     * Using this method, we generate eta 2D sticks (@see AngleStickGenerator) for
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
     */
    private static void _fillWithEta2DStick(final IOrientationImage orientationImage, final IStickFigure stickFigure,
            final int length, final int thickness, final ColorMap colorMap) {
        final IPixelCursor<Float32> rhoCursor = orientationImage.getAngleImage(OrientationAngle.rho).getImage()
                .getCursor();
        final IPixelCursor<Float32> etaCursor = orientationImage.getAngleImage(OrientationAngle.eta).getImage()
                .getCursor();
        final IPixelRandomAccess<RGB16> stickRA = stickFigure.getImage().getRandomAccess();
        final AngleStickGenerator stickGenerator = new AngleStickGenerator(colorMap);

        final DPoint planeDim = _getXYPlaneDimension(stickFigure);

        _loopOverTwoAngles(length, thickness, rhoCursor, etaCursor, OrientationVector.MAX_Eta, stickRA, stickGenerator,
                planeDim);
    }

    /**
     * Loops over a single angle to generate the sticks. The same angle is used for
     * both color and slope.
     */
    private static void _loopOverOneAngle(final int length, final int thickness,
            final IPixelCursor<Float32> angleCursor, double maxAngle, final IPixelRandomAccess<RGB16> stickRA,
            final AngleStickGenerator stickGenerator, DPoint planeDim) {
        while (angleCursor.hasNext()) {
            final float angle = angleCursor.next().value().get();
            final long[] position = angleCursor.localize();
            try {
                final IAngleStick angleStick = stickGenerator.generate2DStick(angle, angle, maxAngle, position, length,
                        thickness, planeDim);

                final IAngleStickIterator stickIterator = angleStick.getIterator();
                final RGB16 color = angleStick.getColor();

                while (stickIterator.hasNext()) {
                    final long[] stickPixelPosition = stickIterator.next();
                    boolean isPixelInRange = (stickPixelPosition[0] > 1 && stickPixelPosition[0] < planeDim.x)
                            && (stickPixelPosition[1] > 1 && stickPixelPosition[1] < planeDim.y);
                    if (isPixelInRange) {
                        stickRA.setPosition(stickPixelPosition);
                        final IPixel<RGB16> stickPixel = stickRA.getPixel();
                        stickPixel.value().set(color.getR(), color.getG(), color.getB());
                        stickRA.setPixel(stickPixel);
                    }
                }

            } catch (final AngleStickUndefined e) {
                // Skip the postions where angle is NaN.
            }
        }
    }

    /**
     * Loops over two orinetation angles to generate the sticks. angle1 is used for
     * slope, while angle2 is used for color.
     */
    private static void _loopOverTwoAngles(final int length, final int thickness,
            final IPixelCursor<Float32> angle1Cursor, final IPixelCursor<Float32> angle2Cursor, double maxAngle2,
            final IPixelRandomAccess<RGB16> stickRA, final AngleStickGenerator stickGenerator, DPoint planeDim) {
        while (angle1Cursor.hasNext()) {
            final float angle1 = angle1Cursor.next().value().get();
            final float angle2 = angle2Cursor.next().value().get();

            final long[] position = angle1Cursor.localize();
            try {
                final IAngleStick angleStick = stickGenerator.generate2DStick(angle1, angle2, maxAngle2, position,
                        length, thickness, planeDim);

                final IAngleStickIterator stickIterator = angleStick.getIterator();
                final RGB16 color = angleStick.getColor();

                while (stickIterator.hasNext()) {
                    final long[] stickPixelPosition = stickIterator.next();
                    boolean isPixelInRange = (stickPixelPosition[0] > 1 && stickPixelPosition[0] < planeDim.x)
                            && (stickPixelPosition[1] > 1 && stickPixelPosition[1] < planeDim.y);
                    if (isPixelInRange) {
                        stickRA.setPosition(stickPixelPosition);
                        final IPixel<RGB16> stickPixel = stickRA.getPixel();
                        stickPixel.value().set(color.getR(), color.getG(), color.getB());
                        stickRA.setPixel(stickPixel);
                    }
                }

            } catch (final AngleStickUndefined e) {
                // Skip the postions where angle is NaN.
            }
        }
    }

    private static DPoint _getXYPlaneDimension(final IStickFigure stickFigure) {
        long[] dim = stickFigure.getImage().getDimensions();
        DPoint planeDim = new DPoint((int) dim[0], (int) dim[1]);
        return planeDim;
    }

}