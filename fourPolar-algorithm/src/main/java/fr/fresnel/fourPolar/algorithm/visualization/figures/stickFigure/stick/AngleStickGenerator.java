package fr.fresnel.fourPolar.algorithm.visualization.figures.stickFigure.stick;

import java.util.Objects;

import fr.fresnel.fourPolar.algorithm.exceptions.visualization.figures.stickFigure.AngleStickUndefined;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.util.colorMap.ColorMap;
import fr.fresnel.fourPolar.core.visualization.figures.stickFigure.stick.IAngleStick;
import fr.fresnel.fourPolar.core.visualization.figures.stickFigure.stick.IAngleStickIterator;

/**
 * A generator of {@link IAngleStick}. Note that the sticks can have negative
 * coordinates in this implementation.
 */
public class AngleStickGenerator {
    private final ColorMap _colorMap;

    /**
     * Define a colormap, to choose the stick colors from.
     */
    public AngleStickGenerator(ColorMap colorMap) {
        Objects.requireNonNull(colorMap, "colormap cannot be null.");
        this._colorMap = colorMap;
    }

    /**
     * Generates an {@link IAngleStick}, where the slope is the equal to the slope angle, 
     * and the color of the stick is chosen from the color map using the color map, with
     * respect to the maximum possible value of that angle.
     * 
     * @param slopeAngle     is the angle that would be the slope of the angle.
     * @param colorAngle     is the angle that assigns the color of the stick.
     * @param maxColorAngle  is the maximum possible for {@param colorAngle}. Used
     *                       for getting a color from the color map.
     * @param dipolePosition is the corresponding dipole discrete position, as [x,
     *                       y, z, ...].
     * @param length         is the desired length of the dipole, must be greater
     *                       than equal 1.
     * @param thickness      is the desired thickness of the dipole, must be greater
     *                       than equal 1.
     * @return the generated angle stick.
     *
     * @throws AngleStickUndefined if rho angle is NaN.
     */
    public IAngleStick generate2DStick(double slopeAngle, double colorAngle, double maxColorAngle,
            long[] dipolePosition, int length, int thickness) throws AngleStickUndefined {
        if (Double.isNaN(slopeAngle) || Double.isNaN(colorAngle) || Double.isNaN(maxColorAngle)) {
            throw new AngleStickUndefined("Can't define stick for undefined ");
        }

        if (length <= 0) {
            throw new AngleStickUndefined("Stick length must be positive.");
        }

        if (thickness <= 0) {
            throw new AngleStickUndefined("Stick thickness must be positive.");
        }

        RGB16 color = _colorMap.getColor(0, maxColorAngle, colorAngle);
        IAngleStickIterator iterator = Angle2DStickIterator.form(slopeAngle, dipolePosition, length, thickness);

        return new AngleStick(dipolePosition, slopeAngle, length, thickness, color, iterator);
    }

}