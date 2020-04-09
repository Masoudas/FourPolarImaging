package fr.fresnel.fourPolar.algorithm.visualization.figures.stickFigure.stick;

import java.util.Objects;

import fr.fresnel.fourPolar.algorithm.exceptions.visualization.figures.stickFigure.AngleStickUndefined;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.physics.dipole.IOrientationVector;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationVector;
import fr.fresnel.fourPolar.core.util.colorMap.ColorMap;
import fr.fresnel.fourPolar.core.visualization.figures.stickFigure.stick.IAngleStick;
import fr.fresnel.fourPolar.core.visualization.figures.stickFigure.stick.IAngleStickIterator;
import fr.fresnel.fourPolar.core.visualization.figures.stickFigure.stick.StickType;
import ij.gui.Line;

/**
 * A concrete implementation of {@link IAngleStick}. Note that the sticks can
 * have negative coordinates in this implementation.
 */
public class AngleStickGenerator {
    private final ColorMap _colorMap;

    /**
     * Define a colormap, to choose the stick colors from.
     */
    public AngleStickGenerator(ColorMap colorMap) {
        this._colorMap = colorMap;
    }

    /**
     * Generates an {@link IAngleStick}, where the slope is the rho angle, and color
     * is again the rho angle. If rho is NaN, and exception is thrown.
     * 
     * @param rhoAngle       is the rho orinetation angle.
     * @param dipolePosition is the corresponding dipole discrete position, as [x, y, z, ...].
     * @param length         is the desired length of the dipole, must be greater
     *                       than equal 1.
     * @param thickness      is the desired thickness of the dipole, must be greater
     *                       than equal 1.
     * @return the generated angle stick.
     *
     * @throws AngleStickUndefined if rho angle is NaN.
     */
    public IAngleStick generate2DStick(StickType stickType, IOrientationVector orientationVector, long[] dipolePosition, int length, int thickness)
            throws AngleStickUndefined {
        Objects.requireNonNull(dipolePosition, "dipolePosition cannot be null.");
        Objects.requireNonNull(orientationVector, "orientationVector cannot be zero.");

        double rho = orientationVector.getAngle(OrientationAngle.rho);
        double delta = orientationVector.getAngle(OrientationAngle.delta);
        double eta = orientationVector.getAngle(OrientationAngle.eta);

        if (Double.isNaN(rho) || Double.isNaN(delta) || Double.isNaN(rho)){
            throw new AngleStickUndefined("Can't define stick for undefined ");
        }

        if (length <= 0) {
            throw new AngleStickUndefined("Stick length must be positive.");
        }

        if (thickness <= 0) {
            throw new AngleStickUndefined("Stick thickness must be positive.");
        }

        RGB16 color = _colorMap.getColor(0, OrientationVector.MAX_Rho, rhoAngle);
        IAngleStickIterator iterator = _formIterator(rhoAngle, dipolePosition, length, thickness);

        return new AngleStick(dipolePosition, rhoAngle, length, thickness, color, iterator);
    }

    /**
     * Generates an {@link IAngleStick}, where the slope is the rho angle, and color
     * the delta angle. If rho or delta is NaN, and exception is thrown.
     * 
     * @param rhoAngle       is the rho angle.
     * @param deltaAngle     is the delta angle.
     * @param dipolePosition is the corresponding dipole discrete position, as [x, y, z, ...].
     * @param length         is the desired length of the dipole, must be greater
     *                       than equal 1.
     * @param thickness      is the desired thickness of the dipole, must be greater
     *                       than equal 1.
     * @return the generated angle stick.
     * 
     * @throws AngleStickUndefined if rho or delta angle is NaN.
     */
    
    public IAngleStick getDelta2DStick(double rhoAngle, double deltaAngle, long[] dipolePosition, int length,
            int thickness) throws AngleStickUndefined {
        Objects.requireNonNull(dipolePosition, "dipolePosition cannot be null");

        if (Double.isNaN(rhoAngle) || Double.isNaN(deltaAngle)) {
            throw new AngleStickUndefined("Cannot define stick for nan angles.");
        }

        if (length <= 0) {
            throw new AngleStickUndefined("Stick length must be positive.");
        }

        if (thickness <= 0) {
            throw new AngleStickUndefined("Stick thickness must be positive.");
        }

        RGB16 color = _colorMap.getColor(0, OrientationVector.MAX_Delta, deltaAngle);
        IAngleStickIterator iterator = _formIterator(rhoAngle, dipolePosition, length, thickness);

        return new AngleStick(dipolePosition, rhoAngle, length, thickness, color, iterator);
    }

    /**
     * Generates an {@link IAngleStick}, where the slope is the rho angle, and color
     * the delta angle. If rho or delta is NaN, and exception is thrown.
     * 
     * @param rhoAngle       is the rho orientation angle.
     * @param deltaAngle     is the delta angle.
     * @param dipolePosition is the corresponding dipole discrete position, as [x, y, z, ...].
     * @param length         is the desired length of the dipole, must be greater
     *                       than equal 1.
     * @param thickness      is the desired thickness of the dipole, must be greater
     *                       than equal 1.
     * @return the generated angle stick.
     * 
     * @throws AngleStickUndefined if rho or eta angle is NaN.
     */
    public IAngleStick getEta2DStick(double rhoAngle, double etaAngle, long[] dipolePosition, int length, int thickness)
            throws AngleStickUndefined {
        Objects.requireNonNull(dipolePosition, "dipolePosition cannot be null");

        if (Double.isNaN(rhoAngle) || Double.isNaN(etaAngle)) {
            throw new AngleStickUndefined("Cannot define stick for nan angles.");
        }

        if (length <= 0) {
            throw new AngleStickUndefined("Stick length must be positive.");
        }

        if (thickness <= 0) {
            throw new AngleStickUndefined("Stick thickness must be positive.");
        }

        RGB16 color = _colorMap.getColor(0, OrientationVector.MAX_Eta, etaAngle);
        IAngleStickIterator iterator = _formIterator(rhoAngle, dipolePosition, length, thickness);

        return new AngleStick(dipolePosition, rhoAngle, length, thickness, color, iterator);
    }

    /**
     * To form an iterator that iterates over the pixels corresponding to the stick,
     * define a negative line, one that starts from the end point to the position,
     * and one that starts from position to the other line. This would ensure that
     * the stick always passes through the dipole!
     * 
     * @param slopeAngle     is the slope of the dipole in particular direction.
     * @param dipolePosition is the pixel position of the dipole, as [x, y, z, ...]
     * @param length         is the desired length of the stick.
     * @param thickness      is the desired thickness of the stick.
     * @return iterator that iterates over the region corresponding to this stick,
     *         in pixel coordinates.
     */
    private IAngleStickIterator _formIterator(double slopeAngle, long[] dipolePosition, int length, int thickness) {
        double xStart = dipolePosition[0] - Math.cos(slopeAngle) * ((length - 1) / 2);
        double yStart = dipolePosition[1] - Math.sin(slopeAngle) * ((length - 1) / 2);

        double xEnd = dipolePosition[0] + Math.cos(slopeAngle) * (length / 2);
        double yEnd = dipolePosition[1] + Math.sin(slopeAngle) * (length / 2);

        Line negativeLine = new Line(xStart, yStart, dipolePosition[0], dipolePosition[1]);
        negativeLine.setStrokeWidth(thickness);

        Line positiveLine = new Line(dipolePosition[0], dipolePosition[1], xEnd, yEnd);
        positiveLine.setStrokeWidth(thickness);

        return new Angle2DStickIterator(negativeLine.iterator(), positiveLine.iterator(), dipolePosition);
    }

    @FunctionalInterface
    interface BasicFunctionalInterface
    {
        void performTask();
    }

}